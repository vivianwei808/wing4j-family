package org.wing4j.common.markdown.dom.element.block;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wing4j.common.markdown.dom.MarkdownBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 * 含有一个或多个连续的文本行
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarkdownParagraphBlock extends AbstractMarkdownBlock {
    /**
     * 当前段落包含的元素
     */
    final List<MarkdownBlock> elements = new ArrayList<>();

    /**
     * 增加元素
     * @param element 元素对象
     */
    public void addElement(MarkdownBlock element){
        elements.add(element);
    }

    public String toString(){
        StringBuilder buffer = new StringBuilder(System.getProperty("line.separator"));
        int idx = 0;
        for (MarkdownBlock markdownBlock : elements){
            idx ++;
            buffer.append(markdownBlock.toString());
            if(idx < elements.size()){
                buffer.append(System.getProperty("line.separator"));
            }
        }
        return buffer.toString();
    }
}
