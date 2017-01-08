package org.wing4j.orm;

/**
 * 时间类型定义
 */
public enum DateType {
    /**
     * 自动选择
     */
    AUTO,
    /**
     * 只保存日期
     */
    DATE,
    /**
     * 只保存时间
     */
    TIME,
    /**
     * 保存时间戳
     */
    TIMESTAMP,
    /**
     * 无时区年月日时分秒
     */
    DATETIME
}