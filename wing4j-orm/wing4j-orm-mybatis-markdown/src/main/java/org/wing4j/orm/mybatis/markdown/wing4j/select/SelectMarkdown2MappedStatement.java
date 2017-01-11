package org.wing4j.orm.mybatis.markdown.wing4j.select;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.wing4j.orm.mybatis.markdown.wing4j.Markdown2MappedStatement;
import org.wing4j.orm.mybatis.markdown.wing4j.MarkdownStatment;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by woate on 2017/1/10.
 */
public class SelectMarkdown2MappedStatement implements Markdown2MappedStatement{
    /**
     * 配置对象
     */
    protected Configuration config;
    /**
     * 命名空间
     */
    protected String namespace;
    @Override
    public MappedStatement convert(MarkdownStatment markdownStatment) {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
//        List<SQLException> lines = markdownStatment.getSqls();
//        for (int i = 0; i < lines.size(); i++) {
//            SQLException line = lines.get(i);
//            if(line.startsWith("/*#")){
//                int beginIdx = line.indexOf("/*#");
//                int endIdx = line.indexOf("*/");
//                String tempLine = line.substring(beginIdx, endIdx);
//                if(tempLine.startsWith("def")){
//                    int defIdx = line.indexOf("def");
//                    String defVal = line.substring(defIdx);
//                    i++;
//                    String sql = lines.get(i);
//                    //提取变量
//                    int beginPlaceholdIdx = sql.indexOf("#{");
//                    int endPlaceholdIdx = sql.indexOf("}", beginPlaceholdIdx);
//                    String beginSql = sql.substring(0, beginPlaceholdIdx);
//                    String endSql = sql.substring(endPlaceholdIdx);
//                    String placeHold = sql.substring(beginPlaceholdIdx, endPlaceholdIdx);
//
//                }
//            }
//        }
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + markdownStatment.getId(), null, SqlCommandType.SELECT);
        return null;
    }
}
