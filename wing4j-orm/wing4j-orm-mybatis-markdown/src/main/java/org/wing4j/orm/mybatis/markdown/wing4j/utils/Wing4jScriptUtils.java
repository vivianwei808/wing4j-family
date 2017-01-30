package org.wing4j.orm.mybatis.markdown.wing4j.utils;
import static org.wing4j.orm.mybatis.markdown.wing4j.MarkdownMapperConstants.*;
/**
 * Created by wing4j on 2017/1/30.
 */
public class Wing4jScriptUtils {
    /**
     * 检查是否是脚本行
     * @param line 文本行
     * @return 如果是脚本行，则返回真
     */
    public static boolean ifScript(String line){
        return line.startsWith(SCRIPT_BEGIN) && line.endsWith(SCRIPT_END);
    }

    /**
     * 去除脚本标签
     * @param line 文本行
     * @return 去除脚本标签文本行
     */
    public static String trimScript(String line){
        return line.substring(SCRIPT_BEGIN.length(), line.length() - SCRIPT_END.length()).trim();
    }

    public static String trimIf(String line){
        return line.substring(SCRIPT_IF_BEGIN.length()).trim();
    }
}
