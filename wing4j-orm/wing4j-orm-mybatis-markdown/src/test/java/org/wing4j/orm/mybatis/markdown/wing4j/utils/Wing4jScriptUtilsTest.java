package org.wing4j.orm.mybatis.markdown.wing4j.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class Wing4jScriptUtilsTest {

    @Test
    public void testIfScript() throws Exception {
        Assert.assertEquals(true, Wing4jScriptUtils.ifScript("/*#     if col2 == null                  */"));
    }

    @Test
    public void testTrimScript() throws Exception {
        Assert.assertEquals("if col2 == null", Wing4jScriptUtils.trimScript("/*#if col2 == null*/"));
        Assert.assertEquals("if col2 == null", Wing4jScriptUtils.trimScript("/*#     if col2 == null*/"));
        Assert.assertEquals("if col2 == null", Wing4jScriptUtils.trimScript("/*#if col2 == null                 */"));
        Assert.assertEquals("if col2 == null", Wing4jScriptUtils.trimScript("/*#     if col2 == null                 */"));
    }
}