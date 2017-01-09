package org.wing4j.common.markdown;

import lombok.Data;
import lombok.ToString;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/9.
 */
@Data
@ToString
public class MarkdownContext {
    /**
     * 方言
     */
    String dialect;
    /**
     * 文件
     */
    URI file;
    /**
     * 文件编码
     */
    String fileEncoding;
    /**
     * 命名空间
     */
    String namespace;
    /**
     * 语句列表
     */
    final List<MarkdownStatment> statments = new ArrayList<>();
}
