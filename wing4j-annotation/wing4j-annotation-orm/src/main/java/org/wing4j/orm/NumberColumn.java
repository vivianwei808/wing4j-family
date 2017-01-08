package org.wing4j.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
/**
 * 数字类型的字段定义
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberColumn {
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
     * 默认值
     * @return 默认值
     */
    String defaultValue() default "";
    /**
     * 数字小数部分
     * @return 数字小数部分精度
     */
    int precision() default 0;

    /**
     * 整数部分
     * @return 整数部分精度
     */
    int scale() default 0;

    /**
     * 数字类型
     * @return 数字类型
     */
    NumberType type() default NumberType.AUTO;
}
