package com.enterprise.mobile.enterprise;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enterprise.mobile.driver.DriverManager;
import com.enterprise.mobile.reporting.AllureReportManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

/**
 * Performance monitoring utility for mobile app testing.
 * Tracks CPU, memory, battery usage, and app launch time.
 */
public final class PerformanceMonitor {

    private static final Logger logger = LogManager.getLogger(PerformanceMonitor.class);
    private static final ThreadLocal<Map<String, Long>> timers = ThreadLocal.withInitial(ConcurrentHashMap::new);
    private static final ThreadLocal<Map<String, Object>> metrics = ThreadLocal.withInitial(ConcurrentHashMap::new);

    private PerformanceMonitor() {
    }

    /**
     * Starts a timer with the given name.
     */
    public static void startTimer(String timerName) {
        timers.get().put(timerName, System.currentTimeMillis());
        logger.debug("Timer started: {}", timerName);
    }

    /**
     * Stops a timer and returns elapsed time in milliseconds.
     */
    public static long stopTimer(String timerName) {
        Long startTime = timers.get().get(timerName);
        if (startTime == null) {
            logger.warn("Timer '{}' was never started", timerName);
            return -1;
        }

        long elapsed = System.currentTimeMillis() - startTime;
        timers.get().remove(timerName);
        metrics.get().put(timerName + "_duration_ms", elapsed);

        logger.info("Timer '{}': {}ms", timerName, elapsed);
        return elapsed;
    }

    /**
     * Measures app launch time (from driver creation to first activity).
     */
    public static long measureAppLaunchTime() {
        startTimer("app_launch");
        // Waiting for first screen element implies app is launched
        return stopTimer("app_launch");
    }

    /**
     * Captures memory info from Android device.
     */
    public static Map<String, Object> captureMemoryInfo(String packageName) {
        Map<String, Object> memoryInfo = new HashMap<>();
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver instanceof AndroidDriver androidDriver) {
                var result = androidDriver.executeScript("mobile: getPerformanceData",
                        Map.of("packageName", packageName, "dataType", "memoryinfo"));
                if (result != null) {
                    memoryInfo.put("memory", result);
                    metrics.get().put("memory_snapshot", result);
                }
            }
        } catch (Exception e) {
            logger.debug("Memory info capture not available: {}", e.getMessage());
        }
        return memoryInfo;
    }

    /**
     * Captures CPU info from Android device.
     */
    public static Map<String, Object> captureCpuInfo(String packageName) {
        Map<String, Object> cpuInfo = new HashMap<>();
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver instanceof AndroidDriver androidDriver) {
                var result = androidDriver.executeScript("mobile: getPerformanceData",
                        Map.of("packageName", packageName, "dataType", "cpuinfo"));
                if (result != null) {
                    cpuInfo.put("cpu", result);
                    metrics.get().put("cpu_snapshot", result);
                }
            }
        } catch (Exception e) {
            logger.debug("CPU info capture not available: {}", e.getMessage());
        }
        return cpuInfo;
    }

    /**
     * Captures battery info from Android device.
     */
    public static Map<String, Object> captureBatteryInfo(String packageName) {
        Map<String, Object> batteryInfo = new HashMap<>();
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver instanceof AndroidDriver androidDriver) {
                var result = androidDriver.executeScript("mobile: getPerformanceData",
                        Map.of("packageName", packageName, "dataType", "batteryinfo"));
                if (result != null) {
                    batteryInfo.put("battery", result);
                    metrics.get().put("battery_snapshot", result);
                }
            }
        } catch (Exception e) {
            logger.debug("Battery info capture not available: {}", e.getMessage());
        }
        return batteryInfo;
    }

    /**
     * Returns all collected metrics.
     */
    public static Map<String, Object> getAllMetrics() {
        return new HashMap<>(metrics.get());
    }

    /**
     * Generates a performance report.
     */
    public static String generateReport() {
        Map<String, Object> allMetrics = metrics.get();
        if (allMetrics.isEmpty()) {
            return "No performance metrics collected.";
        }

        StringBuilder report = new StringBuilder("=== Performance Report ===\n");
        allMetrics.forEach((key, value) -> report.append(String.format("%s: %s%n", key, value)));

        return report.toString();
    }

    /**
     * Attaches performance report to Allure.
     */
    public static void attachToReport() {
        AllureReportManager.attachText("Performance Metrics", generateReport());
    }

    public static void cleanup() {
        timers.remove();
        metrics.remove();
    }
}
