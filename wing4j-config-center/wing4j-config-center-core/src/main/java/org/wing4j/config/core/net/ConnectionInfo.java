package org.wing4j.config.core.net;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by wing4j on 2017/2/14.
 */
@Data
@ToString
@EqualsAndHashCode
public class ConnectionInfo {
    /**
     * 组织编码
     */
    String groupId;
    /**
     * 组件编码
     */
    String artifactId;
    /**
     * 版本号
     */
    String version;
    /**
     * 配置环境
     */
    String profile;
    /**
     * 机器编号
     */
    String machineId;
    /**
     * 远程IP地址
     */
    String remoteAddress;
    /**
     * 连接时间
     */
    long connectTime;
    /**
     * 信息
     */
    String message;
}