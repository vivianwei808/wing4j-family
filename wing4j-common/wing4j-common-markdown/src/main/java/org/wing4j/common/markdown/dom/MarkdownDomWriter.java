package org.wing4j.common.markdown.dom;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wing4j on 2017/1/30.
 * Markdown文件文档模型保存文件
 */
public interface MarkdownDomWriter {
    /**
     * 将Markdown文档对象保存到字符串中
     * @param dom 文档对象
     * @return markdown字符串
     */
    String writeAsText(MarkdownDocument dom);
    /**
     * 将Markdown文档对象保存到文件
     * @param dom Markdown文档对象
     * @param file 字符串形式的文件名
     */
    void writeAsMarkdown(MarkdownDocument dom, String file, String encoding) throws IOException;
    /**
     * 将Markdown文档对象保存到文件
     * @param dom Markdown文档对象
     * @param file 字符串形式的文件名
     */
    void writeAsMarkdown(MarkdownDocument dom, File file, String encoding) throws IOException;
    /**
     * 将Markdown文档对象保存到文件
     * @param dom Markdown文档对象
     * @param os 输出流
     */
    void writeAsMarkdown(MarkdownDocument dom, OutputStream os, String encoding) throws IOException;
}
