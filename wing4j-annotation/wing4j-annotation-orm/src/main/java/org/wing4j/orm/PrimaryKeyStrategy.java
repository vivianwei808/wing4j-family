package org.wing4j.orm;

/**
 * Created by wing4j on 2016/12/17.
 */
public enum PrimaryKeyStrategy {
    /**
     * UUID
     */
    UUID,
    /**
     * 数据库序列
     */
    SEQUENCE,
    /**
     * 数据库的自动增长字段
     */
    IDENTITY,
    /**
     * 自动选择
     */
    AUTO
}
