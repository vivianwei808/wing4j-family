package org.wing4j.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.util.Assert;
import org.wing4j.test.util.MavenTestResourceLookup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 无数据库访问能力测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {ServletTestExecutionListener.class,
                DirtiesContextTestExecutionListener.class,
                DependencyInjectionTestExecutionListener.class})
@Slf4j
public abstract class BaseTest implements ApplicationContextAware {
    /**
     * Spring的上下文
     */
    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 通过Bean名称获取Bean实例
     * @param name Bean名称
     * @param <T> Bean
     * @return Bean实例
     */
    public <T> T getBean(String name) {
        Assert.notNull(name);
        T bean = (T) this.applicationContext.getBean(name);
        Assert.notNull(bean);
        return bean;
    }
    /**
     * 通过Bean类型获取Bean实例
     * @param clazz Bean类型
     * @param <T> Bean
     * @return Bean实例
     */
    public <T> T getBean(Class<T> clazz) {
        Assert.notNull(clazz);
        T bean = (T) this.applicationContext.getBean(clazz);
        Assert.notNull(bean);
        return bean;
    }

    /**
     * 打开Maven测试资源
     * @param fileName 文件名
     * @return 输入流
     */
    public InputStream openMavenTestResouce(String fileName){
        try {
            URI uri = MavenTestResourceLookup.open(fileName);
            return uri.toURL().openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
