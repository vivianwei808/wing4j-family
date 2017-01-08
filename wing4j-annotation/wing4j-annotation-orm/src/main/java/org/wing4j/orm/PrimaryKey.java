package org.wing4j.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注该注解的字段是主键，支持多个主键，但是数据访问对象只支持一个物理主键
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    /**
     * 主键生成规则
     *
     * @return 主键生成规则
     */
    PrimaryKeyStrategy strategy() default PrimaryKeyStrategy.UUID;

    /**
     * 用于指定主键生成的特征
     * <ol>
     * <li>${new Date()}</li>
     * <li>${yyyyMMdd}</li>
     * <li>${yyyyMMddHH}</li>
     * <li>${yyyyMMddHHmm}</li>
     * <li>${yyyyMMddHHmmss}</li>
     * <li>${yyyyMMddHHmmssSSS}</li>
     * <li>固定字符串
     * <p>
     * </ol>
     * @see PrimaryKeyFeatureConstant
     * @return 主键特征
     */
    String feature() default "";
}
