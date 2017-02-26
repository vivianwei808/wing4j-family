package org.wing4j.common.utils;

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/9/24.
 */
public class DateUtilTest {
    static Map<String, DateStyle> TEST_CASE = new HashMap<String, DateStyle>();

    @BeforeClass
    public static void beforeClass() {
        TEST_CASE.clear();
        TEST_CASE.put("2015-01-01 10:10:10,123", DateStyle.MILLI_FORMAT1);
        TEST_CASE.put("2015-01-01 10:10:10,012", DateStyle.MILLI_FORMAT1);
        TEST_CASE.put("2015-01-01 10:10:10,001", DateStyle.MILLI_FORMAT1);
        TEST_CASE.put("2015-01-01 10:10:10", DateStyle.SECOND_FORMAT1);
        TEST_CASE.put("2015/01/01 10:10:10", DateStyle.SECOND_FORMAT2);
        TEST_CASE.put("2015/12/31 10:10:10", DateStyle.SECOND_FORMAT2);
        TEST_CASE.put("2015-01-01 10:10", DateStyle.MINUTE_FORMAT1);
        TEST_CASE.put("2015-12-31 10:10", DateStyle.MINUTE_FORMAT1);
        TEST_CASE.put("2015/01/01 10:10", DateStyle.MINUTE_FORMAT2);
        TEST_CASE.put("2015/12/31 10:10", DateStyle.MINUTE_FORMAT2);
        TEST_CASE.put("2015-01-01_10-10-10", DateStyle.FILE_FORMAT1);
        TEST_CASE.put("20150101101010123", DateStyle.FILE_FORMAT2);
        TEST_CASE.put("20150101101010", DateStyle.FILE_FORMAT3);
        TEST_CASE.put("201501011010", DateStyle.FILE_FORMAT4);
        TEST_CASE.put("2015010110", DateStyle.FILE_FORMAT5);
        TEST_CASE.put("20150101", DateStyle.FILE_FORMAT6);
        TEST_CASE.put("101010123", DateStyle.FILE_FORMAT7);
        TEST_CASE.put("201501", DateStyle.FILE_FORMAT8);
        TEST_CASE.put("2015年01月01日 13时13分13秒", DateStyle.CHINESE_FORMAT1);
        TEST_CASE.put("2015年01月01日", DateStyle.CHINESE_FORMAT2);
        TEST_CASE.put("13时13分13秒", DateStyle.CHINESE_FORMAT3);
    }

    /**
     * 测试根据输入的字符串自动获取格式字符串
     */
    @Test
    public void testGetPattern1() {
        Set<String> keySet = TEST_CASE.keySet();
        for (String s : keySet) {
            DateStyle pattern1 = DateUtils.getPattern(s);
            Assert.assertEquals(TEST_CASE.get(s), pattern1);
            System.out.println(s);
        }
    }

    @Test
    public void testToDate1() {
        Set<String> keySet = TEST_CASE.keySet();
        for (String s : keySet) {
            java.util.Date date = DateUtils.toDate(s, TEST_CASE.get(s));
            String newS = DateUtils.toString(date, TEST_CASE.get(s));
            Assert.assertEquals(s, newS);
            System.out.println(s);
        }

    }

    @Test
    public void testFormatJavaDate2String() {
        Set<String> keySet = TEST_CASE.keySet();
        for (String s : keySet) {
            java.util.Date date = DateUtils.toDate(s, TEST_CASE.get(s));
            String newS = DateUtils.formatJavaDate2String(date, TEST_CASE.get(s));
            Assert.assertEquals(s, newS);
            System.out.println(s);
        }
    }


    @Test
    public void testGetNextSec1() throws Exception {
        String oldDate = "20150101010101";
        Date od = DateUtils.toDate(oldDate);
        Date nd = DateUtils.getNextSec(od, 5);
        Assert.assertEquals("20150101010106", DateUtils.toString(nd, DateStyle.FILE_FORMAT3));
        nd = DateUtils.getNextSec(od, 60 * 60 * 24);
        Assert.assertEquals("20150102010101", DateUtils.toString(nd, DateStyle.FILE_FORMAT3));
    }

    @Test
    public void testGetNextSec2() throws Exception {
        String oldDate = "20150101010101";
        String nd = DateUtils.getNextSec(oldDate, 5);
        Assert.assertEquals("20150101010106", nd);
        nd = DateUtils.getNextSec(oldDate, 60 * 60 * 24);
        Assert.assertEquals("20150102010101", nd);
    }
    @Test
    public void testGetNextDay1() throws Exception {
        String oldDate = "20150101010101";
        Date od = DateUtils.toDate(oldDate);
        Date nd = DateUtils.getNextDay(od, 5);
        Assert.assertEquals("20150106010101", DateUtils.toString(nd, DateStyle.FILE_FORMAT3));
        nd = DateUtils.getNextDay(od, 30);
        Assert.assertEquals("20150131010101", DateUtils.toString(nd, DateStyle.FILE_FORMAT3));
    }
    @Test
    public void testGetNextDay2() throws Exception {
        String oldDate = "20150101010101";
        String nd = DateUtils.getNextDay(oldDate, 5);
        Assert.assertEquals("20150106010101", nd);
        nd = DateUtils.getNextDay(oldDate, 30);
        Assert.assertEquals("20150131010101", nd);
    }
}
