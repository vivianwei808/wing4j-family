package org.wing4j.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
/**
 * 字符串类型的数据定义
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringColumn {
    /**
     * 字段名称
     *
     * @return 字段名称
     */
    String name() default "";

    /**
     * 是否允许为空
     *
     * @return 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 字段长度
     * 超过255的VARCHAR，在MySQL数据库自动使用TEXT
     * @return 字段长度
     */
    int length() default 255;

    /**
     * 默认值
     * @return 默认值
     */
    String defaultValue() default "";

    /**
     * 字段数据类型
     *
     * @return 数据类型
     */
    StringType type() default StringType.VARCHAR;
}

