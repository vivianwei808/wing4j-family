package org.wing4j.config.core;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by wing4j on 2017/2/15.
 */
@Data
@ToString
@Builder
@EqualsAndHashCode
public class Project {
    /**
     * 组织编码
     */
    String groupId;
    /**
     * 组件编码
     */
    String artifactId;
    /**
     * 机器编号
     */
    String machineId;
    /**
     * 版本号
     */
    String version;
    /**
     * 描述
     */
    String description;
}
