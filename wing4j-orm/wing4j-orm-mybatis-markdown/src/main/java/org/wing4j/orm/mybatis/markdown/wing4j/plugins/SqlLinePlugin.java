package org.wing4j.orm.mybatis.markdown.wing4j.plugins;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.orm.mybatis.markdown.wing4j.LifeCycle;
import org.wing4j.orm.mybatis.markdown.wing4j.MarkdownContext;
import org.wing4j.orm.mybatis.markdown.wing4j.Plugin;
import org.wing4j.orm.mybatis.markdown.wing4j.RuntimeContext;
import org.wing4j.orm.mybatis.markdown.wing4j.expression.IfSqlExp;
import org.wing4j.orm.mybatis.markdown.wing4j.expression.StaticSqlExp;

/**
 * Created by woate on 2017/1/10.
 */
@Slf4j
public class SqlLinePlugin implements Plugin {
    @Override
    public boolean accpet(LifeCycle lifeCycle) {
        return lifeCycle == LifeCycle.SQL;
    }

    @Override
    public void execute(MarkdownContext markdownContext, RuntimeContext runtimeContext) {
        if(runtimeContext.statment.getType() == null
                && (runtimeContext.line.startsWith("select") || runtimeContext.line.startsWith("SELECT"))){
            runtimeContext.statment.setType("select");
        }else if(runtimeContext.statment.getType() == null
                && (runtimeContext.line.startsWith("update") || runtimeContext.line.startsWith("UPDATE"))){
            runtimeContext.statment.setType("update");
        }
        String line = runtimeContext.line;
        if(line.startsWith("/*#")){
            if(line.endsWith("*/")){
                int beginIdx = line.indexOf("/*#");
                int endIdx = line.indexOf("*/");
                String tempLine = line.substring(beginIdx + "/*#".length(), endIdx);
                if(tempLine.startsWith("if")){
                    tempLine = tempLine.substring("if".length()).trim();
                    runtimeContext.sqlExp = new IfSqlExp();
                    runtimeContext.statment.getSqls().add(runtimeContext.sqlExp);
                    IfSqlExp ifSqlExp = (IfSqlExp)runtimeContext.sqlExp;
                    ifSqlExp.setOnglExp(tempLine);
                    ifSqlExp.setDefaultValue(runtimeContext.defaultValue);
                }else if(tempLine.startsWith("fi")){//发现if代码块结束
                    log.debug("if 结束");
                }
            }else{
                log.error("无效行 {}", line);
                return;
            }
        }else{
            if(runtimeContext.sqlExp == null){
                runtimeContext.sqlExp = new StaticSqlExp();
                runtimeContext.statment.getSqls().add(runtimeContext.sqlExp);
            }
            if(runtimeContext.sqlExp instanceof StaticSqlExp){
                runtimeContext.sqlExp = new StaticSqlExp();
                runtimeContext.statment.getSqls().add(runtimeContext.sqlExp);
            }else if(runtimeContext.sqlExp instanceof IfSqlExp){
                IfSqlExp ifSqlExp = (IfSqlExp)runtimeContext.sqlExp;
                runtimeContext.sqlExp = new StaticSqlExp();
                StaticSqlExp sqlExp = (StaticSqlExp)runtimeContext.sqlExp;
                ifSqlExp.getSqlExps().add(sqlExp);
                sqlExp.getSqls().add(line.trim());
            }

        }
    }
}
