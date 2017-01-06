package org.wing4j.common.logtrack;

/**
 * Created by woate on 2017/1/6.
 */
public interface ErrorContext extends Error {
    String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    ErrorContext code(String code);

    ErrorContext desc(String desc);

    ErrorContext activity(String format, Object... args);

    ErrorContext solution(String format, Object... args);

    ErrorContext message(String format, Object... args);

    ErrorContext addSubError(SubError error);

    ErrorContext cause(Throwable cause);

    ErrorContext reset();
}
