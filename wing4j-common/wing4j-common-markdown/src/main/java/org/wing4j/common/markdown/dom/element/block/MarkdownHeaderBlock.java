package org.wing4j.common.markdown.dom.element.block;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wing4j.common.markdown.dom.MarkdownSpan;

/**
 * Created by wing4j on 2017/1/30.
 * Markdown标题
 * Setext和Atx
 * Setext 利用 = （最高阶标题）和 - （第二阶标题）
 * Atx 在行首插入 1 到 6 个 # ，对应到标题 1 到 6 阶
 *
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarkdownHeaderBlock extends AbstractMarkdownBlock {
    /**
     * 标题级数
     */
    int level;
    /**
     * 标题名称
     */
    MarkdownSpan heading;

    @Override
    public String toString() {
        String title = heading.toString();
        String fit = "";
        for (int i = 0; i < level; i++) {
            fit += "#";
        }
        return fit + title + fit;
    }
}
