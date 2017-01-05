package org.wing4j.toolkit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@ToString
public class CommandDefine {
    @Setter
    @Getter
    String name;
    @Setter
    @Getter
    String cmd;
    /**
     * 参数定义
     */
    protected final Map<String, Option> options = new HashMap<>();
    /**
     * 别名
     */
    protected final Map<String, String> alias = new HashMap<>();
    @Setter
    @Getter
    protected String example;
    @Setter
    @Getter
    protected String extrInfo;
    /**
     * 获取选项信息
     *
     * @param name 参数名
     * @return 选项
     */
    public Option getOption(String name) {
        String longName = alias.get(name);
        if (longName == null) {
            longName = name;
        }
        Option option = options.get(longName);
        if (option == null) {
            throw new IllegalArgumentException("无效的参数");
        } else {
            return option;
        }

    }

    /**
     * 增加选项
     *
     * @param longName 长命名
     * @param require  是否必须
     * @param argNum   参数数目
     * @return 对象
     */
    public CommandDefine addOption(String longName, boolean require, int argNum, String desc, String example) {
        return addOption(longName, longName, require, argNum, desc, example);
    }

    /**
     * 增加选项
     *
     * @param longName  长命名
     * @param shortName 短命名
     * @param require   是否必须
     * @param argNum    参数数目
     * @return 对象
     */
    public CommandDefine addOption(String longName, String shortName, boolean require, int argNum, String desc, String example) {
        Option option = new Option(longName, shortName, require, argNum, desc, example);
        return addOption(option);
    }

    /**
     * 增加选项
     *
     * @param option 选项
     * @return 对象
     */
    public CommandDefine addOption(Option option) {
        String longName = option.longName;
        String shortName = option.shortName;
        if (longName == null) {
            throw new NullPointerException("无效的长命名");
        }
        if (shortName == null) {
            option.shortName = longName;
        }
        if (options.containsKey(longName)) {
            return this;
        }
        String name = alias.get(shortName);
        if (options.containsKey(name)) {
            return this;
        }
        options.put(longName, option);
        if (shortName != null && !shortName.isEmpty()) {
            alias.put(shortName, longName);
        }
        return this;
    }

    /**
     * 解析参数
     *
     * @return 处理完成
     */
    boolean parseOptions(Command command, List<String> cmdArgs) {
        Iterator<String> it = cmdArgs.iterator();
        while (it.hasNext()) {
            String opt = it.next();
            if (opt.startsWith("-")) {
                String argName = opt.substring(1);
                Option option = getOption(argName);
                int argNum = option.argNum;
                if (argNum == 0) {
                    command.args.put(option.getLongName(), new ArrayList<String>());
                } else if (argNum == 1) {
                    String arg = null;
                    if (it.hasNext()) {
                        arg = it.next();
                    } else {
                        throw new IllegalArgumentException("无效参数");
                    }
                    command.args.put(option.getLongName(), Arrays.asList(arg));
                } else {
                    List<String> args = new ArrayList<>();
                    int count = 0;
                    String param = null;
                    while (it.hasNext() && !(param = it.next()).startsWith("-")) {
                        count++;
                        args.add(param);
                    }
                    if (count != option.argNum) {
                        throw new IllegalArgumentException("无效参数");
                    }
                    command.args.put(option.getLongName(), args);
                }


                continue;
            }
        }
        return true;
    }

    /**
     * 解析命令
     *
     * @param cmd 命令字符串
     * @return 处理完成
     */
    public Command parseCommand(String cmd) {
        String[] args = cmd.split(" ");
        if (args.length == 0) {
            return null;
        }
        Command command = new Command(args[0], this);
        List<String> cmdArgs = Arrays.asList(args);
        parseOptions(command, cmdArgs);
        return command;
    }
}
