package org.wing4j.network.download.manager;

import org.junit.Test;
import org.wing4j.network.download.DownloadManager;
import org.wing4j.network.download.RemoteUrl;
import org.wing4j.network.download.retry.RetryStrategyRoundRobin;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/2/26.
 */
public class DefaultDownloadManagerTest {

    @Test
    public void testDownload() throws Exception {
        DownloadManager restfulManager = new DefaultDownloadManager(new RetryStrategyRoundRobin());
        RemoteUrl remoteUrl = new RemoteUrl("explore/recommend", Arrays.asList("http://git.oschina.net"));
        //http://apache.fayea.com/zookeeper/HEADER.html
        restfulManager.download(remoteUrl, "recommend", "./target/temp", "./download", 3, 1000);
    }
}