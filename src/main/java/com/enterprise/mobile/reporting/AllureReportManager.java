package com.enterprise.mobile.reporting;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Allure report helper for programmatic step creation and attachment.
 */
public final class AllureReportManager {

    private static final Logger logger = LogManager.getLogger(AllureReportManager.class);

    private AllureReportManager() {
    }

    /**
     * Adds a step to the Allure report.
     */
    public static void addStep(String stepName, Status status) {
        Allure.step(stepName, step -> {
            step.name(stepName);
        });
    }

    /**
     * Adds environment information to Allure report.
     */
    public static void addEnvironmentInfo(String key, String value) {
        Allure.parameter(key, value);
    }

    /**
     * Attaches text content to Allure report.
     */
    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }

    /**
     * Attaches JSON content to Allure report.
     */
    public static void attachJson(String name, String jsonContent) {
        Allure.addAttachment(name, "application/json", jsonContent);
    }

    /**
     * Attaches device information to the current test.
     */
    public static void attachDeviceInfo() {
        String deviceInfo = ScreenshotUtils.captureDeviceInfo();
        attachText("Device Information", deviceInfo);
    }

    /**
     * Attaches network logs to Allure report.
     */
    public static void attachNetworkLogs(String logs) {
        attachText("Network Logs", logs);
    }
}
