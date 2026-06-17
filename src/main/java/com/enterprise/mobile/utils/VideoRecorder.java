package com.enterprise.mobile.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enterprise.mobile.config.FrameworkConstants;
import com.enterprise.mobile.driver.DriverManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;

import java.time.Duration;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Video recording utility for test execution evidence.
 */
public final class VideoRecorder {

    private static final Logger logger = LogManager.getLogger(VideoRecorder.class);
    private static final ThreadLocal<Boolean> isRecording = ThreadLocal.withInitial(() -> false);

    private VideoRecorder() {
    }

    public static void startRecording() {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) return;

            if (driver instanceof AndroidDriver androidDriver) {
                androidDriver.startRecordingScreen(
                        new AndroidStartScreenRecordingOptions()
                                .withTimeLimit(Duration.ofMinutes(10))
                                .withVideoSize("1280x720")
                                .withBitRate(5000000));
            } else if (driver instanceof IOSDriver iosDriver) {
                iosDriver.startRecordingScreen(
                        new IOSStartScreenRecordingOptions()
                                .withTimeLimit(Duration.ofMinutes(10))
                                .withVideoQuality(IOSStartScreenRecordingOptions.VideoQuality.MEDIUM));
            }
            isRecording.set(true);
            logger.info("Screen recording started");
        } catch (Exception e) {
            logger.warn("Failed to start screen recording: {}", e.getMessage());
        }
    }

    public static String stopRecording(String testName) {
        try {
            if (!isRecording.get()) return null;

            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) return null;

            String videoBase64;
            if (driver instanceof AndroidDriver androidDriver) {
                videoBase64 = androidDriver.stopRecordingScreen();
            } else if (driver instanceof IOSDriver iosDriver) {
                videoBase64 = iosDriver.stopRecordingScreen();
            } else {
                return null;
            }

            isRecording.set(false);

            // Save video file
            String timestamp = new SimpleDateFormat(FrameworkConstants.DATE_FORMAT).format(new Date());
            String fileName = testName + "_" + timestamp + ".mp4";
            String filePath = FrameworkConstants.VIDEO_PATH + fileName;

            Path videoDir = Paths.get(FrameworkConstants.VIDEO_PATH);
            if (!Files.exists(videoDir)) {
                Files.createDirectories(videoDir);
            }

            byte[] videoBytes = Base64.getDecoder().decode(videoBase64);
            Files.write(Paths.get(filePath), videoBytes);

            logger.info("Video saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to stop/save screen recording", e);
            return null;
        }
    }

    public static void cleanup() {
        isRecording.remove();
    }
}
