package org.wing4j.orm.mybatis.mapper.builder;


import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.wing4j.orm.test.spring.CreateTableTestExecutionListener;
import org.wing4j.orm.test.spring.TestDataSourceTestExecutionListener;
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({
        TestDataSourceTestExecutionListener.class,
        CreateTableTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class})
public abstract class BaseTest implements ApplicationContextAware {
    protected ApplicationContext ctx;
    public <T> T getBean(Class clazz){
        return (T) ctx.getBean(clazz);
    }
    public <T> T getBean(String clazzName){
        return (T) ctx.getBean(clazzName);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }
}
