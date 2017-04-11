package com.luxoft.horizon.dir;

/**
 * Created by bogatp on 15.04.16.
 */
public class DirException extends Exception {

    public DirException() {
    }

    public DirException(String message) {
        super(message);
    }

    public DirException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirException(Throwable cause) {
        super(cause);
    }

    public DirException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
