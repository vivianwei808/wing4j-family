package org.wing4j.orm.mybatis.markdown.wing4j;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.session.Configuration;

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
     * MyBatis配置对象
     */
    Configuration config;
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
}
