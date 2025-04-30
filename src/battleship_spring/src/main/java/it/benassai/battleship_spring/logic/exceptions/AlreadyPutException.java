package it.benassai.logic.exceptions;

public class AlreadyPutException extends RuntimeException {

    public AlreadyPutException() {
    }

    public AlreadyPutException(String message) {
        super(message);
    }

    public AlreadyPutException(Throwable cause) {
        super(cause);
    }

    public AlreadyPutException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyPutException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    
}
