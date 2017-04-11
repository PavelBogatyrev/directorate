package com.luxoft.horizon.dir.service.app;

import com.luxoft.horizon.dir.DirException;

/**
 * Created by pavelbogatyrev on 18/04/16.
 */
public class ExcelParserException extends DirException {
    public ExcelParserException() {
    }

    public ExcelParserException(String message) {
        super(message);
    }

    public ExcelParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelParserException(Throwable cause) {
        super(cause);
    }

    public ExcelParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
