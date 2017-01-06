package org.wing4j.common.logtrack;

/**
 * Created by wing4j on 2017/1/6.
 */
public class SubError implements Error{
    /**
     * 错误码
     */
    String code;
    /**
     * 错误码描述
     */
    String desc;

    public SubError(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
