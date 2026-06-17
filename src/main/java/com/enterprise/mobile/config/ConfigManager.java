package com.enterprise.mobile.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enterprise.mobile.enums.Environment;
import com.enterprise.mobile.enums.ExecutionMode;
import com.enterprise.mobile.enums.Platform;
import com.enterprise.mobile.exceptions.FrameworkConfigException;

/**
 * Centralized configuration manager. Loads properties based on environment
 * and provides type-safe access to all configuration values.
 * Thread-safe singleton implementation.
 */
public final class ConfigManager {

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static volatile ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadConfiguration();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private void loadConfiguration() {
        try {
            // Load base config
            loadPropertiesFile(FrameworkConstants.CONFIG_FILE);

            // Load environment-specific config (overrides base)
            String env = getSystemPropertyOrDefault("environment",
                    properties.getProperty("environment", "QA"));
            String envConfigFile = FrameworkConstants.CONFIG_PATH + env.toLowerCase() + ".properties";
            loadPropertiesFile(envConfigFile);

            // System properties override file properties
            applySystemPropertyOverrides();

            logger.info("Configuration loaded successfully for environment: {}", env);
        } catch (Exception e) {
            throw new FrameworkConfigException("Failed to load framework configuration", e);
        }
    }

    private void loadPropertiesFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            logger.debug("Loaded properties from: {}", filePath);
        } catch (IOException e) {
            logger.warn("Properties file not found: {}. Using defaults.", filePath);
        }
    }

    private void applySystemPropertyOverrides() {
        System.getProperties().forEach((key, value) -> {
            if (key.toString().startsWith("fw.")) {
                String propKey = key.toString().substring(3);
                properties.setProperty(propKey, value.toString());
                logger.debug("System property override: {} = {}", propKey, value);
            }
        });
    }

    private String getSystemPropertyOrDefault(String key, String defaultValue) {
        String sysValue = System.getProperty("fw." + key);
        return sysValue != null ? sysValue : defaultValue;
    }

    // ===== Type-safe Getters =====

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value.trim()) : defaultValue;
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value.trim()) : defaultValue;
    }

    // ===== Framework-specific Getters =====

    public Platform getPlatform() {
        String platform = getSystemPropertyOrDefault("platform",
                properties.getProperty("platform", "ANDROID"));
        return Platform.valueOf(platform.toUpperCase());
    }

    public ExecutionMode getExecutionMode() {
        String mode = getSystemPropertyOrDefault("execution.mode",
                properties.getProperty("execution.mode", "LOCAL"));
        return ExecutionMode.valueOf(mode.toUpperCase());
    }

    public Environment getEnvironment() {
        String env = getSystemPropertyOrDefault("environment",
                properties.getProperty("environment", "QA"));
        return Environment.valueOf(env.toUpperCase());
    }

    public String getAppiumServerUrl() {
        return getProperty("appium.server.url", "http://127.0.0.1:4723");
    }

    public String getBrowserStackUrl() {
        return getProperty("browserstack.url");
    }

    public String getBrowserStackUsername() {
        return Objects.requireNonNull(
                SecretManager.getInstance().getSecret("BROWSERSTACK_USERNAME"),
                "BrowserStack username not configured");
    }

    public String getBrowserStackAccessKey() {
        return Objects.requireNonNull(
                SecretManager.getInstance().getSecret("BROWSERSTACK_ACCESS_KEY"),
                "BrowserStack access key not configured");
    }

    public int getExplicitWait() {
        return getIntProperty("explicit.wait", FrameworkConstants.DEFAULT_EXPLICIT_WAIT);
    }

    public int getImplicitWait() {
        return getIntProperty("implicit.wait", FrameworkConstants.DEFAULT_IMPLICIT_WAIT);
    }

    public int getRetryCount() {
        return getIntProperty("retry.count", FrameworkConstants.DEFAULT_RETRY_COUNT);
    }

    public boolean isVideoRecordingEnabled() {
        return getBooleanProperty("video.recording", false);
    }

    public String getApiBaseUrl() {
        return getProperty("api.base.url", "https://api.example.com");
    }

    public String getMongoConnectionString() {
        return getProperty("mongodb.connection.string", "mongodb://localhost:27017");
    }

    public String getMongoDatabase() {
        return getProperty("mongodb.database", "test_automation");
    }

    public boolean isParallelEnabled() {
        return getBooleanProperty("parallel.enabled", false);
    }

    public int getParallelThreadCount() {
        return getIntProperty("parallel.thread.count", 3);
    }

    /**
     * Reset for testing purposes only.
     */
    public static void reset() {
        instance = null;
    }
}
