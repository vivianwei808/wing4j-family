package org.wing4j.orm.mybatis.markdown.wing4j.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class Wing4jParamUtilsTest {

    @Test
    public void testIfParam() throws Exception {
        Assert.assertEquals(true, Wing4jParamUtils.ifParam("--@name=wing4j"));
        Assert.assertEquals(true, Wing4jParamUtils.ifParam("--@name =wing4j"));
        Assert.assertEquals(true, Wing4jParamUtils.ifParam("--@name= wing4j"));
        Assert.assertEquals(true, Wing4jParamUtils.ifParam("--@name = wing4j"));
        Assert.assertEquals(true, Wing4jParamUtils.ifParam("--@name = wing4j//参数"));
    }

    @Test
    public void testTrimParam() throws Exception {
        Wing4jParamUtils.ParamValue paramValue1 = Wing4jParamUtils.trimParam("--@name=wing4j");
        Assert.assertEquals("name", paramValue1.getName());
        Assert.assertEquals("wing4j", paramValue1.getValue());
        Wing4jParamUtils.ParamValue paramValue2 = Wing4jParamUtils.trimParam("--@name =wing4j");
        Assert.assertEquals("name", paramValue2.getName());
        Assert.assertEquals("wing4j", paramValue2.getValue());
        Wing4jParamUtils.ParamValue paramValue3 = Wing4jParamUtils.trimParam("--@name= wing4j");
        Assert.assertEquals("name", paramValue3.getName());
        Assert.assertEquals("wing4j", paramValue3.getValue());
        Wing4jParamUtils.ParamValue paramValue4 = Wing4jParamUtils.trimParam("--@name = wing4j");
        Assert.assertEquals("name", paramValue4.getName());
        Assert.assertEquals("wing4j", paramValue4.getValue());
        Wing4jParamUtils.ParamValue paramValue5 = Wing4jParamUtils.trimParam("--@name = wing4j//参数");
        Assert.assertEquals("name", paramValue5.getName());
        Assert.assertEquals("wing4j", paramValue5.getValue());
    }

    @Test
    public void testIfComment() throws Exception {
        Assert.assertEquals(false, Wing4jParamUtils.ifComment("--@name=wing4j"));
        Assert.assertEquals(false, Wing4jParamUtils.ifComment("--@name =wing4j"));
        Assert.assertEquals(false, Wing4jParamUtils.ifComment("--@name= wing4j"));
        Assert.assertEquals(false, Wing4jParamUtils.ifComment("--@name = wing4j"));
        Assert.assertEquals(true, Wing4jParamUtils.ifComment("--@name = wing4j//参数"));
        Assert.assertEquals(true, Wing4jParamUtils.ifComment("--@name = wing4j// 参数"));
        Assert.assertEquals(true, Wing4jParamUtils.ifComment("--wing4j参数"));
        Assert.assertEquals(true, Wing4jParamUtils.ifComment("-- wing4j参数"));
    }

    @Test
    public void testTrimComment() throws Exception {
        Assert.assertEquals("", Wing4jParamUtils.trimComment("--@name=wing4j"));
        Assert.assertEquals("", Wing4jParamUtils.trimComment("--@name =wing4j"));
        Assert.assertEquals("", Wing4jParamUtils.trimComment("--@name= wing4j"));
        Assert.assertEquals("", Wing4jParamUtils.trimComment("--@name = wing4j"));
        Assert.assertEquals("参数", Wing4jParamUtils.trimComment("--@name = wing4j//参数"));
        Assert.assertEquals(" 参数", Wing4jParamUtils.trimComment("--@name = wing4j// 参数"));
        Assert.assertEquals("wing4j参数", Wing4jParamUtils.trimComment("--wing4j参数"));
        Assert.assertEquals(" wing4j参数", Wing4jParamUtils.trimComment("-- wing4j参数"));
    }
}