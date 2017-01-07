package org.wing4j.orm.mysql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记实体生成的MySQL语句使用什么数据引擎<br>
 *     用于标记在实体类上，用于指定使用的MySQL存储引擎，默认使用数据库的存储引擎
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataEngine {
    /**
     * 数据引擎
     * @return 数据引擎
     */
    DataEngineType value() default DataEngineType.Auto;
}
