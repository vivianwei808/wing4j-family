package org.wing4j.common.markdown.dom.element.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wing4j.common.markdown.dom.MarkdownBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 */
@Data
@ToString
@EqualsAndHashCode
public abstract class AbstractMarkdownBlock implements MarkdownBlock {
    /**
     * 代码对应行号
     */
    final List<Integer> lineNumbers = new ArrayList<>();

    /**
     * 增加行号
     * @param lineNo 行号
     */
    public void addLineNo(int lineNo){
        lineNumbers.add(lineNo);
    }
}
