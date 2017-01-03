package org.wing4j.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开发数据源注解，用于选择开始环境使用的数据源
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DevDataSource {
    /**
     * 数据源类型
     * @return 数据源类型
     */
    DevDataSourceType value() default DevDataSourceType.h2DataSource;
}
