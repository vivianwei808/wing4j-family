package org.wing4j.toolkit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2017/1/4.
 */
public class HelpTest {

    @Test
    public void testToString() throws Exception {
        CommandCollection commandCollection = new CommandCollection();
        {
            CommandDefine define = new CommandDefine();
            define.addOption("url", "h", true, 1, "数据库地址", "jdbc:mysql://192.168.1.106:3306/wing4j");
            define.addOption("u", true, 1, "用户名", "root");
            define.addOption("p", true, 1, "密码", "root");
            define.addOption("schema", true, 1, "数据库模式", "wing4j");
            define.addOption("package", true, 1, "保存包名", "org.wing4j.entity");
            define.setName("逆向工程");
            define.setCmd("reverse");
            define.setExample("reverse -h jdbc:mysql://192.168.1.106:3306/wing4j -u root -p root -schema wing4j -package org.wing4j.entity");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("帮助信息");
            define.setCmd("help");
            define.setExample("help");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("quit");
            define.setExample("quit");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("exit");
            define.setExample("exit");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("查看历史命令");
            define.setCmd("history");
            define.setExample("history");
            commandCollection.addDefine(define);
        }
        System.out.println(Help.toString(commandCollection));
    }
}