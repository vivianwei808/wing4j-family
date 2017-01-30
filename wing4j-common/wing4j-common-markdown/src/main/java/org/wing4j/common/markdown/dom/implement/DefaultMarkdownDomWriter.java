package org.wing4j.common.markdown.dom.implement;

import org.wing4j.common.markdown.dom.MarkdownDocument;
import org.wing4j.common.markdown.dom.MarkdownDomWriter;
import org.wing4j.common.markdown.dom.MarkdownNode;
import org.wing4j.common.markdown.dom.element.block.MarkdownCodeBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownHeaderBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownParagraphBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownTextBlock;

import java.io.*;

/**
 * Created by wing4j on 2017/1/30.
 * 默认实现的markdown文档模型保存
 */
public class DefaultMarkdownDomWriter implements MarkdownDomWriter {
    @Override
    public String writeAsText(MarkdownDocument dom) {
        StringBuilder buffer = new StringBuilder();
        for (MarkdownNode node : dom.getElements()) {
            if (node instanceof MarkdownTextBlock) {//将字符串区块转换为字符串形式
                MarkdownTextBlock textBlock = (MarkdownTextBlock) node;
                buffer.append(textBlock.toString()).append(System.getProperty("line.separator"));
            } else if (node instanceof MarkdownHeaderBlock) {//将标题元素转换为字符串形式
                MarkdownHeaderBlock headerBlock = (MarkdownHeaderBlock) node;
                buffer.append(headerBlock.toString()).append(System.getProperty("line.separator"));
            } else if (node instanceof MarkdownCodeBlock) {
                MarkdownCodeBlock codeBlock = (MarkdownCodeBlock) node;
                buffer.append(codeBlock.toString()).append(System.getProperty("line.separator"));
            } else if (node instanceof MarkdownParagraphBlock) {
                MarkdownParagraphBlock paragraphBlock = (MarkdownParagraphBlock) node;
                buffer.append(paragraphBlock.toString()).append(System.getProperty("line.separator"));
            }
        }
        return buffer.toString();
    }

    @Override
    public void writeAsMarkdown(MarkdownDocument dom, String file, String encoding) throws IOException {
        OutputStream os = new FileOutputStream(file);
        writeAsMarkdown(dom, os, encoding);
        os.close();
    }

    @Override
    public void writeAsMarkdown(MarkdownDocument dom, File file, String encoding) throws IOException {
        OutputStream os = new FileOutputStream(file);
        writeAsMarkdown(dom, os, encoding);
        os.close();
    }

    @Override
    public void writeAsMarkdown(MarkdownDocument dom, OutputStream os, String encoding) throws IOException {
        String markdown = writeAsText(dom);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        bos.write(markdown.getBytes(encoding));
        bos.flush();
    }
}
