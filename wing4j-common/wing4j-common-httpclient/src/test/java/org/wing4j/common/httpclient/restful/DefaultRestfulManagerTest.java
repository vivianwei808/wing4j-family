package org.wing4j.common.httpclient.restful;

import org.junit.Test;
import org.wing4j.common.httpclient.RemoteUrl;
import org.wing4j.common.httpclient.RestfulManager;
import org.wing4j.common.httpclient.retry.RetryStrategyRoundBin;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/2/7.
 */
public class DefaultRestfulManagerTest {

    @Test
    public void testDownload() throws Exception {
        RestfulManager restfulManager = new DefaultRestfulManager(new RetryStrategyRoundBin());
        RemoteUrl remoteUrl = new RemoteUrl("explore/recommend", Arrays.asList("http://git.oschina.net"));
        //http://apache.fayea.com/zookeeper/HEADER.html
        restfulManager.download(remoteUrl, "recommend", "./temp", "./download", 3, 1000);
    }
}