package org.wing4j.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以标记在实体类上，也可以标记在实体字段上<br>
 *
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    /**
     * 表或者字段的注释
     *
     * @return 注释
     */
    String value();
}
