package org.wing4j.common.markdown.dom.element.block;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 * Markdwon正文
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarkdownTextBlock extends AbstractMarkdownBlock {
    final List<String> contents = new ArrayList<>();

    /**
     * 增加内容
     * @param content 内容
     */
    public void addContent(String content){
        contents.add(content);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        int idx = 0;
        for (String line : contents){
            idx ++;
            buffer.append(line);
            if(idx < contents.size()){
                buffer.append(System.getProperty("line.separator"));
            }
        }
        return buffer.toString();
    }
}
