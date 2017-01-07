package org.wing4j.common.logtrack;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/6.
 */
public class SimpleErrorContext implements ErrorContext {
    /**
     * 错误码
     */
    String code;
    /**
     * 错误码描述
     */
    String desc;
    /**
     * 正在进行的行为
     */
    PlaceHolder activty;
    /**
     * 发生错误信息说明
     */
    PlaceHolder message;
    /**
     * 解决方案
     */
    PlaceHolder solution;
    /**
     * 自错误信息
     */
    final List<Error> subErrors = new ArrayList<>();
    /**
     * 导致错误的异常原因
     */
    Throwable cause;

    @Override
    public ErrorContext code(String code) {
        this.code = code;
        return this;
    }

    @Override
    public ErrorContext desc(String desc) {
        this.desc = desc;
        return this;
    }

    @Override
    public ErrorContext activity(String format, Object... args) {
        if (this.activty == null) {
            this.activty = new PlaceHolder();
        }
        this.activty.format = format;
        this.activty.args = args;
        return this;
    }

    @Override
    public ErrorContext solution(String format, Object... args) {
        if (this.solution == null) {
            this.solution = new PlaceHolder();
        }
        this.solution.format = format;
        this.solution.args = args;
        return this;
    }

    @Override
    public ErrorContext message(String format, Object... args) {
        if (this.message == null) {
            this.message = new PlaceHolder();
        }
        this.message.format = format;
        this.message.args = args;
        return this;
    }

    @Override
    public ErrorContext addSubError(SubError error) {
        subErrors.add(error);
        return this;
    }

    @Override
    public ErrorContext cause(Throwable cause) {
        if (this.cause instanceof LogtrackRuntimeException) {
            //这里没必要，有可能导致死循环
        } else {
            this.cause = cause;
        }
        return this;
    }

    @Override
    public ErrorContext reset() {
        this.code = null;
        this.desc = null;
        this.activty = null;
        this.message = null;
        this.solution = null;
        this.subErrors.clear();
        this.cause = null;
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(200);
        builder.append("### =============================ErrorCotext=============================");
        if (this.code != null) {
            builder.append(LINE_SEPARATOR)
                    .append("### Error: ")
                    .append(this.desc).append("(").append(this.code).append(")");
        }
        if (this.subErrors != null && !this.subErrors.isEmpty()) {
            for (Error subError : this.subErrors) {
                builder.append(LINE_SEPARATOR)
                        .append("### SubErrors: ")
                        .append(subError.getDesc())
                        .append("(").append(subError.getCode()).append(")");
            }

        }
        if (this.activty != null) {
            builder.append(LINE_SEPARATOR)
                    .append("### Activty: ").append(this.activty);
        }
        if (this.message != null) {
            builder.append(LINE_SEPARATOR)
                    .append("### Message: ").append(this.message);
        }
        if (this.solution != null) {
            builder.append(LINE_SEPARATOR)
                    .append("### Solution: ").append(this.solution);
        }
        if (this.cause != null) {
            builder.append(LINE_SEPARATOR)
                    .append("### Cause: ").append(this.cause);
        }
        return builder.toString();
    }
}
