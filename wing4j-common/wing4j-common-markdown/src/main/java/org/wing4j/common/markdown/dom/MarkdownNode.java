package org.wing4j.common.markdown.dom;

/**
 * Created by wing4j on 2017/1/30.
 * Markdown节点接口
 */
public interface MarkdownNode {
    String CODE_BEGIN = "```";
    String CODE_END = "```";
    char HEADING_SETEXT_1_BEGIN = '=';
    char HEADING_SETEXT_1_END = '=';
    char HEADING_SETEXT_2_BEGIN = '-';
    char HEADING_SETEXT_2_END = '-';
    char HEADING_ATX_BEGIN = '#';
    char HEADING_ATX_END = '#';
}
