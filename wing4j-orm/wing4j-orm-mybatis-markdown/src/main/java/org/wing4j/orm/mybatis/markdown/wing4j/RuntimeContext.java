package org.wing4j.orm.mybatis.markdown.wing4j;

import org.wing4j.orm.mybatis.markdown.wing4j.expression.SqlExp;

import java.sql.SQLException;

/**
 * 运行时上下文
 */
public class RuntimeContext {
    /**
     * 行号
     */
    public int lineNo = 0;
    /**
     * 处理后的行
     */
    public String line;
    /**
     * 原始行
     */
    public String originalLine;
    /**
     * 代码块
     */
    public boolean codeBlock = false;
    /**
     * 配置
     */
    public boolean configure = false;
    /**
     * 参数
     */
    public boolean params = false;
    /**
     * SQL
     */
    public boolean sql = false;

    public MarkdownStatment statment;
    /**
     * SQL表达式
     */
    public SqlExp sqlExp;
    /**
     * 解析出的默认值
     */
    public String defaultValue;
}
