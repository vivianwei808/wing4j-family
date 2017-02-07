package org.wing4j.common.httpclient.handler;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.httpclient.UnreliableHandler;
import org.wing4j.common.utils.FileSystemUtils;
import org.wing4j.common.utils.FileUtils;

import java.io.File;
import java.net.URL;

/**
 * Created by wing4j on 2017/2/7.
 * 下载远程文件到本地临时目录
 */
@Slf4j
public class FetchFileHandler implements UnreliableHandler {
    URL remoteUrl;
    File localTmpFile;

    public FetchFileHandler(URL remoteUrl, File localTmpFile) {
        this.remoteUrl = remoteUrl;
        this.localTmpFile = localTmpFile;
    }

    @Override
    public <T> T call() throws Exception {
        // 删除临时文件
        // LOGGER.info("start to remove tmp download file: " + ""
        // + localTmpFile.getAbsolutePath());
        if (localTmpFile.exists()) {
            localTmpFile.delete();
        }
        // start tp download
        log.debug("start to download. From: " + remoteUrl + " , TO: " + localTmpFile.getAbsolutePath());
        // 下载
        FileUtils.copyURLToFile(remoteUrl, localTmpFile);
        // check
        if (!FileSystemUtils.exists(localTmpFile.getAbsolutePath())) {
            throw new Exception("download is ok, but cannot find downloaded file." + localTmpFile);
        }
        // download success
        log.debug("download success!  " + localTmpFile.getAbsolutePath());
        return null;
    }
}
