package org.wing4j.network.download.handler;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.network.download.UnreliableHandler;

import java.io.File;
import java.net.URL;

/**
 * Created by woate on 2017/2/13.
 * 12:34
 */
@Slf4j
public class FtpFileHandler  implements UnreliableHandler {
    URL remoteUrl;
    File localTmpFile;
    @Override
    public <T> T call() throws Exception {
        // 删除临时文件
        log.info("start to remove tmp download file: {}", localTmpFile.getAbsolutePath());
        if (localTmpFile.exists()) {
            localTmpFile.delete();
        }
        // start tp download
        log.debug("start to download. From:{} , TO: {}",  remoteUrl, localTmpFile.getAbsolutePath());
        return null;
    }
}
