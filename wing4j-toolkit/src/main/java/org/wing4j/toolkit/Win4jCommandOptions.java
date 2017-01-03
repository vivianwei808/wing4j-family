package org.wing4j.toolkit;

import lombok.Data;
import lombok.ToString;

import java.util.*;

@Data
@ToString
public class Win4jCommandOptions {
    /**
     * 执行命令的参数名
     */
    protected Map<String, String> options = new HashMap<String, String>();
    /**
     * 执行命令的参数值
     */
    protected List<String> cmdArgs = null;
    /**
     * 命令
     */
    protected String command = null;

    /**
     * 解析参数
     *
     * @return 处理完成
     */
    boolean parseOptions() {
        Iterator<String> it = cmdArgs.iterator();
        while (it.hasNext()) {
            String opt = it.next();
            if (opt.startsWith("-")) {
                options.put(opt.substring(1), it.next());
                continue;
            }
        }
        return true;
    }

    /**
     * 解析命令
     * @param cmd 命令字符串
     * @return 处理完成
     */
    public boolean parseCommand(String cmd) {
        String[] args = cmd.split(" ");
        if (args.length == 0){
            return false;
        }
        command = args[0];
        cmdArgs = Arrays.asList(args);
        parseOptions();
        return true;
    }
}
