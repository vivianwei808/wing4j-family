package org.wing4j.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.wing4j.orm.WordMode;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2016/12/31.
 */
@ContextConfiguration(locations = {"classpath*:testContext-orm.xml"})
public class DbBaseTestTest extends DbBaseTest {
    @CreateTable(entities = DemoEntity.class, sqlMode = WordMode.upperCase, keywordMode = WordMode.upperCase)
    @Test
    public void testInsert(){
        DemoCrudMapper mapper = getBean(DemoCrudMapper.class);
        List<DemoEntity> list = mapper.selectAll();
        System.out.println(list);
    }
}