package org.wing4j.common.markdown.dom;

import java.io.File;
import java.net.URI;

/**
 * Created by wing4j on 2017/1/30.
 * Markdown DOM解析器
 */
public interface MarkdownDomParser {
    /**
     * 根据指定的字符串形式文件路径进行解析
     * @param file 文件路径
     * @param encoding 编码
     * @return Markdown DOM对象
     */
    MarkdownDocument parse(String file, String encoding);
    /**
     * 根据指定的文件对象形式文件路径进行解析
     * @param file 文件路径
     * @param encoding 编码
     * @return Markdown DOM对象
     */
    MarkdownDocument parse(File file, String encoding);
    /**
     * 根据指定的统一资源符形式文件路径进行解析
     * @param file 文件路径
     * @param encoding 编码
     * @return Markdown DOM对象
     */
    MarkdownDocument parse(URI file, String encoding);
}
