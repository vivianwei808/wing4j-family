package org.wing4j.common.logtrack;

/**
 * Created by wing4j on 2017/1/6.
 */
public class ErrorContextFactory {
    static ThreadLocal<ErrorContext> CURRUNT_CTX = new ThreadLocal<>();

    public static ErrorContext instance() {
        ErrorContext ctx = CURRUNT_CTX.get();
        if (ctx == null) {
            ctx = new SimpleErrorContext();
            CURRUNT_CTX.set(ctx);
        }
        return ctx;
    }
}
