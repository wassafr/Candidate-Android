package com.smora.arch.webserver;

public class WebServerException extends Exception {

    public WebServerException() {
    }

    public WebServerException(String message) {
        super(message);
    }

    public WebServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebServerException(Throwable cause) {
        super(cause);
    }

    public WebServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
