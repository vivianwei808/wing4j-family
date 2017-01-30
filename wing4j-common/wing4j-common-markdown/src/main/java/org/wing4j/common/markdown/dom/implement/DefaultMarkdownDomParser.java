package org.wing4j.common.markdown.dom.implement;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.markdown.dom.CodeLanguage;
import org.wing4j.common.markdown.dom.MarkdownDocument;
import org.wing4j.common.markdown.dom.MarkdownDomParser;
import org.wing4j.common.markdown.dom.element.block.MarkdownCodeBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownHeaderBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownParagraphBlock;
import org.wing4j.common.markdown.dom.element.block.MarkdownTextBlock;
import org.wing4j.common.markdown.utils.IoUtils;
import static org.wing4j.common.markdown.dom.MarkdownNode.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 */
@Slf4j
public class DefaultMarkdownDomParser implements MarkdownDomParser {
    @Override
    public MarkdownDocument parse(String file, String encoding) {
        return null;
    }

    @Override
    public MarkdownDocument parse(File file, String encoding) {
        return null;
    }

    @Override
    public MarkdownDocument parse(URI file, String encoding) {
        List<String> lines = null;
        try {
            lines = IoUtils.read2list(file.toURL().openStream(), encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MarkdownDocument document = MarkdownDocument.builder()
                .fileName(file.getPath())
                .filePath(file.getPath())
                .build();
        document.getLines().addAll(lines);
        MarkdownCodeBlock codeBlock = null;
        MarkdownParagraphBlock paragraphBlock = null;
        MarkdownTextBlock textBlock = null;
        boolean codeBlockIndicator = false;
        MarkdownHeaderParser markdownHeaderParser = new MarkdownHeaderParser();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String nextLine = null;
            if (i + 1 < lines.size()) {
                nextLine = lines.get(i + 1);
            }
            log.debug("parse lineNo {}:{}", i + 1, line);
            if (line == null) {
                continue;
            }
            String trimLine = line.trim();
            //寻找是否为代码块
            if (!codeBlockIndicator && line.startsWith(CODE_BEGIN)) {//如果代码块指示器不是真，同时出现```引导，则说明代码块开始
                codeBlock = null;
                textBlock = null;
                log.debug("代码块开始");
                int lastIdx = trimLine.lastIndexOf("`") + 1;
                String lang = null;
                if (lastIdx >= trimLine.length()) {
                    lang = null;
                } else {
                    lang = trimLine.substring(lastIdx);
                }
                log.debug("代码块指定语言字符串:{}", lang);
                CodeLanguage codeLanguage = CodeLanguage.valueOfCode(lang.toLowerCase());
                log.debug("代码块语言:{}", codeLanguage);
                codeBlock = MarkdownCodeBlock.builder().language(codeLanguage).build();
                codeBlock.addLineNo(i + 1);
                if (paragraphBlock == null) {
                    document.addElement(codeBlock);
                } else {
                    paragraphBlock.addElement(codeBlock);
                }
                codeBlockIndicator = true;
                continue;
            }
            if (codeBlockIndicator && line.startsWith(CODE_END)) {//如果代码块指示器是真，同时出现```引导，则说明代码块结束
                log.debug("代码块结束");
                codeBlock.addLineNo(i + 1);
                codeBlockIndicator = false;
                continue;
            }
            if (codeBlockIndicator) {//是代码块的时候
                codeBlock.addLineNo(i + 1);
                codeBlock.getCodes().add(line);
                continue;
            }
            //不是代码块的时候
            if (line.startsWith(Character.toString(HEADING_ATX_BEGIN))) {
                codeBlock = null;
                textBlock = null;
                log.debug("parse heading :{}", line);
                char[] chars = line.toCharArray();
                int level = 0;
                String content = "";
                for (int j = 0; j < chars.length; j++) {
                    if (chars[j] != HEADING_ATX_END) {
                        level = j;
                        content = line.substring(j);
                        int titleEndIdx = content.indexOf(HEADING_ATX_END);
                        content = content.substring(0, titleEndIdx);
                        break;
                    }
                }
                MarkdownHeaderBlock headerBlock = markdownHeaderParser.parse(content, level);
                headerBlock.addLineNo(i + 1);
                if (paragraphBlock == null) {
                    document.addElement(headerBlock);
                } else {
                    paragraphBlock.addElement(headerBlock);
                }
                continue;
            }
            if (nextLine.startsWith(Character.toString(HEADING_SETEXT_1_BEGIN))//如果是=开始就是第一级标题
                    || nextLine.startsWith(Character.toString(HEADING_SETEXT_2_BEGIN))) {//如果是-开始就是第一级标题
                codeBlock = null;
                textBlock = null;
                log.debug("parse heading :{}", line);
                int level = nextLine.startsWith(Character.toString(HEADING_SETEXT_1_BEGIN)) ? 1 : 2;
                String content = line;
                MarkdownHeaderBlock headerBlock = markdownHeaderParser.parse(content, level);
                headerBlock.addLineNo(i + 1);
                headerBlock.addLineNo(i + 2);
                if (paragraphBlock == null) {
                    document.addElement(headerBlock);
                } else {
                    paragraphBlock.addElement(headerBlock);
                }
                i++;//由于下一行是=或者-,所以应该跳过
                continue;
            }

            if (trimLine.isEmpty()) {
                codeBlock = null;
                textBlock = null;
                paragraphBlock = MarkdownParagraphBlock.builder().build();
                paragraphBlock.addLineNo(i + 1);
                document.addElement(paragraphBlock);
                continue;
            } else {
                codeBlock = null;
                if (textBlock == null) {
                    textBlock = MarkdownTextBlock.builder().build();
                    if (paragraphBlock == null) {
                        document.addElement(textBlock);
                    } else {
                        paragraphBlock.addElement(textBlock);
                    }
                }
                textBlock.addLineNo(i + 1);
                textBlock.addContent(line);
            }
        }
        return document;
    }
}
