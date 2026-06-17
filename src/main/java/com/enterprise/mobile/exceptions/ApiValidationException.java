package com.enterprise.mobile.exceptions;

/**
 * Thrown when API validation fails.
 */
public class ApiValidationException extends FrameworkException {

    private final int statusCode;
    private final String endpoint;

    public ApiValidationException(String message, int statusCode, String endpoint) {
        super(String.format("%s [Endpoint: %s, Status: %d]", message, endpoint, statusCode));
        this.statusCode = statusCode;
        this.endpoint = endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
