package org.wing4j.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.wing4j.orm.test.spring.CreateTableTestExecutionListener;
import org.wing4j.orm.test.spring.TestDataSourceTestExecutionListener;

/**
 * 数据库访问测试基类<br>
 * 拥有开发数据源选择
 * 自动创建表结构
 * 事务管理
 * SQL初始化脚本执行
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({
        TestDataSourceTestExecutionListener.class,
        CreateTableTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class})
public abstract class DbBaseTest extends BaseTest implements InitializingBean {
    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jdbcTemplate, "JdbcTemplate is required!");
    }
}
