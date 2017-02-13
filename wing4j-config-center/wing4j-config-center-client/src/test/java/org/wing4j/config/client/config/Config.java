package org.wing4j.config.client.config;

import org.wing4j.config.client.annotation.DynamicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by woate on 2017/2/10.
 * 20:38
 */
@DynamicConfig
public class Config {
    @Value("${memcached.serverlist}")
    String param1;
    @Value("${zookeeper.address}")
    String param2;

    public String getParam1() {
        return param1;
    }
    public void ssssss(){

    }
    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    @Value("${zookeeper.address}")
    public String getParam2() {
        return param2;
    }
}
