package com.enterprise.mobile.data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enterprise.mobile.config.FrameworkConstants;
import com.enterprise.mobile.exceptions.TestDataException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON test data provider using Jackson.
 * Supports loading test data from JSON files into POJOs or Maps.
 */
public final class JsonDataReader {

    private static final Logger logger = LogManager.getLogger(JsonDataReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonDataReader() {
    }

    /**
     * Reads JSON file and maps to the specified class.
     */
    public static <T> T readData(String fileName, Class<T> clazz) {
        String filePath = getFilePath(fileName);
        try {
            logger.debug("Reading JSON data from: {}", filePath);
            return objectMapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new TestDataException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Reads JSON file and returns as a list of maps.
     */
    public static List<Map<String, Object>> readDataAsList(String fileName) {
        String filePath = getFilePath(fileName);
        try {
            return objectMapper.readValue(new File(filePath),
                    new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new TestDataException("Failed to read JSON list from: " + filePath, e);
        }
    }

    /**
     * Reads JSON file and returns as a map.
     */
    public static Map<String, Object> readDataAsMap(String fileName) {
        String filePath = getFilePath(fileName);
        try {
            return objectMapper.readValue(new File(filePath),
                    new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new TestDataException("Failed to read JSON map from: " + filePath, e);
        }
    }

    /**
     * Reads a specific node from JSON file.
     */
    public static JsonNode readNode(String fileName, String nodePath) {
        String filePath = getFilePath(fileName);
        try {
            JsonNode root = objectMapper.readTree(new File(filePath));
            JsonNode node = root.at("/" + nodePath.replace(".", "/"));
            if (node.isMissingNode()) {
                throw new TestDataException("Node not found: " + nodePath + " in " + fileName);
            }
            return node;
        } catch (IOException e) {
            throw new TestDataException("Failed to read JSON node from: " + filePath, e);
        }
    }

    /**
     * Reads JSON and returns as list of typed objects.
     */
    public static <T> List<T> readDataAsList(String fileName, Class<T> clazz) {
        String filePath = getFilePath(fileName);
        try {
            return objectMapper.readValue(new File(filePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new TestDataException("Failed to read JSON list from: " + filePath, e);
        }
    }

    private static String getFilePath(String fileName) {
        if (fileName.startsWith("/") || fileName.contains(":")) {
            return fileName;
        }
        return FrameworkConstants.TEST_DATA_PATH + fileName;
    }
}
