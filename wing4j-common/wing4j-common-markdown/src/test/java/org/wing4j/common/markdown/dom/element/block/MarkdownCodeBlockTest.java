package org.wing4j.common.markdown.dom.element.block;

import org.junit.Assert;
import org.junit.Test;
import org.wing4j.common.markdown.dom.CodeLanguage;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class MarkdownCodeBlockTest {

    @Test
    public void testToString() throws Exception {
        MarkdownCodeBlock codeBlock = MarkdownCodeBlock.builder().language(CodeLanguage.Java).build();
        codeBlock.addLine("hello world!");
        System.out.println(codeBlock.toString());
        Assert.assertEquals("```java" + System.getProperty("line.separator") +
                "hello world!" + System.getProperty("line.separator") +
                "```", codeBlock.toString());
    }
}