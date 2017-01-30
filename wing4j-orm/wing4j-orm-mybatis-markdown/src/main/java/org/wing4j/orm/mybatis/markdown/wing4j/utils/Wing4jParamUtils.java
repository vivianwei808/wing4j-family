package org.wing4j.orm.mybatis.markdown.wing4j.utils;

import static org.wing4j.orm.mybatis.markdown.wing4j.MarkdownMapperConstants.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class Wing4jParamUtils {
    /**
     * 参数值对象
     */
    public static class ParamValue {
        String name;
        String value;

        public ParamValue(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 检查是否是脚本行
     *
     * @param line 文本行
     * @return 如果是脚本行，则返回真
     */
    public static boolean ifParam(String line) {
        return line.startsWith(PARAM_PREFIX);
    }

    /**
     * 获取去除后空格的参数对象
     * @param line 文本行
     * @return 参数对象
     */
    public static ParamValue trimParam(String line) {
        String temp = line.substring(PARAM_PREFIX.length());
        int commentIdx = temp.indexOf(LINE_COMMENT);
        if(commentIdx > -1){
            temp = temp.substring(0, commentIdx);
        }
        int idx1 = temp.indexOf("=");
        int idx2 = temp.lastIndexOf("=");
        if (idx1 != idx2) {
            //TODO 处理不一致的问题
        }
        String name = temp.substring(0, idx1).trim();
        String value = temp.substring(idx2 + 1).trim();
        ParamValue paramValue = new ParamValue(name, value);
        return paramValue;
    }

    /**
     * 检查是否备注
     *
     * @param line 文本行
     * @return 如果是备注行，则返回真
     */
    public static boolean ifComment(String line) {
        if (ifParam(line)) {
            int index = line.indexOf(LINE_COMMENT);
            if (index > PARAM_PREFIX.length()) {//至少是参数引导符的长度
                return true;
            } else {
                return false;
            }
        } else {
            return line.startsWith(COMMENT_PREFIX);
        }
    }

    /**
     * 去除脚本标签
     *
     * @param line 文本行
     * @return 去除脚本标签文本行
     */
    public static String trimComment(String line) {
        if (ifParam(line)) {
            int index = line.indexOf(LINE_COMMENT);
            if(index > -1){
                return line.substring(index + LINE_COMMENT.length());
            }else{
                return "";
            }
        } else {
            return line.substring(COMMENT_PREFIX.length());
        }
    }
}
