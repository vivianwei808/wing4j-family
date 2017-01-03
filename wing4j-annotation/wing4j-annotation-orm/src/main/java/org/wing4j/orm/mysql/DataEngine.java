package org.wing4j.orm.mysql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记实体生成的MySQL语句使用什么数据引擎
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataEngine {
    DataEngineType value() default DataEngineType.NONE;
}
