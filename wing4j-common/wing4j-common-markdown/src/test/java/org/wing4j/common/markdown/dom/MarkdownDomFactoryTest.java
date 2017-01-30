package org.wing4j.common.markdown.dom;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by wing4j on 2017/1/30.
 */
public class MarkdownDomFactoryTest {

    @Test
    public void testCreate() throws Exception {
        MarkdownDomParser parser = new MarkdownDomParserFactory().create(MarkdownDomParserFactory.DEFAULT);
        Assert.assertNotNull(parser);
    }
}