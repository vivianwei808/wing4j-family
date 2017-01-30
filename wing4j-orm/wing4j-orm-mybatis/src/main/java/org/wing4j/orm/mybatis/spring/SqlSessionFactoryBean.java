package org.wing4j.orm.mybatis.spring;

import lombok.Setter;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.wing4j.orm.mybatis.spring.transaction.SpringManagedTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {

    private static final Log LOGGER = LogFactory.getLog(SqlSessionFactoryBean.class);
    @Setter
    private Resource configLocation;
    /**
     * XML格式的Mapper文件
     */
    @Setter
    private Resource[] xmlMapperLocations;
    /**
     * Markdown格式的Mapper文件
     */
    @Setter
    private Resource[] markdownMapperLocations;

    private DataSource dataSource;

    private TransactionFactory transactionFactory;
    @Setter
    private Properties configurationProperties;

    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    private SqlSessionFactory sqlSessionFactory;

    //EnvironmentAware requires spring 3.1
    private String environment = SqlSessionFactoryBean.class.getSimpleName();
    @Setter
    private boolean failFast;
    @Setter
    private Interceptor[] plugins;
    @Setter
    private TypeHandler<?>[] typeHandlers;
    @Setter
    private String typeHandlersPackage;
    @Setter
    private Class<?>[] typeAliases;
    @Setter
    private String typeAliasesPackage;
    @Setter
    private Class<?> typeAliasesSuperType;

    //issue #19. No default provider.
    @Setter
    private DatabaseIdProvider databaseIdProvider;
    @Setter
    private ObjectFactory objectFactory;
    @Setter
    private ObjectWrapperFactory objectWrapperFactory;


    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            // If we got a TransactionAwareDataSourceProxy, we need to perform
            // transactions for its underlying target DataSource, else data
            // access code won't see properly exposed transactions (i.e.
            // transactions for the target DataSource).
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(dataSource, "Property 'dataSource' is required");
        notNull(sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");

        this.sqlSessionFactory = buildSqlSessionFactory();
    }


    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {

        Configuration configuration;

        XMLConfigBuilder xmlConfigBuilder = null;
        if (this.configLocation != null) {
            xmlConfigBuilder = new XMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
            configuration = xmlConfigBuilder.getConfiguration();
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Property 'configLocation' not specified, using default MyBatis Configuration");
            }
            configuration = new Configuration();
            configuration.setVariables(this.configurationProperties);
        }

        if (this.objectFactory != null) {
            configuration.setObjectFactory(this.objectFactory);
        }

        if (this.objectWrapperFactory != null) {
            configuration.setObjectWrapperFactory(this.objectWrapperFactory);
        }

        if (hasLength(this.typeAliasesPackage)) {
            String[] typeAliasPackageArray = tokenizeToStringArray(this.typeAliasesPackage,
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String packageToScan : typeAliasPackageArray) {
                configuration.getTypeAliasRegistry().registerAliases(packageToScan,
                        typeAliasesSuperType == null ? Object.class : typeAliasesSuperType);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Scanned package: '" + packageToScan + "' for aliases");
                }
            }
        }

        if (!isEmpty(this.typeAliases)) {
            for (Class<?> typeAlias : this.typeAliases) {
                configuration.getTypeAliasRegistry().registerAlias(typeAlias);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registered type alias: '" + typeAlias + "'");
                }
            }
        }

        if (!isEmpty(this.plugins)) {
            for (Interceptor plugin : this.plugins) {
                configuration.addInterceptor(plugin);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registered plugin: '" + plugin + "'");
                }
            }
        }

        if (hasLength(this.typeHandlersPackage)) {
            String[] typeHandlersPackageArray = tokenizeToStringArray(this.typeHandlersPackage,
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String packageToScan : typeHandlersPackageArray) {
                configuration.getTypeHandlerRegistry().register(packageToScan);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Scanned package: '" + packageToScan + "' for type handlers");
                }
            }
        }

        if (!isEmpty(this.typeHandlers)) {
            for (TypeHandler<?> typeHandler : this.typeHandlers) {
                configuration.getTypeHandlerRegistry().register(typeHandler);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registered type handler: '" + typeHandler + "'");
                }
            }
        }

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Parsed configuration file: '" + this.configLocation + "'");
                }
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
            } finally {
                ErrorContext.instance().reset();
            }
        }

        if (this.transactionFactory == null) {
            this.transactionFactory = new SpringManagedTransactionFactory();
        }

        configuration.setEnvironment(new Environment(this.environment, this.transactionFactory, this.dataSource));

        if (this.databaseIdProvider != null) {
            try {
                configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            } catch (SQLException e) {
                throw new NestedIOException("Failed getting a databaseId", e);
            }
        }
        //Xml格式的Mapper
        if (!isEmpty(this.xmlMapperLocations)) {
            for (Resource xmlMapperLocation : this.xmlMapperLocations) {
                if (xmlMapperLocation == null) {
                    continue;
                }

                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(xmlMapperLocation.getInputStream(),
                            configuration,
                            xmlMapperLocation.toString(),
                            configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (Exception e) {
                    throw new NestedIOException("Failed to parse mapping resource: '" + xmlMapperLocation + "'", e);
                } finally {
                    ErrorContext.instance().reset();
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Parsed mapper file: '" + xmlMapperLocation + "'");
                }
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Property 'xmlMapperLocations' was not specified or no matching resources found");
            }
        }

        //Markdown格式的Mapper
        if (!isEmpty(this.markdownMapperLocations)) {
            for (Resource markdownMapperLocation : this.markdownMapperLocations) {
                if (markdownMapperLocation == null) {
                    continue;
                }
                //TODO  扫描Markdown文件所在目录

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Parsed mapper file: '" + markdownMapperLocation + "'");
                }
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Property 'markdownMapperLocations' was not specified or no matching resources found");
            }
        }
        return this.sqlSessionFactoryBuilder.build(configuration);
    }

    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }

        return this.sqlSessionFactory;
    }


    @Override
    public Class<? extends SqlSessionFactory> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (failFast && event instanceof ContextRefreshedEvent) {
            // fail-fast -> check all statements are completed
            this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
        }
    }

}
