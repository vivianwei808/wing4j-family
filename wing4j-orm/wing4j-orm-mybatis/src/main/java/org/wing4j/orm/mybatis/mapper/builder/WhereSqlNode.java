package org.wing4j.orm.mybatis.mapper.builder;

import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.apache.ibatis.session.Configuration;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.utils.KeywordsUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wing4j on 2016/12/18.
 */
public class WhereSqlNode extends TrimSqlNode{
    private static List<String> prefixList = Arrays.asList("AND ", "OR ", "AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");
    public WhereSqlNode(Configuration configuration, SqlNode contents, WordMode keywordMode) {
        super(configuration, contents, KeywordsUtils.convert("WHERE", keywordMode), prefixList, null, null);
    }
}
