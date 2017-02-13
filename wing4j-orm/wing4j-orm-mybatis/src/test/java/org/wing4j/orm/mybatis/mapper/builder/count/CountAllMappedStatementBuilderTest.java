package org.wing4j.orm.mybatis.mapper.builder.count;

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
import org.wing4j.orm.WordMode;
import org.wing4j.orm.mybatis.mapper.builder.BaseTest;
import org.wing4j.orm.mybatis.mapper.builder.DemoCrudMapper;
import org.wing4j.orm.mybatis.mapper.builder.DemoEntity;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.insert.InsertMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.select.SelectAndMappedStatementBuilder;
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
public class CountAllMappedStatementBuilderTest extends BaseTest {
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
                demoEntity.setCol1("col1_test2");
                demoEntity.setCol2(BigDecimal.ZERO);
                demoEntity.setCol3(1);
                int cnt = sqlSession.insert(Constants.INSERT, demoEntity);
                Assert.assertEquals(1, cnt);
            }
        }
        MappedStatementBuilder builder = new CountAllMappedStatementBuilder(config, DemoCrudMapper.class);
        builder.setKeywordMode(WordMode.lowerCase);
        builder.setSqlMode(WordMode.lowerCase);
        builder.setSchemaMode(TableNameMode.auto);
        builder.setPrefixMode(TableNameMode.auto);
        MappedStatement ms = builder.build();
        config.addMappedStatement(ms);
        SqlSession sqlSession = new DefaultSqlSession(config, executor, false);
        {
            int count = sqlSession.selectOne(Constants.COUNT_ALL);
            Assert.assertEquals(2, count);
        }
    }
}