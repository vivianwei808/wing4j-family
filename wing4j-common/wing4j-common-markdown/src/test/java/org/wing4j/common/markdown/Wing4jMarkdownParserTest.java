package org.wing4j.common.markdown;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

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
        parser.parse(ctx);
        System.out.println(ctx);
    }
}