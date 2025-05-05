package it.benassai.battleship_spring.logic.exceptions;

public class AlreadyShotException extends RuntimeException {

    public AlreadyShotException() {
    }

    public AlreadyShotException(String message) {
        super(message);
    }

    public AlreadyShotException(Throwable cause) {
        super(cause);
    }

    public AlreadyShotException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyShotException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    
}
