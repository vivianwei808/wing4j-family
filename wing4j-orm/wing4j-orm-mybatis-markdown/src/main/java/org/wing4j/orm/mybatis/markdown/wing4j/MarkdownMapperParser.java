package org.wing4j.orm.mybatis.markdown.wing4j;

import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * Created by wing4j on 2017/1/9.
 */
public interface MarkdownMapperParser extends MarkdownMapperConstants{
    /**
     * 注册插件
     * @param plugin 插件
     */
    void register(Plugin plugin);
    /**
     * 解析markdown文件
     * @param ctx 上下文
     */
    List<MappedStatement> parse(MarkdownContext ctx);
}
