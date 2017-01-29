package org.wing4j.orm.mybatis.markdown.wing4j.select;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.wing4j.orm.mybatis.markdown.wing4j.Markdown2MappedStatement;
import org.wing4j.orm.mybatis.markdown.wing4j.MarkdownStatment;
import org.wing4j.orm.mybatis.markdown.wing4j.expression.IfSqlExp;
import org.wing4j.orm.mybatis.markdown.wing4j.expression.SqlExp;
import org.wing4j.orm.mybatis.markdown.wing4j.expression.StaticSqlExp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woate on 2017/1/10.
 */
public class SelectMarkdown2MappedStatement implements Markdown2MappedStatement {
    /**
     * 配置对象
     */
    protected Configuration config;

    @Override
    public MappedStatement convert(MarkdownStatment markdownStatment) {
//        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        List<SqlExp> lines = markdownStatment.getSqls();
        List<SqlNode> sqlNodes = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            SqlExp sqlExp = lines.get(i);
            if (sqlExp instanceof StaticSqlExp) {
                StaticSqlExp sqlExp1 = (StaticSqlExp) sqlExp;
                sqlNodes.add(new StaticTextSqlNode(sqlExp1.toString()));
            } else if (sqlExp instanceof IfSqlExp) {
                IfSqlExp ifSqlExp = (IfSqlExp) sqlExp;
                List<SqlExp> sqlExps = ifSqlExp.getSqlExps();
                StaticSqlExp sqlExp1 = (StaticSqlExp) sqlExps.get(0);
                StaticTextSqlNode staticTextSqlNode = new StaticTextSqlNode(sqlExp1.toString());
                IfSqlNode ifSqlNode = new IfSqlNode(staticTextSqlNode, ifSqlExp.getOnglExp());
                sqlNodes.add(ifSqlNode);
            }
        }
        String id = markdownStatment.getNamespace() + "." + markdownStatment.getId();
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, id, null, SqlCommandType.SELECT);
        return null;
    }
}
