package org.wing4j.orm.entity.utils;

import org.junit.Assert;
import org.junit.Test;
import org.wing4j.orm.count.CountMapper;
import org.wing4j.orm.entity.utils.wing4j.NormalWing4jDemoEntity;
import org.wing4j.orm.select.SelectMapper;

/**
 * Created by wing4j on 2016/12/18.
 */
public class GenericityExtracteUtilsTest {

    @Test
    public void testExtractEntityClass() throws Exception {
        Class extractEntityClass1 = GenericityExtracteUtils.extractEntityClass(DemoMapper.class, CountMapper.class);
        Assert.assertEquals(NormalWing4jDemoEntity.class, extractEntityClass1);
        Class extractEntityClass2 = GenericityExtracteUtils.extractEntityClass(DemoMapper.class, SelectMapper.class);
        Assert.assertEquals(NormalWing4jDemoEntity.class, extractEntityClass2);
    }

    @Test
    public void testExtractKeyClass() throws Exception {
        Class extractKeyClass1 = GenericityExtracteUtils.extractKeyClass(DemoMapper.class, CountMapper.class);
        Assert.assertEquals(String.class, extractKeyClass1);
        Class extractKeyClass2 = GenericityExtracteUtils.extractKeyClass(DemoMapper.class, SelectMapper.class);
        Assert.assertEquals(String.class, extractKeyClass2);
    }
}