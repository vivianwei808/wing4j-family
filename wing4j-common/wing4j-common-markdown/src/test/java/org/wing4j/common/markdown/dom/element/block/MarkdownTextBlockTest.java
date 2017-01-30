package org.wing4j.common.markdown.dom.element.block;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class MarkdownTextBlockTest {

    @Test
    public void testToString() throws Exception {
        MarkdownTextBlock textBlock = MarkdownTextBlock.builder().build();
        textBlock.addContent("1 line");
        textBlock.addContent("2 line");
        textBlock.addContent("3 line");
        System.out.println(textBlock.toString());
        Assert.assertEquals("1 line" + System.getProperty("line.separator") +
                "2 line"  + System.getProperty("line.separator") +
                "3 line", textBlock.toString());
    }
}