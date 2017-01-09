package org.wing4j.common.markdown;

import java.net.URI;

/**
 * Created by wing4j on 2017/1/9.
 */
public interface MarkdownParser {
    /**
     * 解析markdown文件
     * @param ctx 上下文
     */
    void parse(MarkdownContext ctx);
}
