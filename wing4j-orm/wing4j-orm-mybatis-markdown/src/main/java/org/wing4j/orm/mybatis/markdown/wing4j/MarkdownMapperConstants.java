package org.wing4j.orm.mybatis.markdown.wing4j;

/**
 * Created by wing4j on 2017/1/30.
 * Markdown文件格式的Mapper需要的常量
 */
public interface MarkdownMapperConstants {
    /**
     * 注释引导前缀
     */
    String COMMENT_PREFIX = "--";
    /**
     * 行注释
     */
    String LINE_COMMENT = "//";
    /**
     * 参数引导前缀
     */
    String PARAM_PREFIX = "--@";
    /**
     * 脚本命令开始
     */
    String SCRIPT_BEGIN = "/*#";
    /**
     * 脚本命令结束
     */
    String SCRIPT_END = "*/";

    String SCRIPT_IF_BEGIN = "if";
    String SCRIPT_IF_END = "fi";
}
