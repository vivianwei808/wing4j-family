package org.wing4j.common.markdown;

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
    @Override
    public void parse(MarkdownContext ctx) {
        List<String> lines = read2list(ctx.getFile(), ctx.getFileEncoding());
        parse0(lines, ctx);
    }

    void parse0(List<String> lines, MarkdownContext ctx) {
        int lineNo = 0;
        boolean codeBlock = false;
        boolean globalParams = false;
        boolean params = false;
        MarkdownStatment statment = null;
        for (String line : lines) {
            line = line.trim();
            lineNo++;
            if(globalParams){
                if(line.startsWith("--")){
                    if(line.startsWith("--@dialect")){
                        String tempLine = line.substring("--@dialect".length()).trim();
                        log.info("解析到方言{}", tempLine);
                        ctx.setDialect(tempLine);
                    }else if(line.startsWith("--@namespace")){
                        String tempLine = line.substring("--@namespace".length()).trim();
                        log.info("解析到命名空间{}", tempLine);
                        ctx.setNamespace(tempLine);
                    }
                }
            }
            if(params){
                if(line.startsWith("--")){
                    if(line.startsWith("--@flushCacheRequired")){
                        String tempLine = line.substring("--@flushCacheRequired".length()).trim();
                        log.info("解析到刷新缓存{}", tempLine);
                        ctx.setDialect(tempLine);
                    }
                }
            }
            if (line.startsWith("```")) {
                if (!codeBlock) {
                    codeBlock = true;
                    log.info("代码块开始");
                    String tempLine = line.substring("```".length()).trim();
                    if (tempLine.startsWith("global params")) {
                        globalParams = true;
                    }else if(tempLine.startsWith("params")){
                        params = true;
                    }
                    continue;
                } else {
                    codeBlock = false;
                    if (globalParams) {
                        globalParams = false;
                    }
                    if (params) {
                        params = false;
                    }
                    log.info("代码块结束");
                    continue;
                }
            } else if (line.startsWith("==")) {
                log.info("越过标题行");
                continue;
            } else if (line.startsWith("[")) {
                log.info("解析语句块");
                int spidx = line.indexOf("](");
                String name = line.substring(1, spidx);
                String id = line.substring(spidx + "](".length(), line.lastIndexOf(")"));
                statment = new MarkdownStatment();
                statment.setId(id);
                statment.setName(name);
                System.out.println(statment);
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
