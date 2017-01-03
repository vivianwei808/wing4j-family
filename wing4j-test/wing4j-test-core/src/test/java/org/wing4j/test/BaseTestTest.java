package org.wing4j.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2016/12/31.
 */
public class BaseTestTest{
    /**
     * 测试测试基类上打开Maven测试资源
     * @throws Exception 异常
     */
    @Test
    public void testOpenMavenTestResouce() throws Exception {
        InputStream is = new BaseTest(){}.openMavenTestResouce("test.txt");
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        Assert.assertEquals("this is a test text!", new String(bytes));
    }
}