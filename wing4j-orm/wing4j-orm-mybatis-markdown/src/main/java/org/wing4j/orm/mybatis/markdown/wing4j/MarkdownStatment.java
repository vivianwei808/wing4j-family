package org.wing4j.orm.mybatis.markdown.wing4j;

import lombok.Data;
import org.wing4j.orm.mybatis.markdown.wing4j.expression.SqlExp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/9.
 */
@Data
public class MarkdownStatment {
    String type;
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
    /**
     * 超时设置
     */
    int timeout;

    final List<SqlExp> sqls = new ArrayList<>();
}
