package org.wing4j.toolkit;

import org.junit.Test;

import java.io.PrintWriter;

/**
 * Created by woate on 2017/1/5.
 */
public class HelpFormatterTest {

    @Test
    public void testRenderOption() throws Exception {
        CommandDefine define = new CommandDefine();
        define.addOption("url", "h", true, 1, "数据库地址", "jdbc:mysql://192.168.1.106:3306/wing4j");
        define.addOption("username", "u", true, 1, "用户名", "root");
        define.addOption("password", "p", true, 1, "密码", "root");
        define.addOption("schema", "s", true, 1, "数据库模式", "wing4j");
        define.addOption("package", "f", true, 1, "保存包名", "org.wing4j.entity");
        define.setName("逆向工程");
        define.setCmd("reverse");
        define.setExample("reverse -h jdbc:mysql://192.168.1.106:3306/wing4j -u root -p root -schema wing4j -package org.wing4j.entity");
        define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
        HelpFormatter formmatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        formmatter.renderOption(pw, "其中选项包括:", define, "");
        pw.flush();
    }

    @Test
    public void testRender() throws Exception {
        CommandDefine define = new CommandDefine();
        define.addOption("url", "h", true, 1, "数据库地址", "jdbc:mysql://192.168.1.106:3306/wing4j");
        define.addOption("username", "u", true, 1, "用户名", "root");
        define.addOption("password", "p", true, 1, "密码", "root");
        define.addOption("schema", "s", true, 1, "数据库模式", "wing4j");
        define.addOption("package", "f", true, 1, "保存包名", "org.wing4j.entity");
        define.setName("逆向工程");
        define.setCmd("reverse");
        define.setExample("reverse -h jdbc:mysql://192.168.1.106:3306/wing4j -u root -p root -schema wing4j -package org.wing4j.entity");
        define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
        HelpFormatter formmatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        formmatter.render(pw,"", define,"有关详细信息, 请参阅 http://www.wing4j.org/help");
        pw.flush();
    }

    @Test
    public void testRenderUsage() throws Exception {
        CommandDefine define = new CommandDefine();
        define.addOption("url", "h", true, 1, "数据库地址", "jdbc:mysql://192.168.1.106:3306/wing4j");
        define.addOption("username", "u", true, 1, "用户名", "root");
        define.addOption("password", "p", true, 1, "密码", "root");
        define.addOption("schema", "s", true, 1, "数据库模式", "wing4j");
        define.addOption("package", "f", true, 1, "保存包名", "org.wing4j.entity");
        define.setName("逆向工程");
        define.setCmd("reverse");
        define.setExample("reverse -h jdbc:mysql://192.168.1.106:3306/wing4j -u root -p root -schema wing4j -package org.wing4j.entity");
        define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
        HelpFormatter formmatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        formmatter.renderUsage(pw, "", define,"");
        pw.flush();
    }

    @Test
    public void testRender1() throws Exception {
        CommandCollection commandCollection = new CommandCollection();
        {
            CommandDefine define = new CommandDefine();
            define.addOption("url", "h", true, 1, "数据库地址", "jdbc:mysql://192.168.1.106:3306/wing4j");
            define.addOption("username", "u", true, 1, "用户名", "root");
            define.addOption("password", "p", true, 1, "密码", "root");
            define.addOption("schema", "s", true, 1, "数据库模式", "wing4j");
            define.addOption("package", "f", true, 1, "保存包名", "org.wing4j.entity");
            define.setName("逆向工程");
            define.setCmd("reverse");
            define.setExample("reverse -h jdbc:mysql://192.168.1.106:3306/wing4j -u root -p root -schema wing4j -package org.wing4j.entity");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("帮助信息");
            define.setCmd("help");
            define.setExample("help");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("quit");
            define.setExample("quit");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("exit");
            define.setExample("exit");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("查看历史命令");
            define.setCmd("history");
            define.setExample("history");
            define.setExtrInfo("有关详细信息, 请参阅 http://www.wing4j.org/help");
            commandCollection.addDefine(define);
        }
        HelpFormatter formmatter = new HelpFormatter();
        formmatter.render("", commandCollection,"");
    }
}