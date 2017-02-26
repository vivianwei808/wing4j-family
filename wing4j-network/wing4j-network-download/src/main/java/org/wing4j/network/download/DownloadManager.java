package org.wing4j.network.download;

import java.io.IOException;

/**
 * Created by wing4j on 2017/2/7.
 */
public interface DownloadManager {
    /**
     * 下载
     * @param remoteUrl 远程服务器地址
     * @param fileName 文件名
     * @param tempDirPath 临时目录
     * @param targetDirPath 下载目标目录
     * @param retryTimes 重试次数
     * @param retryIntervalSeconds 每次重试间隔时间
     * @return 下载后文件路径
     * @throws IOException 异常
     */
    String download(RemoteUrl remoteUrl,
                    String fileName,
                    String tempDirPath,
                    String targetDirPath,
                    int retryTimes,
                    int retryIntervalSeconds) throws IOException;
}
