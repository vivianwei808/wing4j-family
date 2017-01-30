package org.wing4j.common.markdown.dom.implement;

import org.junit.Assert;
import org.junit.Test;
import org.wing4j.common.markdown.dom.element.block.MarkdownHeaderBlock;
import org.wing4j.common.markdown.dom.element.span.MarkdownLinkSpan;
import org.wing4j.common.markdown.dom.element.span.MarkdownTextSpan;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class MarkdownHeaderParserTest {

    @Test
    public void testParse1() throws Exception {
        MarkdownHeaderParser parser = new MarkdownHeaderParser();
        {
            MarkdownHeaderBlock markdownHeaderBlock = parser.parse("[查询用户信息](selectDemo)", 1);
            MarkdownLinkSpan markdownLinkSpan = (MarkdownLinkSpan) markdownHeaderBlock.getHeading();
            Assert.assertEquals("查询用户信息", markdownLinkSpan.getTitle());
            Assert.assertEquals("selectDemo", markdownLinkSpan.getUrl());
        }
        {
            MarkdownHeaderBlock markdownHeaderBlock = parser.parse("[](selectDemo)", 1);
            MarkdownLinkSpan markdownLinkSpan = (MarkdownLinkSpan) markdownHeaderBlock.getHeading();
            Assert.assertEquals("", markdownLinkSpan.getTitle());
            Assert.assertEquals("selectDemo", markdownLinkSpan.getUrl());
        }
        {
            MarkdownHeaderBlock markdownHeaderBlock = parser.parse("[查询用户信息]()", 1);
            MarkdownLinkSpan markdownLinkSpan = (MarkdownLinkSpan) markdownHeaderBlock.getHeading();
            Assert.assertEquals("查询用户信息", markdownLinkSpan.getTitle());
            Assert.assertEquals("", markdownLinkSpan.getUrl());
        }
    }
    @Test
    public void testParse2() throws Exception {
        MarkdownHeaderParser parser = new MarkdownHeaderParser();
        {
            MarkdownHeaderBlock markdownHeaderBlock = parser.parse("查询用户信息", 1);
            MarkdownTextSpan markdownTextSpan = (MarkdownTextSpan) markdownHeaderBlock.getHeading();
            Assert.assertEquals("查询用户信息", markdownTextSpan.getContent());
        }
    }
}