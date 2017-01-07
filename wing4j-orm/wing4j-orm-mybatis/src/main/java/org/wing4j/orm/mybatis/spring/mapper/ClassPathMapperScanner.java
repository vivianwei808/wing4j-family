package org.wing4j.orm.mybatis.spring.mapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;
import org.wing4j.orm.mybatis.spring.SqlSessionTemplate;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    /**
     * SQL 语句大小写模式
     */
    String sqlMode;
    /**
     * 关键词大小写模式
     */
    String keywordMode;
    /**
     * 数据库类型
     */
    String databaseType;
    /**
     * 严格wing4j注解模式
     */
    boolean strictWing4j;

    private boolean addToConfig = true;

    private SqlSessionFactory sqlSessionFactory;

    private SqlSessionTemplate sqlSessionTemplate;

    private String sqlSessionTemplateBeanName;

    private String sqlSessionFactoryBeanName;

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    private MapperFactoryBean mapperFactoryBean = new MapperFactoryBean();

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateBeanName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateBeanName;
    }

    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }

    public void setMapperFactoryBean(MapperFactoryBean mapperFactoryBean) {
        this.mapperFactoryBean = (mapperFactoryBean != null ? mapperFactoryBean : new MapperFactoryBean());
    }


    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
            addIncludeFilter(new TypeFilter() {
                @Override
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    return true;
                }
            });
        }

        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
    }

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as
     * MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is MapperFactoryBean
            definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
            definition.setBeanClass(this.mapperFactoryBean.getClass());

            definition.getPropertyValues().add("addToConfig", this.addToConfig);
            definition.getPropertyValues().add("databaseType", this.databaseType);
            definition.getPropertyValues().add("sqlMode", this.sqlMode);
            definition.getPropertyValues().add("keywordMode", this.keywordMode);
            definition.getPropertyValues().add("strictWing4j", this.strictWing4j);

            boolean explicitFactoryUsed = false;
            if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
                definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
                explicitFactoryUsed = true;
            } else if (this.sqlSessionFactory != null) {
                definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
                explicitFactoryUsed = true;
            }

            if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
                if (explicitFactoryUsed) {
                    logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
                }
                definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
                explicitFactoryUsed = true;
            } else if (this.sqlSessionTemplate != null) {
                if (explicitFactoryUsed) {
                    logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
                }
                definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
                explicitFactoryUsed = true;
            }

            if (!explicitFactoryUsed) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
                }
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            }
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.warn("Skipping MapperFactoryBean with name '" + beanName
                    + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }

    public void setSqlMode(String sqlMode) {
        this.sqlMode = sqlMode;
    }

    public void setKeywordMode(String keywordMode) {
        this.keywordMode = keywordMode;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public void setStrictWing4j(boolean strictWing4j) {
        this.strictWing4j = strictWing4j;
    }
}
