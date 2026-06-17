package com.enterprise.mobile.enterprise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enterprise.mobile.driver.DriverManager;
import com.enterprise.mobile.reporting.AllureReportManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

/**
 * Captures network logs during test execution for debugging.
 * Supports Android device network capture via performance logging.
 */
public final class NetworkLogCapture {

    private static final Logger logger = LogManager.getLogger(NetworkLogCapture.class);
    private static final ThreadLocal<List<Map<String, Object>>> networkLogs = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<Boolean> isCapturing = ThreadLocal.withInitial(() -> false);

    private NetworkLogCapture() {
    }

    /**
     * Starts network log capture.
     */
    public static void startCapture() {
        networkLogs.get().clear();
        isCapturing.set(true);
        logger.info("Network log capture started");
    }

    /**
     * Stops network log capture and returns collected logs.
     */
    public static List<Map<String, Object>> stopCapture() {
        isCapturing.set(false);
        List<Map<String, Object>> logs = new ArrayList<>(networkLogs.get());
        logger.info("Network log capture stopped. {} entries collected.", logs.size());
        return logs;
    }

    /**
     * Captures current network performance data.
     */
    public static void captureNetworkPerformance() {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver instanceof AndroidDriver androidDriver) {
                var perfData = androidDriver.execute("mobile: getPerformanceData",
                        Map.of("packageName", "com.example.app",
                                "dataType", "networkinfo"));
                if (perfData != null && perfData.getValue() != null) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("timestamp", System.currentTimeMillis());
                    entry.put("data", perfData.getValue());
                    networkLogs.get().add(entry);
                }
            }
        } catch (Exception e) {
            logger.debug("Network performance capture not available: {}", e.getMessage());
        }
    }

    /**
     * Generates a summary of network activity.
     */
    public static String generateNetworkSummary() {
        List<Map<String, Object>> logs = networkLogs.get();
        if (logs.isEmpty()) {
            return "No network logs captured.";
        }

        StringBuilder summary = new StringBuilder("=== Network Log Summary ===\n");
        summary.append("Total entries: ").append(logs.size()).append("\n");

        for (Map<String, Object> entry : logs) {
            summary.append("Timestamp: ").append(entry.get("timestamp")).append("\n");
            summary.append("Data: ").append(entry.get("data")).append("\n\n");
        }

        return summary.toString();
    }

    /**
     * Attaches network logs to Allure report.
     */
    public static void attachToReport() {
        String summary = generateNetworkSummary();
        AllureReportManager.attachNetworkLogs(summary);
    }

    public static void cleanup() {
        networkLogs.remove();
        isCapturing.remove();
    }
}
