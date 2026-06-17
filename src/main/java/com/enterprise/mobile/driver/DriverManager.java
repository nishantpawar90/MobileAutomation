package com.enterprise.mobile.driver;

import io.appium.java_client.AppiumDriver;

/**
 * Thread-safe driver manager using ThreadLocal.
 * Ensures each thread in parallel execution has its own driver instance.
 */
public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
    }

    public static AppiumDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void setDriver(AppiumDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static void removeDriver() {
        AppiumDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
        }
        driverThreadLocal.remove();
    }

    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }
}
