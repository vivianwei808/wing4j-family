package org.wing4j.config.client.loader;

import com.alibaba.fastjson.JSON;
import org.wing4j.config.client.net.Client;
import io.github.xdiamond.common.ResolvedConfigVO;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by wing4j on 2017/2/12.
 * 配置中心加载器
 */
@Data
@Builder
@Slf4j
public class ConfigCenterLoader {
    Timer timer;
    String localConfigPath;
    volatile Map<String, ResolvedConfigVO> resolvedConfigVOMap;
    Client client;

    public void init() {
        if(profile == null){
            profile = "dev";
        }
        if (maxRetryTimes == 0) {
            maxRetryTimes = Integer.MAX_VALUE;
        }
        if (retryIntervalSeconds == 0) {
            retryIntervalSeconds = 60;
        }
        if (maxRetryIntervalSeconds == 0) {
            maxRetryIntervalSeconds = 5 * 60;
        }
        if (maxTrySaveTimes == 0) {
            maxTrySaveTimes = Integer.MAX_VALUE;
        }
        if (trySaveIntervalMs == 0) {
            trySaveIntervalMs = 1000;
        }
        if (runtimeProperties == null) {
            runtimeProperties = new Properties();
        }
        resolvedConfigVOMap = new HashMap<>();
        timer = new Timer("ConfigCenter-timer-getconfig", true);
        // default config path is: /home/username/.config
        localConfigPath = System.getProperty("user.home") + File.separator + ".config";
        boolean bShouldLoadLocalConfig = true;
        client = new Client(this, host, port, backOffRetryInterval, maxRetryTimes, retryIntervalSeconds, maxRetryIntervalSeconds, daemon);
        // 首先尝试连接服务器
        ChannelFuture future = client.init();
        try {
            // 如果连接服务器成功，则尝试获得配置
            boolean await = future.await(10, TimeUnit.SECONDS);
            if (await && future.isSuccess()) {
                Future<List<ResolvedConfigVO>> configFuture = client.getConfigs(groupId, artifactId, version, profile, secretKey);
                List<ResolvedConfigVO> resolvedConfigs = configFuture.get(10, TimeUnit.SECONDS);
                if (configFuture.isSuccess()) {
                    loadConfig(resolvedConfigs);
                    log.info("load config from config-center server success. " + toProjectInfoString());
                    bShouldLoadLocalConfig = false;

                    if (printConfig) {
                        System.out.println(ResolvedConfigVO.toUTF8PropertiesString(resolvedConfigs, true));
                    }

                }
            }
            if (!future.isSuccess()) {
                log.error("can not load config-center config from server! " + toProjectInfoString(), future.cause());
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
            log.error("load config-center config from server error! " + toProjectInfoString(), e);
        }
        // 如果没有从服务器加载到配置，则从本地的备份读取
        if (bShouldLoadLocalConfig) {
            try {
                log.error("connect to config center fail,so begin to load local cache!");
                List<ResolvedConfigVO> resolvedConfigVOList = loadLocalConfig();
                this.resolvedConfigVOMap = ResolvedConfigVO.listToMap(resolvedConfigVOList);
                log.info("load config-center config " + toProjectInfoString() + " from localConfigPath:" + localConfigPath);

                if (printConfig) {
                    System.out.println(ResolvedConfigVO.toUTF8PropertiesString(resolvedConfigVOList, true));
                }
            } catch (IOException e) {
                throw new RuntimeException("load config-center localConfig error! " + toProjectInfoString(), e);
            }
        }
        //每隔30秒从服务器拿一次配置信息
        timer.schedule(new GetConfigTask(this), 30 * 1000, 30 * 1000);
    }

    /**
     * only call by Client，服务器通知Client配置有更新
     *
     * @return
     */
    public void notifyConfigChanged() {
        timer.schedule(new GetConfigTask(this), 0);
    }

    /**
     * 从服务器拿配置信息
     */
    static class GetConfigTask extends TimerTask {
        ConfigCenterLoader configCenter;

        public GetConfigTask(ConfigCenterLoader configCenter) {
            this.configCenter = configCenter;
        }

        @Override
        public void run() {
            Future<List<ResolvedConfigVO>> future = configCenter.client.getConfigs(configCenter.groupId,
                    configCenter.artifactId,
                    configCenter.version,
                    configCenter.profile,
                    configCenter.secretKey);
            try {
                List<ResolvedConfigVO> resolvedConfigVOs = future.get(10, TimeUnit.SECONDS);
                System.out.println(ResolvedConfigVO.toUTF8PropertiesString(resolvedConfigVOs, true));
                configCenter.loadConfig(resolvedConfigVOs);
            } catch (ConnectException e) {
                // 对于连接错误，这里不打印错误信息，因为会在重试任务里打印
                return;
            } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
                if (e instanceof ExecutionException) {
                    if (e.getCause() instanceof ConnectException) {
                        return;
                    }
                }
                log.error("timer to get config-center config error!", e);
            }
        }
    }

    public void destory() {
        client.destory();
        timer.cancel();
    }

    public synchronized void loadConfig(List<ResolvedConfigVO> resolvedConfigVOs) throws IOException {
        // 先保存到本地
        saveLocalConfig(ResolvedConfigVO.toJSONString(resolvedConfigVOs));
        // 再通知Listener
        Map<String, ResolvedConfigVO> oldResolvedConfigVOMap = this.resolvedConfigVOMap;
        this.resolvedConfigVOMap = ResolvedConfigVO.listToMap(resolvedConfigVOs);
        //如果新的没有，旧的有就进行删除
        for (ResolvedConfigVO resolvedConfigVO : oldResolvedConfigVOMap.values()) {
            String key = resolvedConfigVO.getConfig().getKey();
            String value = resolvedConfigVO.getConfig().getValue();
            ResolvedConfigVO newResolvedConfigVO = this.resolvedConfigVOMap.get(key);
            if(newResolvedConfigVO == null){
                if (syncToSystemProperties) {
                    System.getProperties().remove(resolvedConfigVO.getConfig().getKey());
                }
                runtimeProperties.remove(resolvedConfigVO.getConfig().getKey());
                oldResolvedConfigVOMap.remove(key);
            }
        }
        //如果新的有，旧的有就进行更新
        //如过新的有，旧得没有进行新增
        for (ResolvedConfigVO resolvedConfigVO : this.resolvedConfigVOMap.values()) {
            String key = resolvedConfigVO.getConfig().getKey();
            String value = resolvedConfigVO.getConfig().getValue();
            ResolvedConfigVO oldResolvedConfigVO = oldResolvedConfigVOMap.get(key);

            if (oldResolvedConfigVO == null) {
                if (syncToSystemProperties) {
                    System.setProperty(resolvedConfigVO.getConfig().getKey(), resolvedConfigVO.getConfig().getValue());
                }
                runtimeProperties.setProperty(resolvedConfigVO.getConfig().getKey(), resolvedConfigVO.getConfig().getValue());
            } else if (!value.equals(oldResolvedConfigVO.getConfig().getValue())) {
                if (syncToSystemProperties) {
                    System.setProperty(resolvedConfigVO.getConfig().getKey(), resolvedConfigVO.getConfig().getValue());
                }
                runtimeProperties.setProperty(resolvedConfigVO.getConfig().getKey(), resolvedConfigVO.getConfig().getValue());
            }
        }
    }
    private List<ResolvedConfigVO> loadLocalConfig() throws IOException {
        String dir = localConfigPath + File.separator + groupId + File.separator + artifactId + File.separator + version + File.separator + profile;
        String filePath = dir + File.separator + "config.json";
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                String jsonConfigString = new String(data, "UTF-8");
                List<ResolvedConfigVO> resolvedConfigVOs = JSON.parseArray(jsonConfigString, ResolvedConfigVO.class);
                return resolvedConfigVOs;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        throw new RuntimeException("close file error!" + filePath, e);
                    }
                }
            }
        } else {
            log.warn("can not found local config-center config file! filePath:" + filePath);
            return Collections.emptyList();
        }
    }

    private void saveLocalConfig(String jsonConfigString) throws IOException {
        String dirPath = localConfigPath + File.separator + groupId + File.separator + artifactId + File.separator + version + File.separator + profile;
        String configFilePath = dirPath + File.separator + "config.json";

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 先保存到临时文件，再用renameTo 原子性地改名
        File tempFile = File.createTempFile("config", ".tmp", dir);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(jsonConfigString.getBytes("UTF-8"));
            fos.flush();
            // 要先close再move，否则在windows下会提示文件被占用
            fos.close();

            int trySaveTimes = 0;
            while (true) {
                try {
                    Files.move(tempFile.toPath(), new File(configFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    log.debug("save config-center config file success. path:" + new File(configFilePath).getAbsolutePath());
                    break;
                } catch (IOException e) {
                    trySaveTimes++;
                    log.error("save config-center config file error! trySaveTimes:{}, path:{}", trySaveTimes, configFilePath, e);
                    if (trySaveTimes > this.maxTrySaveTimes) {
                        throw e;
                    }

                    try {
                        Thread.sleep(this.trySaveIntervalMs);
                    } catch (InterruptedException e1) {
                        // ignore
                    }
                }
            }

        } finally {
            if (fos != null) {
                fos.close();
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
    }

    public String getProperty(String key) {
        return runtimeProperties.getProperty(key);
    }

    public Properties getProperties() {
        return runtimeProperties;
    }

    private String toProjectInfoString() {
        return this.groupId + "|" + artifactId + "|" + version + "|" + profile;
    }

    Properties runtimeProperties;
    /**
     * 配置中心主机地址
     */
    String host;
    /**
     * 配置中心主机端口号
     */
    int port;
    /**
     * 组织编号
     */
    String groupId;
    /**
     * 组件编号
     */
    String artifactId;
    /**
     * 版本
     */
    String version;
    /**
     * 环境
     */
    String profile;
    /**
     * 安全密钥
     */
    String secretKey;

    /**
     * 是否daemon线程
     */
    boolean daemon;
    /**
     * 启动时，是否打印获取到的配置信息
     */
    boolean printConfig;
    /**
     * 获取到配置，是否同步到System Properties里
     */
    boolean syncToSystemProperties;
    /**
     * 指数退避的方式增加
     */
    boolean backOffRetryInterval;
    /**
     * 失败重试的次数
     */
    int maxRetryTimes;
    /**
     * 失败重试的时间间隔
     */
    int retryIntervalSeconds;
    /**
     * 最大的重试时间间隔
     */
    int maxRetryIntervalSeconds;
    /**
     * 当保存配置失败时，重试的最多次数
     */
    int maxTrySaveTimes;
    /**
     * 保存间隔
     */
    int trySaveIntervalMs;
}
