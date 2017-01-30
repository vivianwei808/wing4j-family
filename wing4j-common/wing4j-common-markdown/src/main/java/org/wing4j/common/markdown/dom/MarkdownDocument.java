package org.wing4j.common.markdown.dom;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 * 用于描述一个Markdown文件
 */
@Data
@Builder
@ToString
public class MarkdownDocument {
    /**
     * Markdown文件名
     */
    String fileName;
    /**
     * Markdown文件所在路径
     */
    String filePath;
    /**
     * 原始文件内容
     */
    final List<String> lines = new ArrayList<>();
    /**
     * Markdown文件解析出的元素
     */
    final List<MarkdownNode> elements = new ArrayList<>();

    /**
     * 按照行号获取原始内容
     * @param idx 行号
     * @return 原始内容
     */
    public String getLine(int idx){
        return lines.get(idx);
    }

    /**
     * 增加元素
     * @param element 元素对象
     */
    public void addElement(MarkdownNode element){
        elements.add(element);
    }
}
