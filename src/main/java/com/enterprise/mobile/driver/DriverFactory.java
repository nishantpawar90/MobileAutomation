package com.enterprise.mobile.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enterprise.mobile.config.ConfigManager;
import com.enterprise.mobile.enums.ExecutionMode;
import com.enterprise.mobile.enums.Platform;
import com.enterprise.mobile.exceptions.DriverInitializationException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

/**
 * Factory for creating Appium driver instances.
 * Supports Local, BrowserStack, and SauceLabs execution modes.
 * Follows Factory Method pattern for driver creation.
 */
public final class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    private DriverFactory() {
    }

    /**
     * Initializes and returns an AppiumDriver based on configuration.
     */
    public static AppiumDriver createDriver() {
        Platform platform = config.getPlatform();
        ExecutionMode mode = config.getExecutionMode();

        logger.info("Initializing driver - Platform: {}, Mode: {}", platform, mode);

        AppiumDriver driver = switch (mode) {
            case LOCAL -> createLocalDriver(platform);
            case BROWSERSTACK -> createBrowserStackDriver(platform);
            case SAUCELABS -> createSauceLabsDriver(platform);
        };

        configureTimeouts(driver);
        DriverManager.setDriver(driver);

        logger.info("Driver initialized successfully for {} on {}", platform, mode);
        return driver;
    }

    /**
     * Creates driver with specific device capabilities (for parallel execution).
     */
    public static AppiumDriver createDriver(DeviceConfig deviceConfig) {
        ExecutionMode mode = config.getExecutionMode();
        Platform platform = deviceConfig.getPlatform();

        logger.info("Creating driver for device: {} ({})", deviceConfig.getDeviceName(), platform);

        AppiumDriver driver = switch (mode) {
            case LOCAL -> createLocalDriver(deviceConfig);
            case BROWSERSTACK -> createBrowserStackDriver(deviceConfig);
            case SAUCELABS -> createSauceLabsDriver(deviceConfig);
        };

        configureTimeouts(driver);
        DriverManager.setDriver(driver);

        return driver;
    }

    // ===== Local Driver Creation =====

    private static AppiumDriver createLocalDriver(Platform platform) {
        return switch (platform) {
            case ANDROID -> createLocalAndroidDriver();
            case IOS -> createLocalIOSDriver();
        };
    }

    private static AppiumDriver createLocalDriver(DeviceConfig deviceConfig) {
        return switch (deviceConfig.getPlatform()) {
            case ANDROID -> createLocalAndroidDriver(deviceConfig);
            case IOS -> createLocalIOSDriver(deviceConfig);
        };
    }

    private static AndroidDriver createLocalAndroidDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(config.getProperty("android.device.name"))
                .setPlatformVersion(config.getProperty("android.platform.version"))
                .setApp(System.getProperty("user.dir") + "/" + config.getProperty("android.app.path"))
                .setAppPackage(config.getProperty("android.app.package"))
                .setAppActivity(config.getProperty("android.app.activity"))
                .setAutomationName(config.getProperty("android.automation.name", "UiAutomator2"))
                .setAutoGrantPermissions(true)
                .setNoReset(false)
                .setFullReset(false);

        options.setCapability("appium:noSign", true);

        return new AndroidDriver(getAppiumServerUrl(), options);
    }

    private static AndroidDriver createLocalAndroidDriver(DeviceConfig deviceConfig) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(deviceConfig.getDeviceName())
                .setPlatformVersion(deviceConfig.getPlatformVersion())
                .setApp(System.getProperty("user.dir") + "/" + config.getProperty("android.app.path"))
                .setAppPackage(config.getProperty("android.app.package"))
                .setAppActivity(config.getProperty("android.app.activity"))
                .setAutomationName("UiAutomator2")
                .setAutoGrantPermissions(true)
                .setNoReset(false)
                .setFullReset(false)
                .setUdid(deviceConfig.getUdid())
                .setSystemPort(deviceConfig.getSystemPort());

        options.setCapability("appium:noSign", true);

        return new AndroidDriver(getAppiumServerUrl(), options);
    }

    private static IOSDriver createLocalIOSDriver() {
        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName(config.getProperty("ios.device.name"))
                .setPlatformVersion(config.getProperty("ios.platform.version"))
                .setApp(System.getProperty("user.dir") + "/" + config.getProperty("ios.app.path"))
                .setBundleId(config.getProperty("ios.bundle.id"))
                .setAutomationName(config.getProperty("ios.automation.name", "XCUITest"))
                .setAutoAcceptAlerts(true)
                .setNoReset(false)
                .setFullReset(false);

        return new IOSDriver(getAppiumServerUrl(), options);
    }

    private static IOSDriver createLocalIOSDriver(DeviceConfig deviceConfig) {
        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName(deviceConfig.getDeviceName())
                .setPlatformVersion(deviceConfig.getPlatformVersion())
                .setApp(System.getProperty("user.dir") + "/" + config.getProperty("ios.app.path"))
                .setBundleId(config.getProperty("ios.bundle.id"))
                .setAutomationName("XCUITest")
                .setAutoAcceptAlerts(true)
                .setNoReset(false)
                .setFullReset(false)
                .setUdid(deviceConfig.getUdid())
                .setWdaLocalPort(deviceConfig.getWdaLocalPort());

        return new IOSDriver(getAppiumServerUrl(), options);
    }

    // ===== BrowserStack Driver Creation =====

    private static AppiumDriver createBrowserStackDriver(Platform platform) {
        return switch (platform) {
            case ANDROID -> createBrowserStackAndroidDriver();
            case IOS -> createBrowserStackIOSDriver();
        };
    }

    private static AppiumDriver createBrowserStackDriver(DeviceConfig deviceConfig) {
        return switch (deviceConfig.getPlatform()) {
            case ANDROID -> createBrowserStackAndroidDriver(deviceConfig);
            case IOS -> createBrowserStackIOSDriver(deviceConfig);
        };
    }

    private static AndroidDriver createBrowserStackAndroidDriver() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("bstack:options", BrowserStackCapabilities.getAndroidCapabilities());
        options.setCapability("appium:app", config.getProperty("browserstack.app.url"));

        return new AndroidDriver(getBrowserStackUrl(), options);
    }

    private static AndroidDriver createBrowserStackAndroidDriver(DeviceConfig deviceConfig) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("bstack:options",
                BrowserStackCapabilities.getAndroidCapabilities(deviceConfig));
        options.setCapability("appium:app", config.getProperty("browserstack.app.url"));

        return new AndroidDriver(getBrowserStackUrl(), options);
    }

    private static IOSDriver createBrowserStackIOSDriver() {
        XCUITestOptions options = new XCUITestOptions();
        options.setCapability("bstack:options", BrowserStackCapabilities.getIOSCapabilities());
        options.setCapability("appium:app", config.getProperty("browserstack.app.url"));

        return new IOSDriver(getBrowserStackUrl(), options);
    }

    private static IOSDriver createBrowserStackIOSDriver(DeviceConfig deviceConfig) {
        XCUITestOptions options = new XCUITestOptions();
        options.setCapability("bstack:options",
                BrowserStackCapabilities.getIOSCapabilities(deviceConfig));
        options.setCapability("appium:app", config.getProperty("browserstack.app.url"));

        return new IOSDriver(getBrowserStackUrl(), options);
    }

    // ===== SauceLabs (Placeholder) =====

    private static AppiumDriver createSauceLabsDriver(Platform platform) {
        throw new UnsupportedOperationException("SauceLabs integration not yet implemented");
    }

    private static AppiumDriver createSauceLabsDriver(DeviceConfig deviceConfig) {
        throw new UnsupportedOperationException("SauceLabs integration not yet implemented");
    }

    // ===== Helpers =====

    private static void configureTimeouts(AppiumDriver driver) {
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(config.getImplicitWait()));
    }

    private static URL getAppiumServerUrl() {
        try {
            return new URL(config.getAppiumServerUrl());
        } catch (MalformedURLException e) {
            throw new DriverInitializationException("Invalid Appium server URL", e);
        }
    }

    private static URL getBrowserStackUrl() {
        try {
            String url = String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub",
                    config.getBrowserStackUsername(),
                    config.getBrowserStackAccessKey());
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new DriverInitializationException("Invalid BrowserStack URL", e);
        }
    }
}
