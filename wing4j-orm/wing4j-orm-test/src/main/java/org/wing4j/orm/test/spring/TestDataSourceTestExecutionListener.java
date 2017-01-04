package org.wing4j.orm.test.spring;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.wing4j.orm.test.spring.datasource.TestDataSourceUtils;
import org.wing4j.orm.test.TestRuntimeException;
import org.wing4j.test.DevDataSource;

/**
 * 测试数据源监听器
 */
public class TestDataSourceTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);
        final Class<?> testClass = testContext.getTestClass();
        DevDataSource ds = testClass.getAnnotation(DevDataSource.class);
        if(ds != null){
            String dsn = ds.value().name();
            String infaceName = TestDataSourceUtils.setDevDateSourceName(dsn);
            if(!dsn.equals(infaceName)){
                throw new TestRuntimeException("Test Datasource fail!");
            }
        }

    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);
        TestDataSourceUtils.setDevDateSourceName(null);
    }


    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 2;
    }
}
