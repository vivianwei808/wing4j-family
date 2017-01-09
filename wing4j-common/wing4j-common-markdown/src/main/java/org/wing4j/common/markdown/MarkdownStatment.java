package org.wing4j.common.markdown;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/9.
 */
@Data
public class MarkdownStatment {
    /**
     * 语句编号
     */
    String id;
    /**
     * 语句名称
     */
    String name;
    /**
     * 语句备注
     */
    String comment;
    /**
     * 是否必须刷新缓存
     */
    boolean flushCacheRequired;
    /**
     * 是否使用缓存
     */
    boolean useCache;
    /**
     *
     */
    int fetchSize;
    int timeout;
    final List<String> sqls = new ArrayList<>();
}
