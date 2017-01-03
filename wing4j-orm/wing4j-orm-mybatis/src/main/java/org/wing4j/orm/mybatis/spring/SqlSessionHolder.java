package org.wing4j.orm.mybatis.spring;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.transaction.support.ResourceHolderSupport;

import static org.springframework.util.Assert.notNull;

/**
 * MyBatis session Holder
 */
public final class SqlSessionHolder extends ResourceHolderSupport {

    private final SqlSession sqlSession;

    private final ExecutorType executorType;

    private final PersistenceExceptionTranslator exceptionTranslator;

    public SqlSessionHolder(SqlSession sqlSession,
                            ExecutorType executorType,
                            PersistenceExceptionTranslator exceptionTranslator) {

        notNull(sqlSession, "SqlSession must not be null");
        notNull(executorType, "ExecutorType must not be null");

        this.sqlSession = sqlSession;
        this.executorType = executorType;
        this.exceptionTranslator = exceptionTranslator;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
        return exceptionTranslator;
    }

}
