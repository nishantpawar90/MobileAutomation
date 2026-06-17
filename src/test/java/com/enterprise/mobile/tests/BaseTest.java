package com.enterprise.mobile.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.enterprise.mobile.driver.DeviceConfig;
import com.enterprise.mobile.driver.DriverFactory;
import com.enterprise.mobile.driver.DriverManager;
import com.enterprise.mobile.enums.Platform;
import com.enterprise.mobile.listeners.AnnotationTransformer;
import com.enterprise.mobile.listeners.TestListener;

import io.appium.java_client.AppiumDriver;

/**
 * Base test class providing driver lifecycle management.
 * All test classes must extend this class.
 * Supports parameterized execution for device matrix testing.
 */
@Listeners({TestListener.class, AnnotationTransformer.class})
public abstract class BaseTest {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected AppiumDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "deviceName", "platformVersion", "udid"})
    public void setUp(@Optional String platform,
                      @Optional String deviceName,
                      @Optional String platformVersion,
                      @Optional String udid) {

        if (platform != null && deviceName != null) {
            // Parameterized execution (from testng.xml)
            DeviceConfig deviceConfig = DeviceConfig.builder()
                    .platform(Platform.valueOf(platform.toUpperCase()))
                    .deviceName(deviceName)
                    .platformVersion(platformVersion != null ? platformVersion : "")
                    .udid(udid != null ? udid : "")
                    .build();
            driver = DriverFactory.createDriver(deviceConfig);
        } else {
            // Default execution from config
            driver = DriverFactory.createDriver();
        }

        logger.info("Test setup complete. Driver session: {}", driver.getSessionId());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down test...");
        DriverManager.removeDriver();
    }
}
