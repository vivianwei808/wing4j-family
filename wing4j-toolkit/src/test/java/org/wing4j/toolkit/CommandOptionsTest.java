package org.wing4j.toolkit;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wing4j on 2017/1/2.
 */
public class CommandOptionsTest {

    @Test
    public void testParseOptions() throws Exception {
        CommandDefine option = new CommandDefine();
        option.addOption(Option.builder().longName("url").shortName("h").require(true).argNum(1).build());
        option.addOption(Option.builder().longName("u").require(true).argNum(1).build());
        option.addOption(Option.builder().longName("p").require(true).argNum(1).build());
        Command command = option.parseCommand("reverse -h 192.168.1.1:3360 -u root -p root");
        Assert.assertEquals("root", command.valueString("p"));
        Assert.assertEquals("root", command.valueString("u"));
        Assert.assertEquals("192.168.1.1:3360", command.valueString("h"));
        Assert.assertEquals("192.168.1.1:3360", command.valueString("url"));
    }
    @Test
    public void testAddOption() throws Exception {
        CommandDefine commandOptions = new CommandDefine();
        commandOptions.addOption("help", "h", true, 0, "", "");
        Command command = commandOptions.parseCommand("cmd -h");
        System.out.println(command);
    }

    @Test
    public void testAddOption1() throws Exception {
        CommandDefine commandOptions = new CommandDefine();
        commandOptions.addOption("url", true, 1, "", "");
        Command command = commandOptions.parseCommand("cmd -url http://localhost");
        Assert.assertEquals("cmd", command.cmd);
        Assert.assertEquals("http://localhost", command.valueString("url"));
    }

    @Test
    public void testAddOption2() throws Exception {
        CommandDefine commandOptions = new CommandDefine();
        commandOptions.addOption("url", true, 2, "", "");
        Command command = commandOptions.parseCommand("cmd -url http://www.baidu.com http://www.google.com");
        Assert.assertEquals("cmd", command.cmd);
        Assert.assertEquals(2, command.valueArray("url").length);
        Assert.assertEquals("http://www.baidu.com", command.valueArray("url")[0]);
        Assert.assertEquals("http://www.google.com", command.valueArray("url")[1]);
    }
}