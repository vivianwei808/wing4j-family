package org.wing4j.common.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/1.
 */
public class StringUtilsTest {

    @Test
    public void testCamelToUnderline() throws Exception {
        String str_new = StringUtils.camelToUnderline("newStrTest");
        Assert.assertEquals("NEW_STR_TEST", str_new);
    }

    @Test
    public void testFirstCharToUpper() throws Exception {
        {
            String str_new = StringUtils.firstCharToUpper("newStrTest");
            Assert.assertEquals("NewStrTest", str_new);
        }
        {
            String str_new = StringUtils.firstCharToUpper("NewStrTest");
            Assert.assertEquals("NewStrTest", str_new);
        }
    }

    @Test
    public void testFirstCharToLower() throws Exception {
        {
            String str_new = StringUtils.firstCharToLower("NewStrTest");
            Assert.assertEquals("newStrTest", str_new);
        }
        {
            String str_new = StringUtils.firstCharToLower("newStrTest");
            Assert.assertEquals("newStrTest", str_new);
        }
    }

    @Test
    public void testUnderlineToCamel() throws Exception {
        String str_new = StringUtils.underlineToCamel("NEW_STR_TEST");
        Assert.assertEquals("newStrTest", str_new);
    }

    @Test
    public void testFill() throws Exception {
        {
            String str_new = StringUtils.fill("1", false, '0', 6);
            Assert.assertEquals("000001", str_new);
        }
        {
            String str_new = StringUtils.fill("1", true, '0', 6);
            Assert.assertEquals("100000", str_new);
        }
        {
            String str_new = StringUtils.fill("1", false, 'A', 6);
            Assert.assertEquals("AAAAA1", str_new);
        }
        {
            String str_new = StringUtils.fill("1", true, 'A', 6);
            Assert.assertEquals("1AAAAA", str_new);
        }
    }

    @Test
    public void testSafeToString() throws Exception {
        {
            String str_old = null;
            String str_new = StringUtils.safeToString(str_old, "null");
            Assert.assertEquals("null", str_new);
        }
        {
            String str_old = "nil";
            String str_new = StringUtils.safeToString(str_old, "null");
            Assert.assertEquals("nil", str_new);
        }
    }

    @Test
    public void testSafeToString1() throws Exception {
        {
            String str_old = null;
            String str_new = StringUtils.safeToString(str_old);
            Assert.assertEquals(null, str_new);
        }

        {
            String str_old = "nil";
            String str_new = StringUtils.safeToString(str_old);
            Assert.assertEquals("nil", str_new);
        }
    }
}