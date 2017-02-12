package org.wing4j.config.client.aspect;

import org.wing4j.config.client.annotation.DynamicConfig;
import org.wing4j.config.client.spring.DynamicPropertyPlaceholderConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Method;

/**
 * Created by wing4j on 2017/2/11.
 * 动态配置对象切面，将对添加@DynamicConfig的Bean进行切面拦截，实现动态参数
 */
@Aspect
@Slf4j
@Component
public class DynamicConfigAspectJ implements ApplicationContextAware {
    PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
    ApplicationContext applicationContext;

    /**
     * 定义切点，放在config包下的所有对象getter方法
     */
    @Pointcut(value = "execution(public * *..config.*.get*(..))")
    public void anyPublicMethod() {
    }

    /**
     * 定义切点，包含有@Value注解的Getter
     * @param joinPoint 切点
     * @return 返回值
     * @throws Throwable 异常
     */
    @Around("anyPublicMethod() && @annotation(org.springframework.beans.factory.annotation.Value)")
    public Object decideAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Class clazz = ms.getDeclaringType();
        //标注动态配置文件的对象有实时更新参数的能力
        if (clazz.getAnnotation(DynamicConfig.class) == null) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        Method method = ms.getMethod();
        log.debug(method.toString());
        Value value = method.getAnnotation(Value.class);
        if(method.getParameterTypes().length > 0){
            log.error("错误使用@Value注解在非Getter上");
            return joinPoint.proceed(joinPoint.getArgs());
        }
        DynamicPropertyPlaceholderConfigurer configurer = applicationContext.getBean(DynamicPropertyPlaceholderConfigurer.class);
        String key = value.value();//把@Value中的value处理
        String remoteVal = null;
        try {
            remoteVal = placeholderHelper.replacePlaceholders(key, configurer.getProperties());
            //TODO 增加数据类型转换函数
            return remoteVal;
        } catch (Exception e) {
            log.error("get remote prorperties happens error, key : {]", key);
            //如果远程不存在值，说明远程没有，则使用本地的
            Object oldValue = joinPoint.proceed();
            return oldValue;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
