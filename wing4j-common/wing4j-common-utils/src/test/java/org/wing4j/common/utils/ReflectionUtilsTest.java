package org.wing4j.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Created by wing4j on 2016/12/17.
 */
public class ReflectionUtilsTest {

    @Test
    public void testGetFields() throws Exception {
        List<Field> fields = ReflectionUtils.getFields(DemoJavaBean3.class);
        Assert.assertEquals(3, fields.size());
        Assert.assertEquals("field3", fields.get(0).getName());
        Assert.assertEquals("field2", fields.get(1).getName());
        Assert.assertEquals("field1", fields.get(2).getName());
    }

    @Test
    public void testGetFieldsExcludeTransient() throws Exception {
        List<Field> fields = ReflectionUtils.getFieldsExcludeTransient(DemoJavaBean3.class);
        Assert.assertEquals(2, fields.size());
        Assert.assertEquals("field3", fields.get(0).getName());
        Assert.assertEquals("field1", fields.get(1).getName());
    }
}