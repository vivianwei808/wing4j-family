package org.wing4j.network.download.manager;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.utils.FileSystemUtils;
import org.wing4j.network.download.DownloadManager;
import org.wing4j.network.download.RemoteUrl;
import org.wing4j.network.download.RetryStrategy;
import org.wing4j.network.download.UnreliableHandler;
import org.wing4j.network.download.handler.HttpFileHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by wing4j on 2017/2/7.
 */
@Slf4j
public class DefaultDownloadManager implements DownloadManager {
    RetryStrategy retryStrategy;

    public DefaultDownloadManager(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    @Override
    public String download(RemoteUrl remoteUrl,
                           String fileName,
                           String tempDirPath,
                           String targetDirPath,
                           int retryTimes,
                           int retryIntervalSeconds) throws IOException {
        if (tempDirPath == null) {
            tempDirPath = "./temp";
        }
        String randomFileName = FileSystemUtils.randomFileName(fileName);
        String tempPath = FileSystemUtils.pathJoin(tempDirPath, randomFileName);
        retry(remoteUrl, new File(tempPath), retryTimes, retryIntervalSeconds);
        return tempPath;
    }

    /**
     * 多个源地址实现软负载
     * @param remoteUrl 源地址
     * @param tempDirPath 临时文件路径
     * @param retryTimes 重试次数
     * @param retryIntervalSeconds 重试间隔秒数
     * @return
     */
    Object retry(RemoteUrl remoteUrl,
                 File tempDirPath,
                 int retryTimes,
                 int retryIntervalSeconds) {
        Exception ex = null;
        for (URL url : remoteUrl.getUrls()) {
            UnreliableHandler unreliableHandler = new HttpFileHandler(url, tempDirPath);
            try {
                return retryStrategy.retry(unreliableHandler, retryTimes, retryIntervalSeconds);
            } catch (Exception e) {
                ex = e;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    log.info("pass");
                }
            }
        }
        throw new RuntimeException("download failed.", ex);
    }
}
