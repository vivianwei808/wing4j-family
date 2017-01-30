package org.wing4j.orm.mybatis.markdown.wing4j;

import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;

import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 */
public class LinesToStaticTextSqlNodeConvertor {
    public SqlNode convert(List<String> lines){
        StringBuilder sqlBuffer = new StringBuilder();
        for (int i = 0; i < lines.size() ; i++) {
            String line = lines.get(i);
            sqlBuffer.append(line).append(" ");
        }
        return new StaticTextSqlNode(sqlBuffer.toString());
    }
}
