package org.wing4j.orm.mybatis.spring.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.wing4j.common.utils.DateStyle;
import org.wing4j.common.utils.DateUtils;
import org.wing4j.orm.mybatis.mapper.builder.BaseTest;
import org.wing4j.test.CreateTable;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.UUID;


@ContextConfiguration(locations = {"classpath*:testContext-orm-jpa.xml"})
public class NormalJPADemoDaoTest extends BaseTest {
    @Autowired
    DataSource dataSource;

    @CreateTable(entities = {NormalJPADemoEntity.class})
    @Test
    public void testJpa(){
        NormalJPADemoDao dao = getBean(NormalJPADemoDao.class);
        NormalJPADemoEntity entity = new NormalJPADemoEntity();
        entity.setSerialNo(UUID.randomUUID().toString());
        entity.setCol1("col1");
        entity.setCol2(new BigDecimal("1234.345"));
        entity.setCol3(3);
        entity.setCol4(4);
        java.util.Date testDate = DateUtils.toDate("2017/01/02 03:04:05");
        entity.setCol5(testDate);
        entity.setCol6(new java.sql.Date(DateUtils.getNextDay(testDate, 1).getTime()));
        entity.setCol7(new java.sql.Time(DateUtils.getNextDay(testDate, 2).getTime()));
        entity.setCol8(new java.sql.Timestamp(DateUtils.getNextDay(testDate, 3).getTime()));
        entity.setCol9(true);
        entity.setCol10(true);
        dao.insert(entity);
        NormalJPADemoEntity selectEntity = dao.selectByPrimaryKey(entity.getSerialNo());
        Assert.assertEquals(entity.getSerialNo(), selectEntity.getSerialNo());
        Assert.assertEquals(entity.getCol1(), selectEntity.getCol1());
        Assert.assertEquals(entity.getCol2().setScale(3).toPlainString(), selectEntity.getCol2().setScale(3).toPlainString());
        Assert.assertEquals(entity.getCol3(), selectEntity.getCol3());
        Assert.assertEquals(entity.getCol4(), selectEntity.getCol4());
        //有年月日时分秒
        Assert.assertEquals(DateUtils.toFullString(entity.getCol5()), DateUtils.toFullString(selectEntity.getCol5()));
        //只有年月日
        Assert.assertEquals(DateUtils.toString(entity.getCol6(), DateStyle.DAY_FORMAT1), DateUtils.toString(selectEntity.getCol6(), DateStyle.DAY_FORMAT1));
        //只有时分秒
        Assert.assertEquals(DateUtils.toString(entity.getCol7(), DateStyle.MILLI_FORMAT2), DateUtils.toString(selectEntity.getCol7(),  DateStyle.MILLI_FORMAT2));
        //有年月日时分秒
        Assert.assertEquals(DateUtils.toFullString(entity.getCol8()), DateUtils.toFullString(selectEntity.getCol8()));
        Assert.assertEquals(entity.isCol9(), selectEntity.isCol9());
        Assert.assertEquals(entity.getCol10().booleanValue(), selectEntity.getCol10().booleanValue());
    }
}
