package org.wing4j.config.client.loader;

import org.junit.Test;

import java.util.Properties;

/**
 * Created by woate on 2017/2/10.
 * 20:07
 */
public class ConfigCenterLoaderTest {

    @Test
    public void testInit() throws Exception {
        String profile = "test";
        String version = "0.0.1-SNAPSHOT";
        String artifactId = "xdiamond-client-example";
        String groupId = "io.github.xdiamond";
        String secretKey = "";

        String serverHost = "localhost";
        int port = 5678;
        ConfigCenterLoader config = ConfigCenterLoader.builder()
                .host(serverHost)
                .port(port)
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .profile(profile)
                .secretKey(secretKey)
                .syncToSystemProperties(true)
                .printConfig(true)
                .backOffRetryInterval(true)
                .daemon(true)
                .build();
        config.init();

        while (true){
            String serverlist = config.getProperty("memcached.serverlist");
            if(serverlist==null){
                Properties properties = System.getProperties();
                System.out.println();
            }
            String zookeeperAddress = config.getProperty("zookeeper.address");
            System.out.println(serverlist);
            System.out.println(zookeeperAddress);
            Thread.sleep(500);
        }

    }
}