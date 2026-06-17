package com.enterprise.mobile.reporting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.enterprise.mobile.config.FrameworkConstants;
import com.enterprise.mobile.driver.DriverManager;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;

/**
 * Screenshot and video recording utility for test evidence capture.
 */
public final class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    /**
     * Captures screenshot and returns file path.
     */
    public static String captureScreenshot(String testName) {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) {
                logger.warn("Driver is null, cannot capture screenshot");
                return null;
            }

            String timestamp = new SimpleDateFormat(FrameworkConstants.DATE_FORMAT).format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = FrameworkConstants.SCREENSHOT_PATH + fileName;

            File screenshotDir = new File(FrameworkConstants.SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            logger.info("Screenshot captured: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: {}", testName, e);
            return null;
        }
    }

    /**
     * Captures screenshot as Base64 string (for Extent Reports).
     */
    public static String captureScreenshotAsBase64() {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) {
                return null;
            }
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to capture Base64 screenshot", e);
            return null;
        }
    }

    /**
     * Captures screenshot as byte array (for Allure Reports).
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] captureScreenshotForAllure() {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) {
                return new byte[0];
            }
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for Allure", e);
            return new byte[0];
        }
    }

    /**
     * Attaches screenshot to Allure report with custom name.
     */
    public static void attachScreenshotToAllure(String name) {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) return;

            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure", e);
        }
    }

    /**
     * Captures device information for reporting.
     */
    public static String captureDeviceInfo() {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) return "Driver not available";

            StringBuilder info = new StringBuilder();
            info.append("Session ID: ").append(driver.getSessionId()).append("\n");

            var caps = driver.getCapabilities();
            info.append("Platform: ").append(caps.getPlatformName()).append("\n");
            info.append("Device: ").append(caps.getCapability("deviceName")).append("\n");
            info.append("OS Version: ").append(caps.getCapability("platformVersion")).append("\n");

            return info.toString();
        } catch (Exception e) {
            logger.error("Failed to capture device info", e);
            return "Device info unavailable";
        }
    }
}
