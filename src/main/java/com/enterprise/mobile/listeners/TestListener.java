package com.enterprise.mobile.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.enterprise.mobile.config.ConfigManager;
import com.enterprise.mobile.enterprise.NetworkLogCapture;
import com.enterprise.mobile.enterprise.PerformanceMonitor;
import com.enterprise.mobile.enterprise.SelfHealingLocator;
import com.enterprise.mobile.reporting.AllureReportManager;
import com.enterprise.mobile.reporting.ExtentReportManager;
import com.enterprise.mobile.reporting.ExtentTestManager;
import com.enterprise.mobile.reporting.ScreenshotUtils;
import com.enterprise.mobile.utils.VideoRecorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for test lifecycle management.
 * Handles reporting, screenshots, video recording, and cleanup.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("============================================");
        logger.info("Test Suite Started: {}", context.getName());
        logger.info("============================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("============================================");
        logger.info("Test Suite Finished: {}", context.getName());
        logger.info("Passed: {} | Failed: {} | Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        logger.info("============================================");

        // Log self-healing report
        String healingReport = SelfHealingLocator.generateHealingReport();
        logger.info(healingReport);

        ExtentReportManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        logger.info(">>> Test Started: {}", testName);

        // Create Extent Test
        ExtentTestManager.createTest(testName, description != null ? description : "");

        // Add categories/tags
        String[] groups = result.getMethod().getGroups();
        ExtentTest test = ExtentTestManager.getTest();
        for (String group : groups) {
            test.assignCategory(group);
        }

        // Start video recording if enabled
        if (ConfigManager.getInstance().isVideoRecordingEnabled()) {
            VideoRecorder.startRecording();
        }

        // Start network capture
        NetworkLogCapture.startCapture();

        // Start performance timer
        PerformanceMonitor.startTimer(testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = PerformanceMonitor.stopTimer(testName);

        logger.info("<<< Test PASSED: {} ({}ms)", testName, duration);

        ExtentTestManager.getTest().pass("Test passed successfully");

        // Stop video
        if (ConfigManager.getInstance().isVideoRecordingEnabled()) {
            VideoRecorder.stopRecording(testName);
        }

        NetworkLogCapture.stopCapture();
        cleanupTestResources();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();

        logger.error("<<< Test FAILED: {}", testName, throwable);

        // Capture screenshot
        String screenshotBase64 = ScreenshotUtils.captureScreenshotAsBase64();
        ScreenshotUtils.captureScreenshot(testName);
        ScreenshotUtils.captureScreenshotForAllure();

        // Extent Report failure with screenshot
        ExtentTest test = ExtentTestManager.getTest();
        if (screenshotBase64 != null) {
            test.fail(throwable,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
        } else {
            test.fail(throwable);
        }

        // Allure attachments
        AllureReportManager.attachDeviceInfo();
        ScreenshotUtils.attachScreenshotToAllure("Failure Screenshot");

        // Stop and save video
        if (ConfigManager.getInstance().isVideoRecordingEnabled()) {
            VideoRecorder.stopRecording(testName + "_FAILED");
        }

        // Attach network logs
        NetworkLogCapture.stopCapture();
        NetworkLogCapture.attachToReport();

        // Performance metrics
        PerformanceMonitor.stopTimer(testName);
        PerformanceMonitor.attachToReport();

        cleanupTestResources();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("--- Test SKIPPED: {}", testName);

        ExtentTestManager.getTest().skip("Test skipped");

        if (result.getThrowable() != null) {
            ExtentTestManager.getTest().skip(result.getThrowable());
        }

        cleanupTestResources();
    }

    private void cleanupTestResources() {
        ExtentTestManager.removeTest();
        NetworkLogCapture.cleanup();
        PerformanceMonitor.cleanup();
        VideoRecorder.cleanup();
    }
}
