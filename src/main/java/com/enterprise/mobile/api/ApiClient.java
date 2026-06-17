package com.enterprise.mobile.api;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enterprise.mobile.config.ConfigManager;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * REST Assured based API client for backend validation.
 * Used to verify API state alongside mobile UI tests.
 */
public final class ApiClient {

    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private final String baseUrl;
    private String authToken;

    public ApiClient() {
        this.baseUrl = ConfigManager.getInstance().getApiBaseUrl();
        RestAssured.baseURI = baseUrl;
    }

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    /**
     * Performs GET request.
     */
    public Response get(String endpoint) {
        logger.info("GET {}", endpoint);
        return buildRequest()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Performs GET request with query parameters.
     */
    public Response get(String endpoint, Map<String, Object> queryParams) {
        logger.info("GET {} with params: {}", endpoint, queryParams);
        return buildRequest()
                .queryParams(queryParams)
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Performs POST request with JSON body.
     */
    public Response post(String endpoint, Object body) {
        logger.info("POST {}", endpoint);
        return buildRequest()
                .contentType(ContentType.JSON)
                .body(body)
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Performs PUT request with JSON body.
     */
    public Response put(String endpoint, Object body) {
        logger.info("PUT {}", endpoint);
        return buildRequest()
                .contentType(ContentType.JSON)
                .body(body)
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Performs DELETE request.
     */
    public Response delete(String endpoint) {
        logger.info("DELETE {}", endpoint);
        return buildRequest()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Performs PATCH request.
     */
    public Response patch(String endpoint, Object body) {
        logger.info("PATCH {}", endpoint);
        return buildRequest()
                .contentType(ContentType.JSON)
                .body(body)
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    private RequestSpecification buildRequest() {
        RequestSpecification spec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);

        if (authToken != null && !authToken.isBlank()) {
            spec.header("Authorization", "Bearer " + authToken);
        }

        return spec;
    }
}
