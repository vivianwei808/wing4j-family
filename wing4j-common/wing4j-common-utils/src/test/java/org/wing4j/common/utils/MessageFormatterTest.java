package org.wing4j.common.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2017/1/6.
 */
public class MessageFormatterTest {

    @Test
    public void testFormat() throws Exception {
        Assert.assertEquals("this is placeHolder A", MessageFormatter.format("this is placeHolder {}", "A"));
        Assert.assertEquals("this is placeHolder 1", MessageFormatter.format("this is placeHolder {}", 1));
        Assert.assertEquals("this is placeHolder true", MessageFormatter.format("this is placeHolder {}", true));
        Assert.assertEquals("this is placeHolder 1.1", MessageFormatter.format("this is placeHolder {}", 1.1D));
        Assert.assertEquals("this is placeHolder 1 2 3", MessageFormatter.format("this is placeHolder {} {} {}", 1, 2, 3));
        Assert.assertEquals("this is placeHolder {}", MessageFormatter.format("this is placeHolder {}"));
        Assert.assertEquals("this is placeHolder ", MessageFormatter.format("this is placeHolder "));
    }
}