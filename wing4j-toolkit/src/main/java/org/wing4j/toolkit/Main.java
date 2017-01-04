package org.wing4j.toolkit;

import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import lombok.extern.slf4j.Slf4j;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityUtils;
import org.wing4j.orm.entity.utils.ReverseEntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wing4j on 2017/1/2.
 */
@Slf4j
public class Main {
    protected int commandCount = 0;
    /**
     * 存放执行过的历史命令
     */
    protected HashMap<Integer, String> history = new HashMap<Integer, String>();
    /**
     * 命令集合
     */
    protected static CommandCollection commandCollection = new CommandCollection();

    static {
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
            define.setExample("help 帮助信息");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("quit");
            define.setExample("quit 退出CLI");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("退出CLI");
            define.setCmd("exit");
            define.setExample("exit 退出CLI");
            commandCollection.addDefine(define);
        }
        {
            CommandDefine define = new CommandDefine();
            define.setName("查看历史命令");
            define.setCmd("history");
            define.setExample("history 查看历史命令");
            commandCollection.addDefine(define);
        }
    }

    /**
     * 保存执行过的历史
     *
     * @param i   序号
     * @param cmd 命令
     */
    protected void addToHistory(int i, String cmd) {
        history.put(i, cmd);
    }

    protected String getPrompt() {
        return "cli>";
    }

    /**
     * 用法提示方法
     */
    static void usage() {
        System.err.println("Wing4j toolkit用法:");
        System.out.println(Help.toString(commandCollection));
    }

    protected boolean processCmd(Command command) {
        String cmd = command.getCmd();
        if (cmd.equals("quit") || cmd.equals("exit")) {
            System.out.println("Quitting...");
            System.exit(0);
        } else if (cmd.equals("history")) {
            for (int i = commandCount - 10; i <= commandCount; ++i) {
                if (i < 0) continue;
                System.out.println(i + " - " + history.get(i));
            }
        } else if (cmd.equals("reverse")) {
            String packageName = command.valueString("package");
            String schema = command.valueString("schema");
            String h = command.valueString("h");
            String u = command.valueString("u");
            String p = command.valueString("p");
            if (packageName == null) {
                System.out.println("package is empty!");
                return false;
            }
            if (schema == null) {
                System.out.println("schema is empty!");
                return false;
            }
            try {
                List<TableMetadata> tableMetadatas = ReverseEntityUtils.reverseFormDatabase(schema, h, u, p);
                EntityUtils.generate(tableMetadatas, packageName);
            } catch (Exception e) {
                log.error("逆向生成失败", e);
            }
        } else if (cmd.equals("help")) {//什么都不能匹配则输出用法
            usage();
        }
        return true;
    }

    /**
     * 解析命令行
     *
     * @param line 命令字符串
     */
    public void executeLine(String line) {
        if (!line.equals("")) {
            String[] args = line.split(" ");
            CommandDefine define = commandCollection.getOptionCollection().get(args[0]);
            if (define == null) {
                System.out.println(commandCollection.getOptionCollection().keySet());
                System.out.println("无效的命令【" + args[0] + "】");
                return;
            }
            Command command = define.parseCommand(line);
            addToHistory(commandCount, line);
            processCmd(command);
            commandCount++;
        }
    }

    void run() throws IOException {
        System.out.println("Welcome to Wing4j toolkit!");
        boolean jlinemissing = false;
        // only use jline if it's in the classpath
        try {
            Class consoleC = Class.forName("jline.console.ConsoleReader");
            Object console = consoleC.newInstance();
            System.out.println("JLine support is enabled");
            Method addCompleter = consoleC.getMethod("addCompleter", Completer.class);
            addCompleter.invoke(console, new FileNameCompleter());
            String line;
            Method readLine = consoleC.getMethod("readLine", String.class);
            while ((line = (String) readLine.invoke(console, getPrompt())) != null) {
                executeLine(line);
            }
        } catch (Exception e) {
            log.debug("Unable to start jline", e);
            jlinemissing = true;
        }

        if (jlinemissing) {
            System.out.println("JLine support is disabled");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = br.readLine()) != null) {
                executeLine(line);
            }
        }

    }

    /**
     * 启动CLI命令
     *
     * @param args 参数
     * @throws IOException 异常
     */
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run();
    }
}
