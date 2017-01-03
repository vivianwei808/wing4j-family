package org.wing4j.orm.mybatis.spring.config;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.wing4j.orm.mybatis.spring.mapper.ClassPathMapperScanner;

import java.lang.annotation.Annotation;

public class MyBatisMapperScannerBeanDefinitionParser implements BeanDefinitionParser {
    /**
     * 定义用于对要扫描的Dao所在包路径，支持多个包路径,用逗号分割或者分号分割
     */
    static String ATTRIBUTE_BASE_PACKAGE = "basePackage";
    static String ATTRIBUTE_ANNOTATION = "annotation";
    static String ATTRIBUTE_MARKER_INTERFACE = "marker-interface";
    static String ATTRIBUTE_NAME_GENERATOR = "name-generator";
    static String ATTRIBUTE_TEMPLATE_REF = "template-ref";
    static String ATTRIBUTE_FACTORY_REF = "sqlSessionFactoryBeanName";
    static String ATTRIBUTE_SQL_MODE = "sqlMode";
    static String ATTRIBUTE_KEYWORD_MODE = "keywordMode";
    static String ATTRIBUTE_DATABASE_TYPE = "databaseType";

    @Override
    public synchronized BeanDefinition parse(Element element, ParserContext parserContext) {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(parserContext.getRegistry());
        ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
        XmlReaderContext readerContext = parserContext.getReaderContext();
        scanner.setResourceLoader(readerContext.getResourceLoader());
        try {
            String annotationClassName = element.getAttribute(ATTRIBUTE_ANNOTATION);
            if (StringUtils.hasText(annotationClassName)) {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation> markerInterface = (Class<? extends Annotation>) classLoader.loadClass(annotationClassName);
                scanner.setAnnotationClass(markerInterface);
            }
            String markerInterfaceClassName = element.getAttribute(ATTRIBUTE_MARKER_INTERFACE);
            if (StringUtils.hasText(markerInterfaceClassName)) {
                Class<?> markerInterface = classLoader.loadClass(markerInterfaceClassName);
                scanner.setMarkerInterface(markerInterface);
            }
            String nameGeneratorClassName = element.getAttribute(ATTRIBUTE_NAME_GENERATOR);
            if (StringUtils.hasText(nameGeneratorClassName)) {
                Class<?> nameGeneratorClass = classLoader.loadClass(nameGeneratorClassName);
                BeanNameGenerator nameGenerator = BeanUtils.instantiateClass(nameGeneratorClass, BeanNameGenerator.class);
                scanner.setBeanNameGenerator(nameGenerator);
            }
        } catch (Exception ex) {
            readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
        }
        String sqlSessionTemplateBeanName = element.getAttribute(ATTRIBUTE_TEMPLATE_REF);
        scanner.setSqlSessionTemplateBeanName(sqlSessionTemplateBeanName);
        String sqlSessionFactoryBeanName = element.getAttribute(ATTRIBUTE_FACTORY_REF);
        scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        String basePackage = element.getAttribute(ATTRIBUTE_BASE_PACKAGE);
        String sqlMode = element.getAttribute(ATTRIBUTE_SQL_MODE);
        scanner.setSqlMode(sqlMode);
        String keywordMode = element.getAttribute(ATTRIBUTE_KEYWORD_MODE);
        scanner.setKeywordMode(keywordMode);
        String databaseType = element.getAttribute(ATTRIBUTE_DATABASE_TYPE);
        scanner.setDatabaseType(databaseType);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        return null;
    }

}
