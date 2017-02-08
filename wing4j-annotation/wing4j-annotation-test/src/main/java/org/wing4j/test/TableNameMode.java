package org.wing4j.test;

/**
 * Created by wing4j on 2017/2/8.
 */
public enum TableNameMode {
    /**
     * 自动识别
     */
    auto,
    /**
     * 使用实体上的
     */
    entity,
    /**
     * 使用@CreateTable上的
     */
    createTest
}
