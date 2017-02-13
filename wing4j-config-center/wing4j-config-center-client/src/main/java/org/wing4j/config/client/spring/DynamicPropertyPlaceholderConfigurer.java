package org.wing4j.config.client.spring;

import org.wing4j.config.client.loader.ConfigCenterLoader;
import org.wing4j.config.client.enums.ConfigMode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

/**
 * Created by wing4j on 2017/2/12.
 * 动态方式的属性占位符
 */
@Slf4j
public class DynamicPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements BeanFactoryAware, EnvironmentAware, BeanPostProcessor {

    @Setter
    Environment environment;
    /**
     * 配置模式
     */
    @Setter
    ConfigMode configMode;
    /**
     * 配置中心主机地址
     */
    @Setter
    String host;
    /**
     * 配置中心主机端口号
     */
    @Setter
    int port = 5678;
    /**
     * 组织编号
     */
    @Setter
    String groupId;
    /**
     * 组件编号
     */
    @Setter
    String artifactId;
    /**
     * 版本
     */
    @Setter
    String version;
    /**
     * 环境
     */
    @Setter
    String profile;
    /**
     * 安全密钥
     */
    @Setter
    String secretKey;
    /**
     * 文件编码
     */
    String fileEncoding = "UTF-8";
    /**
     * 将获取的参数同步到SystemProperties中
     */
    @Setter
    boolean syncToSystemProperties = true;
    /**
     * 是否打印获取到的配置信息
     */
    @Setter
    boolean printConfig = true;
    /**
     * 指数退避的方式增加
     */
    @Setter
    boolean backOffRetryInterval = true;
    /**
     * 失败重试的次数
     */
    @Setter
    int maxRetryTimes = 0;
    /**
     * 失败重试的时间间隔
     */
    @Setter
    int retryIntervalSeconds = 0;
    /**
     * 最大的重试时间间隔
     */
    @Setter
    int maxRetryIntervalSeconds = 0;
    /**
     * 当保存配置失败时，重试的最多次数
     */
    int maxTrySaveTimes;
    /**
     * 保存间隔
     */
    int trySaveIntervalMs;
    /**
     * 配置中心
     */
    @Setter
    ConfigCenterLoader config = null;
    /**
     * 引导属性配置
     */
    @Setter
    Properties bootProperties = new Properties();
    /**
     * 运行时属性配置
     */
    final Properties runtimeProperties = new Properties();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //检验参数
        vlidateParams();
        //如果是本地模式，则按父类方式处理即可
        if (ConfigMode.LOCAL == configMode) {
            super.postProcessBeanFactory(beanFactory);
            return;
        }
        //最终解析出的坐标
        log.info("Finally load client's config coordinate [groupId, artifactId , version, profile] -> [{}, {}, {}, {}]", groupId, artifactId, profile, version);
        try {
            if(ConfigMode.AUTO == configMode){
                //加载引导参数
                initBootProperties();
                //处理参数
                processProperties(beanFactory, procProperties());
            }
            //初始化配置中心客户端
            initConfigCenter();
        } catch (IOException e) {
            log.error("proccess properies happens error!", e);
        }
    }

    /**
     * 初始化配置中心
     */
    void initConfigCenter() {
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
        ConfigCenterLoader.ConfigCenterLoaderBuilder builder = ConfigCenterLoader.builder();
        builder.host(host)
                .port(port)
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .profile(profile)
                .secretKey(secretKey)
                .printConfig(printConfig)
                .daemon(true)
                .syncToSystemProperties(syncToSystemProperties)
                .backOffRetryInterval(backOffRetryInterval)
                .maxRetryTimes(maxRetryTimes)
                .maxRetryIntervalSeconds(maxRetryIntervalSeconds)
                .maxTrySaveTimes(maxTrySaveTimes)
                .retryIntervalSeconds(retryIntervalSeconds)
                .runtimeProperties(runtimeProperties);
        this.config = builder.build();
        this.config.init();
    }


    /**
     * 处理参数
     *
     * @return
     * @throws IOException
     */
    Properties procProperties() throws IOException {
        //加载bootProrperties文件
        Properties overrideProperties = clone(bootProperties);
        //加载location指定的Properties文件
        Properties locationsProps = clone(mergeProperties());
        //合并
        for (Object key : locationsProps.keySet()) {
            String propertieKey = (String) key;
            overrideProperties.setProperty(propertieKey, locationsProps.getProperty(propertieKey));
        }
        //将null转换成空串
        convertProperties(overrideProperties);
        for (Object key : overrideProperties.keySet()) {
            String propertieKey = (String) key;
            this.runtimeProperties.setProperty(propertieKey, overrideProperties.getProperty(propertieKey));
        }
        return this.runtimeProperties;
    }

    /**
     * 检查参数
     */
    void vlidateParams() {
        if (isBlank(host)) {
            log.warn("host:{} is blank, so to set defalut domain!", host);
            host = "config.wing4j.org";
        }
        if (isBlank(groupId)) {
            throw new IllegalArgumentException("please set groupId!");
        }
        if (isBlank(artifactId)) {
            throw new IllegalArgumentException("please set artifactId!");
        }
        if (isBlank(version)) {
            log.warn("version:{} is blank, so to set defalut version!", version);
            version = "10";
        }
        if (isBlank(profile)) {
            log.warn("profile:{} is blank, so to set defalut profile!", profile);
            profile = "test";
        }

        if (configMode == null) {
            log.warn("configMode:{} is blank, so to set defalut configMode!", configMode);
            configMode = ConfigMode.SERVER;
        }
    }

    /**
     * 加载初始化参数
     */
    void initBootProperties() {
        String fileName = "./" + groupId + "_" + artifactId + "_" + version + "_" + profile + ".properties";
        InputStreamReader reader = null;
        try {
            log.info("prepare to load local boot file -> classpath:" + fileName);
            Resource resource = new ClassPathResource(fileName);
            reader = new InputStreamReader(resource.getInputStream(), this.fileEncoding);
            bootProperties.load(reader);
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                log.info("classpath:" + fileName + " not exist, ignore boot prorperties!");
                //do nothing
            } else {
                throw new RuntimeException(e);
            }
        } finally {
            closeQuietly(reader);
        }
    }

    boolean isBlank(String val) {
        return val == null || val.isEmpty();
    }

    @Override
    public void setFileEncoding(String encoding) {
        this.fileEncoding = encoding;
        super.setFileEncoding(this.fileEncoding);
    }

    Properties clone(Properties props) {
        Properties properties = new Properties();
        if (props == null) {
            return properties;
        }
        for (String key : props.stringPropertyNames()) {
            properties.setProperty(key, props.getProperty(key));
        }
        return properties;
    }

    public void closeQuietly(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                //处理不了
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public String getProperty(String key, String defVal) {
        return runtimeProperties.getProperty(key, defVal);
    }

    public String getProperty(String key) {
        return runtimeProperties.getProperty(key);
    }

    public Properties getProperties() {
        return runtimeProperties;
    }
}
