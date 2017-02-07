package org.wing4j.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 机器封装
 */
public abstract class MachineInfoUtils {

    /**
     * 获取机器名
     * @return 机器名
     */
    public static String getHostName(){
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            return hostname;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取机器IP
     * @return 机器IP
     */
    public static String getHostIp() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            return ip;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}