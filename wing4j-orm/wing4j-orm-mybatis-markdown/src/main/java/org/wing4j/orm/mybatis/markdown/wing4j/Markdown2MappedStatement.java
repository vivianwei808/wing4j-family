package org.wing4j.orm.mybatis.markdown.wing4j;

import org.apache.ibatis.mapping.MappedStatement;

public interface Markdown2MappedStatement {
    MappedStatement convert(MarkdownStatment markdownStatment);
}