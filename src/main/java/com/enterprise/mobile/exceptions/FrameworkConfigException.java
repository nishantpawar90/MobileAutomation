package com.enterprise.mobile.exceptions;

/**
 * Thrown when framework configuration is invalid or missing.
 */
public class FrameworkConfigException extends FrameworkException {

    public FrameworkConfigException(String message) {
        super(message);
    }

    public FrameworkConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
