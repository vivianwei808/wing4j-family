package org.wing4j.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String name() default "";

    String schema() default "";
    /**
     * 关键字的单词模式
     * 例如select drop delete update where 等
     * @return 单词模式
     */
    WordMode keywordMode() default WordMode.lowerCase;

    /**
     * SQL语句使用的单词模式
     * 例如 select col1 from table1, col1和table1就是SQL语句
     * @return 单词模式
     */
    WordMode sqlMode() default WordMode.upperCase;
}
