package org.wing4j.toolkit.cli;

import lombok.Data;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by woate on 2017/1/4.
 */
@Data
public class HelpFormatter {
    /**
     * 输出每行信息的行宽
     */
    int width = 74;
    /**
     * 左边距
     */
    int leftPadding = 1;
    /**
     * 描述边距
     */
    int descPadding = 3;
    /**
     * 整个帮助信息输出的前缀
     */
    String syntaxPrefix = "用法: ";
    /**
     * 新行换行符号
     */
    String newLine = System.getProperty("line.separator");
    /**
     * 短命名参数前缀
     */
    String shortOptionPrefix = "-";
    /**
     * 长命名参数前缀
     */
    String longOptionPrefix = "-";
    /**
     * 长命名参数分隔符
     */
    String longOptSeparator = " ";
    /**
     * 默认参数名字,用于表示参数
     */
    String argName = "arg";

    boolean isBlank(String string) {
        return string == null || string.isEmpty();
    }

    boolean isNotBlank(String string) {
        return string != null && !string.isEmpty();
    }
    /**
     * 渲染出命令信息
     *
     * @param header 选项头信息
     * @param commandCollection 命令集合
     * @param footer 选项尾信息
     */
    public void render(String header, CommandCollection commandCollection, String footer) {
        PrintWriter pw = new PrintWriter(System.out);
        for (CommandDefine define :commandCollection.getOptionCollection().values()){
            render(pw, header, define, footer);
            pw.append(newLine);
        }
        pw.flush();
    }
    /**
     * 渲染出命令信息
     *
     * @param header 选项头信息
     * @param define 命令定义信息
     * @param footer 选项尾信息
     */
    public void render(String header, CommandDefine define, String footer) {
        PrintWriter pw = new PrintWriter(System.out);
        render(pw, header, define, footer);
        pw.append(newLine);
        pw.flush();
    }
    /**
     * 渲染出命令信息
     *
     * @param header 选项头信息
     * @param define 命令定义信息
     * @param footer 选项尾信息
     */
    public void render(PrintWriter pw, String header, CommandDefine define, String footer) {
        if (isBlank(syntaxPrefix)) {
            throw new IllegalArgumentException("syntaxPrefix not provided");
        }
        if (isNotBlank(header)) {
            pw.append(header).append(newLine);
        }
        pw.append(define.getName()).append("(").append(define.getCmd()).append(")").append(newLine);
        renderUsage(pw, "", define, "");
        renderOption(pw, "其中选项包括:", define, "");
        renderExample(pw, "", define, "");
        if (isNotBlank(define.getExtrInfo())) {
            pw.append(newLine).append(define.getExtrInfo());
        }
        if (isNotBlank(footer)) {
            pw.append(newLine).append(footer);
        }

    }

    public void renderUsage(PrintWriter pw, String header, CommandDefine define, String footer) {
        if(isBlank(define.getExample())){
            return;
        }
        final String lpad = createPadding(leftPadding);
        final String dpad = createPadding(descPadding);
        StringBuffer sb = new StringBuffer();
        if (isNotBlank(header)) {
            sb.append(header);
        }
        sb.append(syntaxPrefix).append(define.getCmd()).append(lpad);
        if (!define.options.isEmpty()) {
            sb.append("-options");
        }

        if (isNotBlank(header)) {
            sb.append(newLine).append(footer);
        }
        pw.append(sb).append(newLine);
    }

    /**
     * 渲染出选项信息
     *
     * @param pw     输出字符流
     * @param header 选项头信息
     * @param define 命令定义信息
     * @param footer 选项尾信息
     */
    public void renderOption(PrintWriter pw, String header, CommandDefine define, String footer) {
        if (define.options.isEmpty()) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        if (isNotBlank(header)) {
            sb.append(header).append(newLine);
        }
        final String lpad = createPadding(leftPadding);
        final String dpad = createPadding(descPadding);
        int max = 0;
        List<StringBuffer> prefixList = new ArrayList<StringBuffer>();
        for (Option option : define.options.values()) {
            StringBuffer optBuf = new StringBuffer();
            if (option.getLongName().equals(option.getShortName())) {
                optBuf.append(lpad).append(" ").append(getLongOptionPrefix()).append(option.getLongName());
            } else {
                optBuf.append(lpad).append(" ").append(getShortOptionPrefix()).append(option.getShortName()).append(" | ").append(getLongOptionPrefix()).append(option.getLongName());
            }
            if (option.getArgNum() > 0) {
                optBuf.append(!option.getLongName().equals(option.getShortName()) ? longOptSeparator : " ");
                optBuf.append("<");
                if (isBlank(option.getExample())) {
                    optBuf.append(getArgName());
                } else {
                    optBuf.append(option.getExample());
                }
                optBuf.append(">");

            } else {
                optBuf.append(" ");
            }
            optBuf.append(newLine).append("\t\t\t\t").append(option.getDesc());
            prefixList.add(optBuf);
            max = optBuf.length() > max ? optBuf.length() : max;
        }
        int x = 0;

        for (Iterator<Option> it = define.options.values().iterator(); it.hasNext(); ) {
            Option option = it.next();
            StringBuilder optBuf = new StringBuilder(prefixList.get(x++));
            if (optBuf.length() < max) {
                optBuf.append(createPadding(max - optBuf.length()));
            }
            optBuf.append(dpad);
            if (isBlank(option.getDesc())) {
                optBuf.append(option.getDesc());
            }
            sb.append(optBuf);
            if (it.hasNext()) {
                sb.append(getNewLine());
            }
        }
        if (isNotBlank(footer)) {
            sb.append(newLine).append(footer);
        }
        pw.append(sb).append(newLine);;
    }

    /**
     * 打印命令的例子
     *
     * @param pw     输出字符流
     * @param header 选项头信息
     * @param define 命令定义信息
     * @param footer 选项尾信息
     */
    public void renderExample(PrintWriter pw, String header, CommandDefine define, String footer) {
        if(isBlank(define.getExample())){
            return;
        }
        StringBuffer sb = new StringBuffer();
        if (isNotBlank(header)) {
            sb.append(header).append(newLine);
        }
        sb.append(define.getExample());
        if (isNotBlank(footer)) {
            sb.append(newLine).append(footer);
        }
        pw.append(sb);
    }

    /**
     * 创建填充边距
     *
     * @param len 长度
     * @return 填充信息
     */
    String createPadding(int len) {
        char[] padding = new char[len];
        Arrays.fill(padding, ' ');
        return new String(padding);
    }
}
