package org.wing4j.orm.mybatis.markdown.wing4j;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by woate on 2017/1/9.
 */
@Slf4j
public class Wing4jMarkdownParser implements MarkdownParser {
    public static final String DIALECT = "--@dialect";
    public static final String NAMESPACE = "--@namespace";
    public static final String PREFIX = "--";
    public static final String FLUSH_CACHE_REQUIRED = "--@flushCacheRequired";
    public static final String USE_CACHE = "--@useCache";
    public static final String FETCH_SIZE = "--@fetchSize";
    public static final String TIMEOUT = "--@timeout";
    public static final String COMMENT = "--@comment";
    public static final String CODE = "```";
    public static final String CONFIGURE = "configure";
    public static final String PARAMS = "params";
    public static final String SQL = "sql";
    public static final String TITLE = "==";
    public static final String SQL_NAME_BEGIN = "[";
    public static final String SQL_NAME_END = "]";
    public static final String SQL_ID_BEGIN = "(";
    public static final String SQL_ID_END = ")";
    final List<Plugin> plugins = new ArrayList<>();

    @Override
    public void register(Plugin plugin) {
        plugins.add(plugin);
    }

    @Override
    public void parse(MarkdownContext ctx) {
        List<String> lines = read2list(ctx.getFile(), ctx.getFileEncoding());
        parse0(lines, ctx, new RuntimeContext());
    }

    void parse0(List<String> lines, MarkdownContext ctx, RuntimeContext runtimeContext) {
        for (String line : lines) {
            runtimeContext.originalLine = line;
            runtimeContext.lineNo++;
            if (line == null) {
                log.debug("解析到非法空行，行号:{} , 内容'{}'", runtimeContext.lineNo, line);
            }
            line = line.trim();
            if (runtimeContext.configure) {
                if (line.startsWith(PREFIX)) {
                    if (line.startsWith(DIALECT)) {
                        String tempLine = line.substring(DIALECT.length() + 1).trim();
                        log.debug("解析到方言'{}'", tempLine);
                        ctx.setDialect(tempLine);
                    } else if (line.startsWith(NAMESPACE)) {
                        String tempLine = line.substring(NAMESPACE.length() + 1).trim();
                        log.debug("解析到命名空间'{}'", tempLine);
                        ctx.setNamespace(tempLine);
                    } else {
                        String tempLine = line.substring(PREFIX.length()).trim();
                        log.debug("解析到非法配置，行号:{} , 内容'{}'", runtimeContext.lineNo, tempLine);
                    }
                }
            }
            if (runtimeContext.params) {
                if (line.startsWith(PREFIX)) {
                    if (line.startsWith(FLUSH_CACHE_REQUIRED)) {
                        String tempLine = line.substring(FLUSH_CACHE_REQUIRED.length() + 1).trim();
                        log.debug("解析到刷新缓存'{}'", tempLine);
                        runtimeContext.statment.setFlushCacheRequired(Boolean.valueOf(tempLine));
                    } else if (line.startsWith(USE_CACHE)) {
                        String tempLine = line.substring(USE_CACHE.length() + 1).trim();
                        log.debug("解析到使用缓存'{}'", tempLine);
                        runtimeContext.statment.setUseCache(Boolean.valueOf(tempLine));
                    } else if (line.startsWith(FETCH_SIZE)) {
                        String tempLine = line.substring(FETCH_SIZE.length() + 1).trim();
                        log.debug("解析到使用缓存'{}'", tempLine);
                        runtimeContext.statment.setFetchSize(Integer.valueOf(tempLine));
                    } else if (line.startsWith(TIMEOUT)) {
                        String tempLine = line.substring(TIMEOUT.length() + 1).trim();
                        log.debug("解析到超时时间'{}'", tempLine);
                        runtimeContext.statment.setTimeout(Integer.valueOf(tempLine));
                    } else if (line.startsWith(COMMENT)) {
                        String tempLine = line.substring(COMMENT.length() + 1).trim();
                        log.debug("解析到备注'{}'", tempLine);
                        runtimeContext.statment.setComment(tempLine);
                    } else {
                        String tempLine = line.substring(PREFIX.length()).trim();
                        log.debug("解析到非法参数，行号:{} , 内容'{}'", runtimeContext.lineNo, tempLine);
                    }
                }
            }
            if (line.startsWith(CODE)) {
                if (!runtimeContext.codeBlock) {
                    runtimeContext.codeBlock = true;
                    log.debug("解析到代码块开始'{}'", line);
                    String tempLine = line.substring(CODE.length()).trim();
                    log.debug("解析到代码块'{}'", tempLine);
                    if (tempLine.startsWith(CONFIGURE)) {
                        runtimeContext.configure = true;
                    } else if (tempLine.startsWith(PARAMS)) {
                        runtimeContext.params = true;
                    } else if (tempLine.startsWith(SQL)) {
                        runtimeContext.sql = true;
                        runtimeContext.sqlExp = null;
                    }
                } else if (runtimeContext.codeBlock) {
                    runtimeContext.codeBlock = false;
                    if (runtimeContext.configure) {
                        runtimeContext.configure = false;
                    } else if (runtimeContext.params) {
                        runtimeContext.params = false;
                    } else if (runtimeContext.sql) {
                        runtimeContext.sql = false;
                    }
                    log.debug("解析到代码块结束'{}'", line);
                }
            } else if (line.startsWith(TITLE)) {
                log.debug("解析到标题行'{}'", line);
            } else if (line.startsWith(SQL_NAME_BEGIN)) {
                if (runtimeContext.statment != null) {
                    runtimeContext.statment = null;
                }
                if (runtimeContext.statment == null) {
                    runtimeContext.statment = new MarkdownStatment();
                    ctx.getStatments().add(runtimeContext.statment);
                }
                log.debug("解析到语句块'{}'", line);
                int nameIdx = line.indexOf(SQL_NAME_END);
                String name = line.substring(1, nameIdx);
                String tempLine = line.substring(nameIdx + 1);
                int idIdx = tempLine.indexOf(SQL_ID_BEGIN);
                String id = tempLine.substring(idIdx + 1, tempLine.lastIndexOf(SQL_ID_END));
                runtimeContext.statment.setId(id);
                runtimeContext.statment.setName(name);
            } else {
                if (line.isEmpty()) {
                    if (runtimeContext.configure) {
                        log.debug("SQL MAPPER文件配置空行'{}'", line);
                    } else if (runtimeContext.params) {
                        log.debug("解析到语句参数空行'{}'", line);
                    } else if (runtimeContext.sql) {
                        log.debug("解析到SQL语句空行'{}'", line);
                    } else {
                        log.debug("解析到未知区域空行'{}'", line);
                    }
                }
                if (runtimeContext.sql) {
                    runtimeContext.line = line;
                    log.debug("解析到SQL语句代码'{}'", line);
                    for (Plugin plugin : plugins) {
                        if (plugin.accpet(LifeCycle.SQL)) {
                            plugin.execute(ctx, runtimeContext);
                        }
                    }

                }
            }
        }
    }

    /**
     * 将markdown文件读取成列表
     *
     * @param uri      地址对象
     * @param encoding 文件编码
     * @return 文本列表
     * @throws IOException 一场
     */
    List<String> read2list(URI uri, String encoding) {
        List<String> lines = new ArrayList<>();
        InputStream is = null;
        try {
            is = uri.toURL().openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return lines;
    }
}
