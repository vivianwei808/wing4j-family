package org.wing4j.orm.mybatis.spring.mapper;

import static org.springframework.util.Assert.notNull;

import java.lang.annotation.Annotation;
import java.util.Map;

import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;
import org.wing4j.orm.DatabaseType;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.mybatis.spring.SqlSessionTemplate;

public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
    @Setter
    private String basePackage;
    @Setter
    private boolean addToConfig = true;
    @Setter
    private SqlSessionFactory sqlSessionFactory;
    @Setter
    private String sqlSessionFactoryBeanName;
    @Setter
    private String sqlSessionTemplateBeanName;
    @Setter
    private Class<? extends Annotation> annotationClass;
    @Setter
    private Class<?> markerInterface;

    private ApplicationContext applicationContext;

    private String beanName;

    private boolean processPropertyPlaceHolders;

    private BeanNameGenerator nameGenerator;

    private MapperFactoryBean mapperFactoryBean;

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

    /**
     * 严格wing4j注解模式
     */
    @Setter
    boolean strictWing4j;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }


    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }


    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setMarkerInterface(Class<?> superClass) {
        this.markerInterface = superClass;
    }

    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
    }


    @Deprecated
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
    }

    public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
        this.processPropertyPlaceHolders = processPropertyPlaceHolders;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public BeanNameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public void setNameGenerator(BeanNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        if (this.processPropertyPlaceHolders) {
            processPropertyPlaceHolders();
        }
        if(mapperFactoryBean == null){
            this.mapperFactoryBean = new MapperFactoryBean();
        }
//        this.mapperFactoryBean.setSqlMode(sqlMode);
//        this.mapperFactoryBean.setKeywordMode(keywordMode);
//        this.mapperFactoryBean.setDatabaseType(databaseType);

        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.setAddToConfig(this.addToConfig);
        scanner.setAnnotationClass(this.annotationClass);
        scanner.setMarkerInterface(this.markerInterface);
        scanner.setSqlSessionFactory(this.sqlSessionFactory);
        scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
        scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setBeanNameGenerator(this.nameGenerator);
        scanner.setMapperFactoryBean(this.mapperFactoryBean);
        scanner.setSqlMode(this.sqlMode);
        scanner.setKeywordMode(this.keywordMode);
        scanner.setDatabaseType(this.databaseType);
        scanner.setStrictWing4j(this.strictWing4j);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    private void processPropertyPlaceHolders() {
        Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

        if (!prcs.isEmpty() && applicationContext instanceof GenericApplicationContext) {
            BeanDefinition mapperScannerBean = ((GenericApplicationContext) applicationContext)
                    .getBeanFactory().getBeanDefinition(beanName);

            // PropertyResourceConfigurer does not expose any methods to explicitly perform
            // property placeholder substitution. Instead, create a BeanFactory that just
            // contains this mapper scanner and post process the factory.
            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(beanName, mapperScannerBean);

            for (PropertyResourceConfigurer prc : prcs.values()) {
                prc.postProcessBeanFactory(factory);
            }

            PropertyValues values = mapperScannerBean.getPropertyValues();

            this.basePackage = updatePropertyValue("basePackage", values);
            this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", values);
            this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", values);
        }
    }

    private String updatePropertyValue(String propertyName, PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else {
            return null;
        }
    }
}