package org.wing4j.config.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by wing4j on 2017/2/11.
 * 动态配置对象，该注解将使用Spring Bean形式,凡是@Value标注的getter的返回值都是动态获取参数
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DynamicConfig {
    /**
     * 配置对象名字
     * @return 对象名字
     */
    String value() default "";
}
