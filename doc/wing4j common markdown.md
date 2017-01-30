#wing4j common Markdown组件#
## markdown解析

该组件提供对markdown文件的dom方式解析。

主要的类有：

```java
org.wing4j.common.markdown.dom.MarkdownDocument;
org.wing4j.common.markdown.dom.MarkdownDomParserFactory;
org.wing4j.common.markdown.dom.MarkdownDomParser;
org.wing4j.common.markdown.dom.MarkdownNode;
org.wing4j.common.markdown.dom.MarkdownSpan;
org.wing4j.common.markdown.dom.MarkdownBlock;
```

核心类为

```java
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
     * @return Markdown DOM对象
     */
    MarkdownDocument parse(String file, String encoding);
    /**
     * 根据指定的文件对象形式文件路径进行解析
     * @param file 文件路径
     * @return Markdown DOM对象
     */
    MarkdownDocument parse(File file, String encoding);
    /**
     * 根据指定的统一资源符形式文件路径进行解析
     * @param file 文件路径
     * @return Markdown DOM对象
     */
    MarkdownDocument parse(URI file, String encoding);
}
```

通过调用MarkdownDomParser接口的实现类解析获取Markdown文档模型类MarkdownDocument。然后根据MarkdownDocument中的getElements()方法遍历元素。元素分为两类，一类是区块元素，一类是区段元素。

详细的两类元素参见[Markdown 语法说明 (简体中文版)](http://www.appinn.com/markdown/index.html)

## markdown文档对象保存

核心类为

```java
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
```

可以通过该接口的实现类，实现将Markdown文档模型对象转换为markdown文本。