package org.wing4j.orm.mybatis.markdown;

import org.junit.Test;
import org.wing4j.orm.mybatis.markdown.wing4j.MarkdownContext;
import org.wing4j.orm.mybatis.markdown.wing4j.MarkdownParser;
import org.wing4j.orm.mybatis.markdown.wing4j.plugins.SqlLinePlugin;
import org.wing4j.orm.mybatis.markdown.wing4j.Wing4jMarkdownParser;

import java.io.File;

/**
 * Created by woate on 2017/1/9.
 */
public class Wing4jMarkdownParserTest {

    @Test
    public void testParse() throws Exception {
        MarkdownParser parser = new Wing4jMarkdownParser();
        MarkdownContext ctx = new MarkdownContext();
        ctx.setFile(new File("target/test-classes/sql.md").toURI());
        ctx.setFileEncoding("GBK");
        parser.register(new SqlLinePlugin());
        parser.parse(ctx);
        System.out.println(ctx);
    }
}