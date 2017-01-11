package org.wing4j.orm.mybatis.markdown.wing4j;

/**
 * Created by wing4j on 2017/1/9.
 */
public interface MarkdownParser {
    /**
     * 注册插件
     * @param plugin 插件
     */
    void register(Plugin plugin);
    /**
     * 解析markdown文件
     * @param ctx 上下文
     */
    void parse(MarkdownContext ctx);
}
