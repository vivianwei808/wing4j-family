package org.wing4j.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.wing4j.orm.WordMode;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
        Assert.assertEquals(0, list.size());
        System.out.println(list);
        String serialNo = UUID.randomUUID().toString();
        {
            DemoEntity demoEntity = new DemoEntity();
            demoEntity.setSerialNo(serialNo);
            demoEntity.setCol1("col1");
            demoEntity.setCol2(new BigDecimal("1"));
            demoEntity.setCol3(1);
            mapper.insertSelective(demoEntity);
        }
        {
            DemoEntity demoEntity = mapper.selectByPrimaryKey(serialNo);
            Assert.assertEquals(serialNo, demoEntity.getSerialNo());
            Assert.assertEquals("col1", demoEntity.getCol1());
            Assert.assertEquals(1, demoEntity.getCol2().intValue());
            Assert.assertEquals(1, demoEntity.getCol3().intValue());
        }
    }
}