package org.wing4j.common.download.manager;

import org.junit.Test;
import org.wing4j.common.download.RemoteUrl;
import org.wing4j.common.download.DownloadManager;
import org.wing4j.common.download.retry.RetryStrategyRoundBin;

import java.util.Arrays;

/**
 * Created by wing4j on 2017/2/7.
 */
public class DefaultRestfulManagerTest {

    @Test
    public void testDownload() throws Exception {
        DownloadManager restfulManager = new DefaultDownloadManager(new RetryStrategyRoundBin());
        RemoteUrl remoteUrl = new RemoteUrl("explore/recommend", Arrays.asList("http://git.oschina.net"));
        //http://apache.fayea.com/zookeeper/HEADER.html
        restfulManager.download(remoteUrl, "recommend", "./temp", "./download", 3, 1000);
    }
}