package org.wing4j.orm.mybatis.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Mapper框架关于Spring标签的解析
 */
public class MyBatisNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("scan", new MyBatisMapperScannerBeanDefinitionParser());
    }

}
