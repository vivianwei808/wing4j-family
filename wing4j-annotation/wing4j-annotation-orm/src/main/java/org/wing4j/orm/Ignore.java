package org.wing4j.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标记此注解的字段将忽略，标记此注解的数据访问对象将忽略
 */
@Target({FIELD, TYPE})
@Retention(RUNTIME)
public @interface Ignore {
}
