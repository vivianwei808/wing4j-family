package org.wing4j.orm.entity.exception;

import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.common.logtrack.ErrorContext;

/**
 * Created by wing4j on 2017/1/7.
 * 用于Entity模块的日志跟踪
 */
public class OrmEntityRuntimeException extends LogtrackRuntimeException {
    public OrmEntityRuntimeException(String code, String desc, String activity, String message, String solution, Throwable cause) {
        super(code, desc, activity, message, solution, cause);
    }

    public OrmEntityRuntimeException(ErrorContext ctx) {
        super(ctx);
    }
}
