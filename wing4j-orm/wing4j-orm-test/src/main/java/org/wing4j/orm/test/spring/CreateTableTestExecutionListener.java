package org.wing4j.orm.test.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.utils.SqlScriptUtils;
import org.wing4j.orm.test.spring.datasource.TestDataSourceUtils;
import org.wing4j.test.CreateTable;
import org.wing4j.test.DevDataSourceType;
import org.wing4j.test.TableNameMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自动创建表监听器
 */
@Slf4j
public class CreateTableTestExecutionListener extends AbstractTestExecutionListener{
    ApplicationContext ctx;

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        final Class testClass = testContext.getTestClass();

    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        final Class testClass = testContext.getTestClass();

    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        final Class testClass = testContext.getTestClass();
        final Method testMethod = testContext.getTestMethod();
        String dsn = TestDataSourceUtils.lookupDataSource();
        CreateTable classCreateTable = (CreateTable)testClass.getAnnotation(CreateTable.class);
        List<Class> entitys = new ArrayList<>();
        if(classCreateTable != null){
            entitys.addAll(Arrays.asList(classCreateTable.entities()));
        }
        CreateTable methodCreateTable = (CreateTable)testMethod.getAnnotation(CreateTable.class);
        if (methodCreateTable != null) {
            Class[] clazzs = methodCreateTable.entities();
            boolean test = methodCreateTable.createBeforeTest();
            boolean drop = methodCreateTable.testBeforeDrop();
            WordMode sqlMode = methodCreateTable.sqlMode();
            WordMode keywordMode = methodCreateTable.keywordMode();
            String schema = methodCreateTable.schema();
            String prefix = methodCreateTable.prefix();
            String suffix = methodCreateTable.suffix();
            TableNameMode schemaMode = methodCreateTable.schemaMode();
            TableNameMode prefixMode = methodCreateTable.prefixMode();
            TableNameMode suffixMode = methodCreateTable.suffixMode();
            if(schemaMode == TableNameMode.entity){
                schema = null;
            }
            if(prefixMode == TableNameMode.entity){
                prefix = null;
            }
            if(suffixMode == TableNameMode.entity){
                suffix = null;
            }
            for (Class clazz : clazzs) {
                JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
                if (drop) {
                    String dropSql = SqlScriptUtils.generateDropTable(clazz, schemaMode, schema, prefixMode, prefix, suffixMode, suffix, sqlMode, keywordMode, drop);
                    jdbcTemplate.execute(dropSql);
                }
                String createSql = SqlScriptUtils.generateCreateTable(clazz, schemaMode, schema, prefixMode, prefix, suffixMode, suffix, null, sqlMode, keywordMode, test);
                jdbcTemplate.execute(createSql);
            }
            //重新加载Mapper
        }

    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        final Method testMethod = testContext.getTestMethod();
        final Class<?> testClass = testContext.getTestClass();
        String dsn = TestDataSourceUtils.lookupDataSource();
        CreateTable classCreateTable = (CreateTable)testClass.getAnnotation(CreateTable.class);
        List<Class> entitys = new ArrayList<>();
        if(classCreateTable != null){
            entitys.addAll(Arrays.asList(classCreateTable.entities()));
        }
        CreateTable methodCreateTable = (CreateTable)testMethod.getAnnotation(CreateTable.class);
        if (methodCreateTable != null) {
            Class[] clazzs = methodCreateTable.entities();
            boolean drop = methodCreateTable.testBeforeDrop();
            WordMode sqlMode = methodCreateTable.sqlMode();
            WordMode keywordMode = methodCreateTable.keywordMode();
            String schema = methodCreateTable.schema();
            String prefix = methodCreateTable.prefix();
            String suffix = methodCreateTable.suffix();
            TableNameMode schemaMode = methodCreateTable.schemaMode();
            TableNameMode prefixMode = methodCreateTable.prefixMode();
            TableNameMode suffixMode = methodCreateTable.suffixMode();
            if(schemaMode == TableNameMode.entity){
                schema = null;
            }
            if(prefixMode == TableNameMode.entity){
                prefix = null;
            }
            if(suffixMode == TableNameMode.entity){
                suffix = null;
            };
            if (drop && DevDataSourceType.h2DataSource.name().equals(dsn)) {
                for (Class clazz : clazzs) {
                    JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
                    String createSql = SqlScriptUtils.generateCreateTable(clazz, schemaMode, schema, prefixMode, prefix, suffixMode, suffix, null, sqlMode, keywordMode, true);
                    jdbcTemplate.execute(createSql);
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }

}
