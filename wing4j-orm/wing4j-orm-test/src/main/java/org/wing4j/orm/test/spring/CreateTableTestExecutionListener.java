package org.wing4j.orm.test.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.wing4j.orm.entity.utils.SqlScriptUtils;
import org.wing4j.orm.test.spring.datasource.TestDataSourceUtils;
import org.wing4j.test.CreateTable;
import org.wing4j.orm.WordMode;
import org.wing4j.test.DevDataSourceType;

import java.lang.reflect.Method;

public class CreateTableTestExecutionListener extends AbstractTestExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(CreateTableTestExecutionListener.class);

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        final Method testMethod = testContext.getTestMethod();
        String dsn = TestDataSourceUtils.lookupDataSource();
        CreateTable createTable = testMethod.getAnnotation(CreateTable.class);
        if (createTable != null) {
            Class[] clazzs = createTable.entities();
            boolean test = createTable.createBeforeTest();
            boolean drop = createTable.testBeforeDrop();
            WordMode sqlMode = createTable.sqlMode();
            WordMode keywordMode = createTable.keywordMode();
            for (Class clazz : clazzs) {
                JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
                if (drop && DevDataSourceType.h2DataSource.name().equals(dsn)) {
                    String dropSql = SqlScriptUtils.generateDropTable(clazz, "", sqlMode, keywordMode, drop);
                    jdbcTemplate.execute(dropSql);
                }
                String createSql = SqlScriptUtils.generateCreateTable(clazz, "", "", sqlMode, keywordMode, test);
                jdbcTemplate.execute(createSql);
            }
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        final Method testMethod = testContext.getTestMethod();
        final Class<?> testClass = testContext.getTestClass();
        String dsn = TestDataSourceUtils.lookupDataSource();
        CreateTable createTable = testMethod.getAnnotation(CreateTable.class);
        if (createTable != null) {
            Class[] clazzs = createTable.entities();
            boolean drop = createTable.testBeforeDrop();
            WordMode sqlMode = createTable.sqlMode();
            WordMode keywordMode = createTable.keywordMode();
            if (drop && DevDataSourceType.h2DataSource.name().equals(dsn)) {
                for (Class clazz : clazzs) {
                    JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
                    String dropSql = SqlScriptUtils.generateDropTable(clazz, "", sqlMode, keywordMode, drop);
                    jdbcTemplate.execute(dropSql);
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }
}
