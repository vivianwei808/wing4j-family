package org.wing4j.common.httpclient.restful;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.httpclient.RemoteUrl;
import org.wing4j.common.httpclient.RestfulManager;
import org.wing4j.common.httpclient.RetryStrategy;
import org.wing4j.common.httpclient.UnreliableHandler;
import org.wing4j.common.httpclient.handler.FetchFileHandler;
import org.wing4j.common.utils.FileSystemUtils;
import org.wing4j.common.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by wing4j on 2017/2/7.
 */
@Slf4j
public class DefaultRestfulManager implements RestfulManager {
    RetryStrategy retryStrategy;

    public DefaultRestfulManager(RetryStrategy retryStrategy) {
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

    Object retry(RemoteUrl remoteUrl,
                 File tempDirPath,
                 int retryTimes,
                 int retryIntervalSeconds) {
        Exception ex = null;
        for (URL url : remoteUrl.getUrls()) {
            UnreliableHandler unreliableHandler = new FetchFileHandler(url, tempDirPath);
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
