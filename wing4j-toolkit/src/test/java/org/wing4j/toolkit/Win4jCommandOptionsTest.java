package org.wing4j.toolkit;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/2.
 */
public class Win4jCommandOptionsTest {

    @Test
    public void testParseOptions() throws Exception {
        Win4jCommandOptions option = new Win4jCommandOptions();
        option.parseCommand("reverse -h 192.168.1.1:3360 -u root -p root");
        option.parseOptions();
        System.out.println(option.options);
        Assert.assertEquals("root", option.options.get("p"));
        Assert.assertEquals("root", option.options.get("u"));
        Assert.assertEquals("192.168.1.1:3360", option.options.get("h"));
    }
}