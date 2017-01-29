package org.wing4j.orm.mybatis.markdown.wing4j.select;

import org.junit.Test;
import org.wing4j.orm.mybatis.markdown.wing4j.MarkdownContext;
import org.wing4j.orm.mybatis.markdown.wing4j.MarkdownParser;
import org.wing4j.orm.mybatis.markdown.wing4j.Wing4jMarkdownParser;
import org.wing4j.orm.mybatis.markdown.wing4j.plugins.SqlLinePlugin;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by woate on 2017/1/29.
 */
public class SelectMarkdown2MappedStatementTest {

    @Test
    public void testConvert() throws Exception {
        MarkdownParser parser = new Wing4jMarkdownParser();
        MarkdownContext ctx = new MarkdownContext();
        ctx.setFile(new File("target/test-classes/sql.md").toURI());
        ctx.setFileEncoding("GBK");
        parser.register(new SqlLinePlugin());
        parser.parse(ctx);
        SelectMarkdown2MappedStatement statement = new SelectMarkdown2MappedStatement();
        statement.convert(ctx.getStatments().get(0));
    }
}