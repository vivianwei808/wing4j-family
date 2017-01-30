package org.wing4j.common.markdown.dom.implement;

import org.wing4j.common.markdown.dom.MarkdownSpan;
import org.wing4j.common.markdown.dom.element.block.MarkdownHeaderBlock;
import org.wing4j.common.markdown.dom.element.span.MarkdownLinkSpan;
import org.wing4j.common.markdown.dom.element.span.MarkdownTextSpan;

/**
 * Created by wing4j on 2017/1/30.
 * Markdown标题解析器
 */
class MarkdownHeaderParser {
    public MarkdownHeaderBlock parse(String content, int level) {
        MarkdownSpan heading = null;
        int idx1 = content.indexOf("]");
        int idx2 = content.indexOf("(", idx1);
        //如果满足[]()格式的字符串，则说明是链接元素
        if (content.startsWith("[") && content.endsWith(")") && idx1 < idx2) {
            String title = content.substring(1, idx1);
            String url = content.substring(idx2 + 1, content.length() - 1);
            heading = MarkdownLinkSpan.builder().title(title).url(url).build();
        } else {//否则使用文本元素
            heading = MarkdownTextSpan.builder().content(content).build();
        }
        MarkdownHeaderBlock headerBlock = MarkdownHeaderBlock.builder()
                .level(level)
                .heading(heading)
                .build();
        return headerBlock;
    }
}
