package org.wing4j.toolkit.cli;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wing4j on 2017/1/4.
 */
public class Command {
    /**
     * 执行命令的参数名
     */
    @Getter
    protected final Map<String, List<String>> args = new HashMap<String, List<String>>();
    /**
     * 命令
     */
    @Getter
    protected String cmd = null;

    @Getter
    protected CommandDefine commandOptions = null;

    public Command(String cmd, CommandDefine commandOptions) {
        this.cmd = cmd;
        this.commandOptions = commandOptions;
    }

    /**
     * 检查是否有传入指定的参数
     *
     * @param name 参数名字
     * @return 含有返回真，反之为假
     */
    public boolean hasOption(String name) {
        if (args.containsKey(name)) {
            return true;
        } else {
            String longName = commandOptions.alias.get(name);
            return args.containsKey(longName);
        }
    }

    /**
     * 提取参数数组
     *
     * @param name 参数名字
     * @return 参数值
     */
    public String[] valueArray(String name) {
        if (args.containsKey(name)) {
            return args.get(name).toArray(new String[0]);
        }
        String longName = commandOptions.alias.get(name);
        if (args.containsKey(longName)) {
            return args.get(longName).toArray(new String[0]);
        } else {
            return null;
        }
    }

    /**
     * 获取参数值
     *
     * @param name 参数名字
     * @return 参数值
     */
    public String valueString(String name) {
        if (args.containsKey(name)) {
            return args.get(name).get(0);
        }
        String longName = commandOptions.alias.get(name);
        if (args.containsKey(longName)) {
            return args.get(longName).get(0);
        } else {
            return null;
        }
    }
}
