package org.wing4j.common.markdown.dom.element.span;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by wing4j on 2017/1/30.
 * 文本元素，用于表示一个单行文本元素，区别与MarkdownTextBlock
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarkdownTextSpan  extends AbstractMarkdownSpan{
    /**
     * 内容
     */
    String content;

    @Override
    public String toString() {
        return content;
    }
}
