package com.enterprise.mobile.config;

/**
 * Framework-wide constants. Single source of truth for all constant values.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation
    }

    // Paths
    public static final String CONFIG_PATH = "src/main/resources/config/";
    public static final String CONFIG_FILE = CONFIG_PATH + "config.properties";
    public static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    public static final String APPS_PATH = "src/test/resources/apps/";
    public static final String REPORT_PATH = "test-output/reports/";
    public static final String SCREENSHOT_PATH = "test-output/screenshots/";
    public static final String VIDEO_PATH = "test-output/videos/";
    public static final String ALLURE_RESULTS_PATH = "target/allure-results/";
    public static final String LOCATOR_REPOSITORY_PATH = "src/main/resources/locators/";

    // Timeouts
    public static final int DEFAULT_EXPLICIT_WAIT = 30;
    public static final int DEFAULT_IMPLICIT_WAIT = 10;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 60;
    public static final int DEFAULT_POLLING_INTERVAL = 500;
    public static final int SHORT_WAIT = 5;
    public static final int MEDIUM_WAIT = 15;
    public static final int LONG_WAIT = 45;

    // Retry
    public static final int DEFAULT_RETRY_COUNT = 0;
    public static final int ELEMENT_RETRY_COUNT = 3;

    // Report
    public static final String EXTENT_REPORT_NAME = "Mobile Automation Report";
    public static final String EXTENT_REPORT_TITLE = "Test Execution Report";

    // Date Format
    public static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    // Encryption
    public static final String ENCRYPTION_ALGORITHM = "AES";
    public static final int ENCRYPTION_KEY_SIZE = 256;
}
