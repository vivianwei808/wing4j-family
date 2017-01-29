package org.wing4j.orm.mybatis.markdown.wing4j.expression;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woate on 2017/1/11.
 * 静态SQL语句
 */
@Data
public class StaticSqlExp implements SqlExp{
    /**
     * sql语句
     */
    final List<String> sqls = new ArrayList<>();
    public StaticSqlExp(){

    }
    public StaticSqlExp(String line){
        sqls.add(line);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (String line : sqls){
            sb.append(line).append("\n");
        }
        return sb.toString().trim();
    }
}
