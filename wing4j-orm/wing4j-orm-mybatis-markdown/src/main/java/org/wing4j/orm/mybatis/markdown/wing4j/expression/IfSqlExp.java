package org.wing4j.orm.mybatis.markdown.wing4j.expression;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woate on 2017/1/11.
 */
@Data
public class IfSqlExp implements SqlExp{
    /**
     * ongl表达式
     */
    String onglExp;
    /**
     * 默认值
     */
    String defaultValue;
    /**
     * SQL表达式
     */
    final List<SqlExp> sqlExps = new ArrayList<>();
}
