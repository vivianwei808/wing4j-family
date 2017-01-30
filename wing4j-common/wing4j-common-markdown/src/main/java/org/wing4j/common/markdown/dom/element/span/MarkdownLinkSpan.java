package org.wing4j.common.markdown.dom.element.span;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by wing4j on 2017/1/30.
 * 链接元素
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarkdownLinkSpan extends AbstractMarkdownSpan{
    /**
     * 标题
     */
    String title;
    /**
     * 链接
     */
    String url;

    @Override
    public String toString() {
        return "[" + title + "](" + url + ")";
    }
}
