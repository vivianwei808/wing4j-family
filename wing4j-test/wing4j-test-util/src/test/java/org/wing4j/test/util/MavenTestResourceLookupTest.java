package org.wing4j.test.util;


import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URI;

/**
 * Created by wing4j on 2016/12/30.
 */
public class MavenTestResourceLookupTest {
    /**
     * 打开与当前类放在一起的资源
     *
     * @throws Exception 异常
     */
    @Test
    public void testOpen() throws Exception {
        URI uri = MavenTestResourceLookup.open("test.txt");
        InputStream is = uri.toURL().openStream();
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        Assert.assertEquals("this is a test text!", new String(bytes));
    }

    /**
     * 打开与当前类放在一起的资源
     *
     * @throws Exception 异常
     */
    @Test
    public void testOpen1() throws Exception {
        URI uri = MavenTestResourceLookup.open("org.wing4j.test.util", "test.txt");
        InputStream is = uri.toURL().openStream();
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        Assert.assertEquals("this is a test text!", new String(bytes));
    }

    /**
     * 获取当前类所对应的路径
     *
     * @throws Exception 异常
     */
    @Test
    public void testGetCurrentPackage() throws Exception {
        String path = MavenTestResourceLookup.getCurrentPackage();
        Assert.assertEquals("org.wing4j.test.util", path);
    }

}