package com.enterprise.mobile.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * API response validator providing fluent assertion API.
 */
public class ApiValidator {

    private static final Logger logger = LogManager.getLogger(ApiValidator.class);
    private final Response response;

    public ApiValidator(Response response) {
        this.response = response;
    }

    public static ApiValidator validate(Response response) {
        return new ApiValidator(response);
    }

    @Step("Verify status code is {expectedCode}")
    public ApiValidator statusCode(int expectedCode) {
        int actualCode = response.getStatusCode();
        logger.debug("Verifying status code: expected={}, actual={}", expectedCode, actualCode);
        assertEquals(actualCode, expectedCode, "Unexpected status code");
        return this;
    }

    @Step("Verify response body contains field: {field}")
    public ApiValidator hasField(String field) {
        Object value = response.jsonPath().get(field);
        assertTrue(value != null, "Field '" + field + "' not found in response");
        return this;
    }

    @Step("Verify field {field} equals {expectedValue}")
    public ApiValidator fieldEquals(String field, Object expectedValue) {
        Object actualValue = response.jsonPath().get(field);
        assertEquals(actualValue, expectedValue,
                "Field '" + field + "' value mismatch");
        return this;
    }

    @Step("Verify response time is less than {maxMillis}ms")
    public ApiValidator responseTimeLessThan(long maxMillis) {
        long actualTime = response.getTime();
        assertTrue(actualTime < maxMillis,
                "Response time " + actualTime + "ms exceeds max " + maxMillis + "ms");
        return this;
    }

    @Step("Verify response body contains text: {text}")
    public ApiValidator bodyContains(String text) {
        assertTrue(response.getBody().asString().contains(text),
                "Response body does not contain: " + text);
        return this;
    }

    @Step("Verify response header {header} is present")
    public ApiValidator hasHeader(String header) {
        assertTrue(response.getHeader(header) != null,
                "Header '" + header + "' not found");
        return this;
    }

    public Response getResponse() {
        return response;
    }

    public <T> T getBodyAs(Class<T> clazz) {
        return response.getBody().as(clazz);
    }
}
