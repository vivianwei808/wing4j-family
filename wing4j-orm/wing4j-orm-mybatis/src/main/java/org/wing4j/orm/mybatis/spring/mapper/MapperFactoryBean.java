package org.wing4j.orm.mybatis.spring.mapper;

import lombok.Setter;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.wing4j.orm.DatabaseType;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.mybatis.plugins.PaginationStage1Interceptor;
import org.wing4j.orm.mybatis.plugins.PaginationStage2Interceptor;
import org.wing4j.orm.mybatis.sequnece.SequenceServiceConfigure;
import org.wing4j.orm.mybatis.spring.SqlSessionDaoSupport;
import org.wing4j.test.TableNameMode;

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
    @Setter
    String schemaMode = TableNameMode.auto.name();
    @Setter
    String schema;
    @Setter
    String prefixMode = TableNameMode.auto.name();
    @Setter
    String prefix;
    @Setter
    String suffixMode = TableNameMode.auto.name();
    @Setter
    String suffix;
    /**
     * 严格wing4j注解模式
     */
    @Setter
    boolean strictWing4j;
    /**
     * 序号配置
     */
    @Setter
    @Autowired(required = false)
    SequenceServiceConfigure sequenceConfigure;
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
        TableNameMode schemaMode0 = null;
        TableNameMode prefixMode0 = null;
        TableNameMode suffixMode0 = null;
        DatabaseType databaseType0 = null;
        if ((sqlMode0 = WordMode.valueOf(sqlMode)) == null) {
            throw new IllegalArgumentException("Property 'sqlMode' must is lowerCase or upperCase ");
        }
        if ((keywordMode0 = WordMode.valueOf(keywordMode)) == null) {
            throw new IllegalArgumentException("Property 'keywordMode' must is lowerCase or upperCase ");
        }
        if ((schemaMode0 = TableNameMode.valueOf(schemaMode)) == null) {
            throw new IllegalArgumentException("Property 'schemaMode' must is auto 、entity or createTable");
        }
        if ((prefixMode0 = TableNameMode.valueOf(prefixMode)) == null) {
            throw new IllegalArgumentException("Property 'prefixMode' must is auto 、entity or createTable");
        }
        if ((suffixMode0 = TableNameMode.valueOf(suffixMode)) == null) {
            throw new IllegalArgumentException("Property 'suffixMode' must is auto 、entity or createTable");
        }
        if ((databaseType0 = DatabaseType.valueOf(databaseType)) == null) {
            throw new IllegalArgumentException("Property 'databaseType' must is lowerCase or upperCase ");
        }
        super.checkDaoConfig();
        notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = getSqlSession().getConfiguration();
        configuration.addInterceptor(new PaginationStage1Interceptor(databaseType0));
        configuration.addInterceptor(new PaginationStage2Interceptor());

        MappedStatementRegister.scan(configuration, this.mapperInterface, sqlMode0, keywordMode0, schemaMode0, schema, prefixMode0, prefix, suffixMode0, suffix, strictWing4j, sequenceConfigure);
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
