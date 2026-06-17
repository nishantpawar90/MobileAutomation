package com.enterprise.mobile.exceptions;

/**
 * Thrown when an element cannot be found after retries and self-healing attempts.
 */
public class ElementNotFoundException extends FrameworkException {

    private final String locatorStrategy;
    private final String locatorValue;

    public ElementNotFoundException(String message, String locatorStrategy, String locatorValue) {
        super(String.format("%s [Strategy: %s, Value: %s]", message, locatorStrategy, locatorValue));
        this.locatorStrategy = locatorStrategy;
        this.locatorValue = locatorValue;
    }

    public ElementNotFoundException(String message, String locatorStrategy, String locatorValue, Throwable cause) {
        super(String.format("%s [Strategy: %s, Value: %s]", message, locatorStrategy, locatorValue), cause);
        this.locatorStrategy = locatorStrategy;
        this.locatorValue = locatorValue;
    }

    public String getLocatorStrategy() {
        return locatorStrategy;
    }

    public String getLocatorValue() {
        return locatorValue;
    }
}
