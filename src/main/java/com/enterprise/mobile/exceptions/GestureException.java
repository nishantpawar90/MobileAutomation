package com.enterprise.mobile.exceptions;

/**
 * Thrown when a mobile gesture operation fails.
 */
public class GestureException extends FrameworkException {

    public GestureException(String message) {
        super(message);
    }

    public GestureException(String message, Throwable cause) {
        super(message, cause);
    }
}
