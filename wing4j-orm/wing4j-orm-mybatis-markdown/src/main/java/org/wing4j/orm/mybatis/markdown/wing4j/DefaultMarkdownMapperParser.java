package org.wing4j.orm.mybatis.markdown.wing4j;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.wing4j.common.markdown.dom.*;
import org.wing4j.common.markdown.dom.element.block.MarkdownCodeBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownHeaderBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownParagraphBlock;
import org.wing4j.common.markdown.dom.element.span.MarkdownLinkSpan;
import org.wing4j.orm.mybatis.markdown.wing4j.utils.Wing4jParamUtils;
import org.wing4j.orm.mybatis.markdown.wing4j.utils.Wing4jScriptUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 */
@Slf4j
public class DefaultMarkdownMapperParser implements MarkdownMapperParser {
    MarkdownDomParser parser = new MarkdownDomParserFactory().create(MarkdownDomParserFactory.DEFAULT);

    @Override
    public void register(Plugin plugin) {

    }

    @Override
    public List<MappedStatement> parse(MarkdownContext ctx) {
        List<MappedStatement> mappedStatments = new ArrayList<>();
        MarkdownDocument dom = parser.parse(ctx.getFile(), ctx.getFileEncoding());
        List<MarkdownNode> elements = new ArrayList<>();
        for (MarkdownNode node : dom.getElements()){
            if (node instanceof MarkdownHeaderBlock) {
                elements.add(node);
            } else if (node instanceof MarkdownCodeBlock) {
                elements.add(node);
            } else if (node instanceof MarkdownParagraphBlock) {
                MarkdownParagraphBlock paragraphBlock = (MarkdownParagraphBlock)node;
                for (MarkdownNode node0 : paragraphBlock.getElements()){
                    elements.add(node0);
                }
            }
        }
        boolean statement = false;
        MappedStatementRuntimeContext runtimeCtx = new MappedStatementRuntimeContext();
        for (int idx = 0; idx < elements.size(); idx++) {
            MarkdownNode node = elements.get(idx);
            if (node instanceof MarkdownHeaderBlock) {
                MarkdownHeaderBlock headerBlock = (MarkdownHeaderBlock) node;
                statement = true;//开始新的语句
                if (headerBlock.getHeading() instanceof MarkdownLinkSpan) {
                    MarkdownLinkSpan markdownLinkSpan = (MarkdownLinkSpan) headerBlock.getHeading();
                    runtimeCtx.setId(markdownLinkSpan.getUrl());
                } else {
                    throw new IllegalArgumentException("语法错误");
                }
            } else if (node instanceof MarkdownCodeBlock) {
                MarkdownCodeBlock codeBlock = (MarkdownCodeBlock) node;
                //Wing4j 配置对象
                if (codeBlock.getLanguage() == CodeLanguage.WING4J_CONFIGURE) {
                    List<String> codeLines = codeBlock.getCodes();
                    for (String line : codeLines) {
                        if(Wing4jParamUtils.ifParam(line)){
                            Wing4jParamUtils.ParamValue paramValue = Wing4jParamUtils.trimParam(line);
                            if ("namespace".equals(paramValue.getName())) {
                                ctx.setNamespace(paramValue.getValue());
                            }
                        }

                    }
                }
                //Wing4j 参数配置
                if (codeBlock.getLanguage() == CodeLanguage.WING4J_PARAM) {
                    if (!statement) {
                        throw new IllegalArgumentException("无效的Markdown文件");
                    }
                    List<String> codeLines = codeBlock.getCodes();
                    for (String line : codeLines) {
                        if(Wing4jParamUtils.ifComment(line)){
                            continue;
                        }
                        Wing4jParamUtils.ParamValue paramValue = Wing4jParamUtils.trimParam(line);
                        if ("flushCacheRequired".equals(paramValue.getName())) {
                            runtimeCtx.setFlushCacheRequired(Boolean.valueOf(paramValue.getValue()));
                        }
                        if ("useCache".equals(paramValue.getName())) {
                            runtimeCtx.setUseCache(Boolean.valueOf(paramValue.getValue()));
                        }
                        if ("fetchSize".equals(paramValue.getName())) {
                            runtimeCtx.setFetchSize(Integer.valueOf(paramValue.getValue()));
                        }
                        if ("timeout".equals(paramValue.getName())) {
                            runtimeCtx.setTimeout(Integer.valueOf(paramValue.getValue()));
                        }
                        if ("paramEntity".equals(paramValue.getName())) {
                            try {
                                runtimeCtx.setParamEntity(Class.forName(paramValue.getValue()));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if ("resultEntity".equals(paramValue.getName())) {
                            try {
                                runtimeCtx.setResultEntity(Class.forName(paramValue.getValue()));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (codeBlock.getLanguage() == CodeLanguage.SQL) {
                    if (!statement) {
                        throw new IllegalArgumentException("无效的Markdown文件");
                    }
                    if(runtimeCtx.getId().startsWith("select")){
                        runtimeCtx.setSqlCommandType(SqlCommandType.SELECT);
                    }else if(runtimeCtx.getId().startsWith("update")){
                        runtimeCtx.setSqlCommandType(SqlCommandType.UPDATE);
                    }else if(runtimeCtx.getId().startsWith("insert")){
                        runtimeCtx.setSqlCommandType(SqlCommandType.INSERT);
                    }else if(runtimeCtx.getId().startsWith("delete")){
                        runtimeCtx.setSqlCommandType(SqlCommandType.DELETE);
                    }else{
                        //TODO 按照wing4j markdown规范来
                    }
                    List<String> codeLines = codeBlock.getCodes();
                    for (int i = 0; i < codeLines.size(); i++) {
                        String line = codeLines.get(i);
                        if (Wing4jScriptUtils.ifScript(line)) {
                            if (runtimeCtx.sqlBuffer != null) {//在进入其他类型的SQL节点前处理缓存的SQL语句
                                StaticTextSqlNode sqlNode = new StaticTextSqlNode(runtimeCtx.sqlBuffer.toString());
                                runtimeCtx.sqlNodes.add(sqlNode);
                                runtimeCtx.sqlBuffer = null;
                            }
                            String ifScript = Wing4jScriptUtils.trimScript(line);
                            if (ifScript.startsWith("if")) {
                                List<String> ifContents = new ArrayList<>();
                                i++;
                                while ((line = codeLines.get(i)) != null && !Wing4jScriptUtils.trimScript(line).equals("fi") && i < codeLines.size()) {
                                    ifContents.add(line);
                                    i++;
                                }
                                //TODO 将ifContent转换成SqlNode
                                String ognl = Wing4jScriptUtils.trimIf(ifScript);
                                IfSqlNode ifSqlNode = new IfSqlNode(new LinesToStaticTextSqlNodeConvertor().convert(ifContents), ognl);
                                runtimeCtx.sqlNodes.add(ifSqlNode);
                            }
                            //TODO 将脚本处理
                        } else if (Wing4jParamUtils.ifComment(line)) {
                            //TODO 注释跳过
                        } else {//原生SQL语句
                            if (runtimeCtx.sqlBuffer == null) {
                                runtimeCtx.sqlBuffer = new StringBuilder();
                            }
                            runtimeCtx.sqlBuffer.append(line).append(" ");
                        }
                    }
                    if (runtimeCtx.sqlBuffer != null) {//在进入其他类型的SQL节点前处理缓存的SQL语句
                        StaticTextSqlNode sqlNode = new StaticTextSqlNode(runtimeCtx.sqlBuffer.toString());
                        runtimeCtx.sqlNodes.add(sqlNode);
                        runtimeCtx.sqlBuffer = null;
                    }
                    //创建一个MappedStatement建造器
                    DynamicSqlSource sqlSource = new DynamicSqlSource(ctx.getConfig(), new MixedSqlNode(runtimeCtx.getSqlNodes()));
                    MappedStatement.Builder msBuilder = new MappedStatement.Builder(ctx.getConfig(), ctx.getNamespace() + "." + runtimeCtx.getId(), sqlSource, runtimeCtx.getSqlCommandType());
                    if(runtimeCtx.getSqlCommandType() == SqlCommandType.SELECT){
                        msBuilder.flushCacheRequired(runtimeCtx.isFlushCacheRequired());
                        msBuilder.useCache(runtimeCtx.isUseCache());
                        msBuilder.fetchSize(runtimeCtx.getFetchSize());
                        msBuilder.timeout(runtimeCtx.getTimeout());
                    }
                    //参数映射
                    ParameterMap.Builder paramBuilder = new ParameterMap.Builder(ctx.getConfig(), "defaultParameterMap", runtimeCtx.getParamEntity(), runtimeCtx.getParameterMappings());
                    msBuilder.parameterMap(paramBuilder.build());
                    //结果映射
                    ResultMap.Builder resultBuilder = new ResultMap.Builder(ctx.getConfig(), "BaseResultMap", runtimeCtx.getResultEntity(), runtimeCtx.getResultMappings());
                    List<ResultMap> resultMaps = new ArrayList<ResultMap>();
                    resultMaps.add(resultBuilder.build());
                    msBuilder.resultMaps(resultMaps);
                    //建造出MappedStatement
                    MappedStatement ms = msBuilder.build();
                    mappedStatments.add(ms);
                    runtimeCtx.getSqlNodes().clear();
                    runtimeCtx.setId(null);
                    runtimeCtx.setSqlCommandType(SqlCommandType.UNKNOWN);
                    runtimeCtx.setResultEntity(null);
                    runtimeCtx.setParamEntity(null);
                    runtimeCtx.setSqlBuffer(null);
                    runtimeCtx.setTimeout(0);
                    runtimeCtx.setUseCache(false);
                    runtimeCtx.setFetchSize(0);
                    runtimeCtx.setFlushCacheRequired(false);
                    runtimeCtx.getParameterMappings().clear();
                    runtimeCtx.getResultMappings().clear();
                }
            }
        }
        return mappedStatments;
    }
}
