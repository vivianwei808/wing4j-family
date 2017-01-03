package org.wing4j.demo.dao;

import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.wing4j.demo.entity.DemoInfoEntity;
import org.wing4j.orm.WordMode;
import org.wing4j.test.CreateTable;
import org.wing4j.test.DbBaseTest;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/1.
 */
@ContextConfiguration(locations = {"classpath*:testContext-orm.xml"})
public class DemoInfoDAOTest extends DbBaseTest {
    @Test
    @CreateTable(entities = {DemoInfoEntity.class}, keywordMode = WordMode.lowerCase, sqlMode = WordMode.lowerCase)
    public void test1(){
        DemoInfoEntity entity = new DemoInfoEntity();
        entity.setSerialNo(UUID.randomUUID().toString());
        entity.setAge(20);
        entity.setCreateDate(new Date());
        entity.setLastUpdateDate(new Date());

        DemoInfoDAO demoInfoDAO = getBean(DemoInfoDAO.class);
        demoInfoDAO.insertSelective(entity);
    }
}