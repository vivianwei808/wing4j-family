package org.wing4j.orm.mybatis.mapper.builder.select;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.wing4j.orm.Constants;
import org.wing4j.orm.DatabaseType;
import org.wing4j.orm.Pagination;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.mybatis.mapper.builder.BaseTest;
import org.wing4j.orm.mybatis.mapper.builder.DemoCrudMapper;
import org.wing4j.orm.mybatis.mapper.builder.DemoEntity;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.insert.InsertMappedStatementBuilder;
import org.wing4j.orm.mybatis.plugins.PaginationStage1Interceptor;
import org.wing4j.orm.mybatis.plugins.PaginationStage2Interceptor;
import org.wing4j.orm.mybatis.sequnece.SequenceServiceConfigure;
import org.wing4j.orm.mybatis.spring.transaction.SpringManagedTransaction;
import org.wing4j.test.CreateTable;
import org.wing4j.test.TableNameMode;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"classpath*:testContext-builder.xml"})
public class SelectPageAndMappedStatementBuilderTest extends BaseTest {
    @Autowired
    DataSource dataSource;

    @CreateTable(entities = DemoEntity.class, sqlMode = WordMode.upperCase, keywordMode = WordMode.upperCase)
    @Test
    public void testBuild_upper() throws Exception {
        //创建配置文件
        final Configuration config = new Configuration();
        config.setCacheEnabled(true);
        config.setLazyLoadingEnabled(false);
        config.setAggressiveLazyLoading(true);
        Transaction transaction = new SpringManagedTransaction(dataSource);
        SequenceServiceConfigure sequenceServiceConfigure = getBean(SequenceServiceConfigure.class);
        final Executor executor = config.newExecutor(transaction);
        {
            MappedStatementBuilder builder = new InsertMappedStatementBuilder(config, DemoCrudMapper.class, sequenceServiceConfigure);
            builder.setKeywordMode(WordMode.lowerCase);
            builder.setSqlMode(WordMode.lowerCase);
            builder.setSchemaMode(TableNameMode.auto);
            builder.setPrefixMode(TableNameMode.auto);
            MappedStatement ms = builder.build();
            config.addMappedStatement(ms);
            SqlSession sqlSession = new DefaultSqlSession(config, executor, false);
            {
                DemoEntity demoEntity = new DemoEntity();
                demoEntity.setSerialNo(UUID.randomUUID().toString());
                demoEntity.setCol1("col1_test1");
                demoEntity.setCol2(BigDecimal.ONE);
                demoEntity.setCol3(1);
                int cnt = sqlSession.insert(Constants.INSERT, demoEntity);
                Assert.assertEquals(1, cnt);
            }
            {
                DemoEntity demoEntity = new DemoEntity();
                demoEntity.setSerialNo(UUID.randomUUID().toString());
                demoEntity.setCol1("col1_test1");
                demoEntity.setCol2(BigDecimal.ZERO);
                demoEntity.setCol3(1);
                int cnt = sqlSession.insert(Constants.INSERT, demoEntity);
                Assert.assertEquals(1, cnt);
            }
            {
                DemoEntity demoEntity = new DemoEntity();
                demoEntity.setSerialNo(UUID.randomUUID().toString());
                demoEntity.setCol1("col1_test1");
                demoEntity.setCol2(BigDecimal.ZERO);
                demoEntity.setCol3(1);
                int cnt = sqlSession.insert(Constants.INSERT, demoEntity);
                Assert.assertEquals(1, cnt);
            }
            {
                DemoEntity demoEntity = new DemoEntity();
                demoEntity.setSerialNo(UUID.randomUUID().toString());
                demoEntity.setCol1("col1_test1");
                demoEntity.setCol2(BigDecimal.ZERO);
                demoEntity.setCol3(1);
                int cnt = sqlSession.insert(Constants.INSERT, demoEntity);
                Assert.assertEquals(1, cnt);
            }
        }
        config.addInterceptor(new PaginationStage1Interceptor(DatabaseType.MySQL));
        config.addInterceptor(new PaginationStage2Interceptor());
        {

            MappedStatementBuilder builder = new SelectPageAndMappedStatementBuilder(config, DemoCrudMapper.class);
            builder.setKeywordMode(WordMode.lowerCase);
            builder.setSqlMode(WordMode.lowerCase);
            builder.setSchemaMode(TableNameMode.auto);
            builder.setPrefixMode(TableNameMode.auto);
            MappedStatement ms = builder.build();
            config.addMappedStatement(ms);
            SqlSession sqlSession = new DefaultSqlSession(config, executor, false);

            DemoEntity demoEntity = new DemoEntity();
            demoEntity.setCol1("col1_test1");
//            demoEntity.setCol2(BigDecimal.ONE);
            Pagination<DemoEntity> pagination = new Pagination<DemoEntity>(2, 1, demoEntity);
            List<DemoEntity> list = sqlSession.selectList(Constants.SELECT_PAGE_AND, pagination);
            pagination.setRecords(list);
            Assert.assertEquals(4, pagination.getTotal());
            Assert.assertEquals(1, pagination.getCurPageNo());
            Assert.assertEquals(2, pagination.getPageSize());
            Assert.assertEquals("col1_test1", list.get(0).getCol1());
            Assert.assertEquals(BigDecimal.ONE.intValue(), list.get(0).getCol2().intValue());
            Assert.assertEquals(BigDecimal.ZERO.intValue(), list.get(1).getCol2().intValue());
        }
    }
}