package org.wing4j.orm.test;

public class TestRuntimeException extends RuntimeException{
    public TestRuntimeException() {
        super();
    }

    public TestRuntimeException(String message) {
        super(message);
    }

    public TestRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestRuntimeException(Throwable cause) {
        super(cause);
    }

    protected TestRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
