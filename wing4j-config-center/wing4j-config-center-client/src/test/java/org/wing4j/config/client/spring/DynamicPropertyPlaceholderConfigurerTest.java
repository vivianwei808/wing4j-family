package org.wing4j.config.client.spring;

import org.wing4j.config.client.config.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by woate on 2017/2/10.
 * 20:13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:DynamicPropertyPlaceholderConfigurerTest.xml")
public class DynamicPropertyPlaceholderConfigurerTest {
    @Autowired
    DynamicPropertyPlaceholderConfigurer configurer;
    @Autowired
    Config config;

    @Test
    public void testProcProperties() throws Exception {
        config.ssssss();
        while (true){
            System.out.println(config.getParam1());
            System.out.println(config.getParam2());
            Thread.sleep(500);
        }
    }
}