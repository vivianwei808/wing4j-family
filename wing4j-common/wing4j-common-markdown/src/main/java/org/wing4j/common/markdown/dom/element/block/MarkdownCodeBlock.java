package org.wing4j.common.markdown.dom.element.block;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wing4j.common.markdown.dom.CodeLanguage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 * Markdown代码
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarkdownCodeBlock extends AbstractMarkdownBlock {
    /**
     * 代码语言
     */
    CodeLanguage language;
    /**
     * 代码
     */
    final List<String> codes = new ArrayList<>();

    /**
     * 增加一行代码
     * @param code 代码行
     */
    public void addLine(String code){
        codes.add(code);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(CODE_BEGIN).append(language.getCode()).append(System.getProperty("line.separator"));
        for (String line : codes){
            buffer.append(line).append(System.getProperty("line.separator"));
        }
        buffer.append(CODE_END);
        return buffer.toString();
    }
}
