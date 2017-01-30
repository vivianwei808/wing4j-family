package org.wing4j.common.markdown.dom;

import org.wing4j.common.markdown.dom.implement.DefaultMarkdownDomParser;
import org.wing4j.common.markdown.dom.implement.DefaultMarkdownDomWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wing4j on 2017/1/30.
 */
public class MarkdownDomWriterFactory {
    private static Map<String, Class> registers = new HashMap<>();
    /**
     * 默认实现
     */
    public static final String DEFAULT = "default";
    static {
        registers.put(DEFAULT, DefaultMarkdownDomWriter.class);
    }

    /**
     * 创建解析器
     * @param name 解析器实现名字
     * @return 解析器
     */
    public MarkdownDomWriter create(String name){
        if(!registers.containsKey(name)){
            throw new IllegalArgumentException("不支持的解析器实现");
        }
        Class clazz = registers.get(name);
        MarkdownDomWriter writer = null;
        try {
            writer = (MarkdownDomWriter) clazz.newInstance();
            return writer;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
