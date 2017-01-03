package org.wing4j.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    /**
     * 主键生成规则
     * @return 主键生成规则
     */
    PrimaryKeyStrategy strategy() default PrimaryKeyStrategy.UUID;
    /**
     * 用于指定主键生成的特征
     * @return 主键特征
     */
    String feature() default "";
}
