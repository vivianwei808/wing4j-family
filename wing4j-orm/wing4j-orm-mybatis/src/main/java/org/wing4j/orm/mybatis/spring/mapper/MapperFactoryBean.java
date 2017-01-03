package org.wing4j.orm.mybatis.spring.mapper;

import lombok.Setter;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.wing4j.orm.DatabaseType;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.mybatis.plugins.PaginationStage1Interceptor;
import org.wing4j.orm.mybatis.plugins.PaginationStage2Interceptor;
import org.wing4j.orm.mybatis.spring.SqlSessionDaoSupport;

import static org.springframework.util.Assert.notNull;

public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {
    @Setter
    Class<T> mapperInterface;
    @Setter
    boolean addToConfig = true;
    /**
     * SQL 语句大小写模式
     */
    @Setter
    String sqlMode = WordMode.lowerCase.name();
    /**
     * 关键词大小写模式
     */
    @Setter
    String keywordMode = WordMode.lowerCase.name();
    /**
     * 数据库类型
     */
    @Setter
    String databaseType = DatabaseType.MySQL.name();

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public MapperFactoryBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDaoConfig() {
        notNull(this.sqlMode, "Property 'sqlMode' is required");
        notNull(this.sqlMode, "Property 'keywordMode' is required");
        notNull(this.databaseType, "Property 'databaseType' is required");
        WordMode sqlMode0 = null;
        WordMode keywordMode0 = null;
        DatabaseType databaseType0 = null;
        if ((sqlMode0 = WordMode.valueOf(sqlMode)) == null) {
            throw new IllegalArgumentException("Property 'sqlMode' must is lowerCase or upperCase ");
        }
        if ((keywordMode0 = WordMode.valueOf(keywordMode)) == null) {
            throw new IllegalArgumentException("Property 'keywordMode' must is lowerCase or upperCase ");
        }
        if ((databaseType0 = DatabaseType.valueOf(databaseType)) == null) {
            throw new IllegalArgumentException("Property 'databaseType' must is lowerCase or upperCase ");
        }
        super.checkDaoConfig();
        notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = getSqlSession().getConfiguration();
        configuration.addInterceptor(new PaginationStage1Interceptor(databaseType0));
        configuration.addInterceptor(new PaginationStage2Interceptor());

        MappedStatementRegister.scan(configuration, this.mapperInterface, sqlMode0, keywordMode0);
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
    }

    @Override
    public T getObject() throws Exception {
        return getSqlSession().getMapper(this.mapperInterface);
    }

    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

}
