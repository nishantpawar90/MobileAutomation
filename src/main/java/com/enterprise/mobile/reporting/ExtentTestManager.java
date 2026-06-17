package com.enterprise.mobile.reporting;

import com.aventstack.extentreports.ExtentTest;

/**
 * Thread-safe Extent Test holder for parallel execution support.
 */
public final class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentTestManager() {
    }

    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    public static void setTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static void removeTest() {
        extentTestThreadLocal.remove();
    }

    public static ExtentTest createTest(String testName) {
        ExtentTest test = ExtentReportManager.getInstance().createTest(testName);
        setTest(test);
        return test;
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = ExtentReportManager.getInstance().createTest(testName, description);
        setTest(test);
        return test;
    }
}
