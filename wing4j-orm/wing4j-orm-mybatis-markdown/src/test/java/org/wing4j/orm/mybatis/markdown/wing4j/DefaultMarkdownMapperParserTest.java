package org.wing4j.orm.mybatis.markdown.wing4j;

import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class DefaultMarkdownMapperParserTest {

    @Test
    public void testParse() throws Exception {
        MarkdownMapperParser parser = new DefaultMarkdownMapperParser();
        MarkdownContext ctx = new MarkdownContext();
        ctx.setFile(new File("target/test-classes/example1.md").toURI());
        ctx.setFileEncoding("GBK");
        Configuration config = new Configuration();
        config.setCacheEnabled(true);
        config.setLazyLoadingEnabled(false);
        config.setAggressiveLazyLoading(true);
        ctx.setConfig(config);
        parser.parse(ctx);
    }
}