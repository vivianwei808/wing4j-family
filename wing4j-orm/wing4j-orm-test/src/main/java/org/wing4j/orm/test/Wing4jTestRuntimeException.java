package org.wing4j.orm.test;

public class Wing4jTestRuntimeException extends RuntimeException{
    public Wing4jTestRuntimeException() {
        super();
    }

    public Wing4jTestRuntimeException(String message) {
        super(message);
    }

    public Wing4jTestRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public Wing4jTestRuntimeException(Throwable cause) {
        super(cause);
    }

    protected Wing4jTestRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
