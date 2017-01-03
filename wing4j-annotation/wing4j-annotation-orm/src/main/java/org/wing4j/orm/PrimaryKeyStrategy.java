package org.wing4j.orm;

/**
 * Created by wing4j on 2016/12/17.
 */
public enum PrimaryKeyStrategy {
    UUID,
    SEQUENCE,
    /**
     * 数据库的自动增长字段
     */
    IDENTITY,
    AUTO
}
