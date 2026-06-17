package com.enterprise.mobile.driver;

import java.util.HashMap;
import java.util.Map;

import com.enterprise.mobile.config.ConfigManager;

/**
 * Manages BrowserStack-specific capabilities for device farm integration.
 */
public final class BrowserStackCapabilities {

    private static final ConfigManager config = ConfigManager.getInstance();

    private BrowserStackCapabilities() {
    }

    public static Map<String, Object> getAndroidCapabilities() {
        Map<String, Object> bstackOptions = getCommonCapabilities();
        bstackOptions.put("deviceName", config.getProperty("android.device.name", "Google Pixel 6"));
        bstackOptions.put("osVersion", config.getProperty("android.platform.version", "13.0"));
        bstackOptions.put("os", "android");
        return bstackOptions;
    }

    public static Map<String, Object> getAndroidCapabilities(DeviceConfig deviceConfig) {
        Map<String, Object> bstackOptions = getCommonCapabilities();
        bstackOptions.put("deviceName", deviceConfig.getBrowserStackDevice() != null
                ? deviceConfig.getBrowserStackDevice()
                : deviceConfig.getDeviceName());
        bstackOptions.put("osVersion", deviceConfig.getOsVersion() != null
                ? deviceConfig.getOsVersion()
                : deviceConfig.getPlatformVersion());
        bstackOptions.put("os", "android");
        return bstackOptions;
    }

    public static Map<String, Object> getIOSCapabilities() {
        Map<String, Object> bstackOptions = getCommonCapabilities();
        bstackOptions.put("deviceName", config.getProperty("ios.device.name", "iPhone 14"));
        bstackOptions.put("osVersion", config.getProperty("ios.platform.version", "16"));
        bstackOptions.put("os", "ios");
        return bstackOptions;
    }

    public static Map<String, Object> getIOSCapabilities(DeviceConfig deviceConfig) {
        Map<String, Object> bstackOptions = getCommonCapabilities();
        bstackOptions.put("deviceName", deviceConfig.getBrowserStackDevice() != null
                ? deviceConfig.getBrowserStackDevice()
                : deviceConfig.getDeviceName());
        bstackOptions.put("osVersion", deviceConfig.getOsVersion() != null
                ? deviceConfig.getOsVersion()
                : deviceConfig.getPlatformVersion());
        bstackOptions.put("os", "ios");
        return bstackOptions;
    }

    private static Map<String, Object> getCommonCapabilities() {
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", config.getBrowserStackUsername());
        bstackOptions.put("accessKey", config.getBrowserStackAccessKey());
        bstackOptions.put("projectName", config.getProperty("browserstack.project.name", "Mobile Automation"));
        bstackOptions.put("buildName",
                config.getProperty("browserstack.build.name", "Build-" + System.currentTimeMillis()));
        bstackOptions.put("sessionName", "Test-" + Thread.currentThread().getName());
        bstackOptions.put("debug", true);
        bstackOptions.put("networkLogs", true);
        bstackOptions.put("video", config.isVideoRecordingEnabled());
        bstackOptions.put("appiumVersion", "2.4.1");
        bstackOptions.put("idleTimeout", 300);
        return bstackOptions;
    }
}
