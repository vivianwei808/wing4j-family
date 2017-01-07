package org.wing4j.common.logtrack;

/**
 * Created by wing4j on 2017/1/6.
 * 日志跟踪异常
 */
public class LogtrackRuntimeException extends RuntimeException {
    ErrorContext ctx;

    public LogtrackRuntimeException(String code, String desc, String activity, String message, String solution, Throwable cause) {
        this(ErrorContextFactory.instance().code(code).desc(desc).activity(activity).message(message).solution(solution).cause(cause));
    }

    public LogtrackRuntimeException(ErrorContext ctx) {
        this.ctx = ctx;
        if (this.ctx == null) {
            this.ctx = ErrorContextFactory.instance();
        }
    }

    @Override
    public String toString() {
        return this.ctx == null ? null : this.ctx.toString();
    }
}
