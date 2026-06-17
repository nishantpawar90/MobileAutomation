package com.enterprise.mobile.exceptions;

/**
 * Thrown when test data loading or parsing fails.
 */
public class TestDataException extends FrameworkException {

    public TestDataException(String message) {
        super(message);
    }

    public TestDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
