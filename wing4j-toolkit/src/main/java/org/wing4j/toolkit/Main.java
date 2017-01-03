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
import java.util.Map;

/**
 * Created by wing4j on 2017/1/2.
 */
@Slf4j
public class Main {
    protected int commandCount = 0;
    /**
     * 存放支持的命令
     */
    protected static final Map<String, String> commandMap = new HashMap<String, String>();
    /**
     * 存放执行过的历史命令
     */
    protected HashMap<Integer, String> history = new HashMap<Integer, String>();
    /**
     * 命令选项
     */
    protected Win4jCommandOptions co = new Win4jCommandOptions();

    static {
        commandMap.put("reverse", "-h jdbc:mysql://192.168.1.106:3306/wing4j -u root -p root -schema wing4j -package org.wing4j.entity");
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
        return "wing4j>";
    }

    /**
     * 用法提示方法
     */
    static void usage() {
        System.err.println("Wing4j cmd args");
        for (String cmd : commandMap.keySet()) {
            System.err.println("\t" + cmd + " " + commandMap.get(cmd));
        }
    }

    protected boolean processCmd(Win4jCommandOptions co) {
        String cmd = co.getCommand();
        if (cmd.equals("quit") || cmd.equals("exit")) {
            System.out.println("Quitting...");
            System.exit(0);
        } else if (cmd.equals("his")) {
            for (int i = commandCount - 10; i <= commandCount; ++i) {
                if (i < 0) continue;
                System.out.println(i + " - " + history.get(i));
            }
        } else if (cmd.equals("reverse")) {
            Map<String, String> options = co.getOptions();
            String packageName = options.get("package");
            String schema = options.get("schema");
            String h = options.get("h");
            String u = options.get("u");
            String p = options.get("p");
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
        } else {//什么都不能匹配则输出用法
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
            co.parseCommand(line);
            addToHistory(commandCount, line);
            processCmd(co);
            commandCount++;
        }
    }

    void run() throws IOException {
        System.out.println("Welcome to Wing4j!");
        if (co.getCommand() == null) {
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
