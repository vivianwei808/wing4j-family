package org.wing4j.common.markdown.dom;

import lombok.Data;

/**
 * Created by wing4j on 2017/1/30.
 * 代码语言
 */
public enum CodeLanguage {
    Java("java", "Java语言"),
    Go("go", "go语言"),
    C("c", "c语言"),
    CPP("c++", "C++语言"),
    Xml("xml", "xml"),
    Markdown("markdown", "markdown"),
    JSON("json", "json"),
    SQL("sql", "sql语句"),
    WING4J_CONFIGURE("wing4j configure", "wing4j配置"),
    WING4J_PARAM("wing4j param", "wing4j参数"),
    UNDEF("", "未定义");

    String code;
    String desc;
    CodeLanguage(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CodeLanguage valueOfCode(String code){
        CodeLanguage[] values = values();
        for (CodeLanguage value : values){
            if(value.code.equals(code)){
                return value;
            }
        }
        return UNDEF;
    }
}
