package org.wing4j.config.client.net;

import io.github.xdiamond.common.ResolvedConfigVO;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.util.List;

/**
 * Created by woate on 2017/2/10.
 * 20:11
 */
public class ClientTest {

    @Test
    public void testInit() throws Exception {
        String profile = "test";
        String version = "0.0.1-SNAPSHOT";
        String artifactId = "xdiamond-client-example";
        String groupId = "io.github.xdiamond";
        String secretKey = "";
        Client client = new Client();
        client.setHost("localhost");
        client.setPort(5678);
        client.init();
        Future<List<ResolvedConfigVO>> future = client.getConfigs(groupId, artifactId, version, profile, secretKey);
        System.err.println("config:" + future);
    }
}