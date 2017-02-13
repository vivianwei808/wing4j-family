package org.wing4j.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解标注在实体类上，用于声明对应表名等信息
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 表名
     * @return 表名
     * @since 1.0.1
     */
    String name() default "";

    /**
     * 实体所属模式
     * @return 模式
     * @since 1.0.1
     */
    String schema() default "";

    /**
     * 表前缀
     * @return 表前缀
     * @since 1.0.2
     */
    String prefix() default "";

    /**
     * 表后缀
     * @return 表后缀
     * @since 1.0.2
     */
    String suffix() default "";
    /**
     * 关键字的单词模式
     * 例如select drop delete update where 等
     * @return 单词模式
     * @since 1.0.1
     */
    WordMode keywordMode() default WordMode.lowerCase;

    /**
     * SQL语句使用的单词模式
     * 例如 select col1 from table1, col1和table1就是SQL语句
     * @return 单词模式
     * @since 1.0.1
     */
    WordMode sqlMode() default WordMode.upperCase;
}
