package com.enterprise.mobile.utils;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enterprise.mobile.driver.DriverManager;

import io.appium.java_client.AppiumDriver;

/**
 * Wait utility providing various explicit wait strategies.
 */
public final class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    private WaitUtils() {
    }

    public static WebElement waitForVisibility(By locator, int timeoutSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator, int timeoutSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForPresence(By locator, int timeoutSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForInvisibility(By locator, int timeoutSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForTextPresent(By locator, String text, int timeoutSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean isElementPresent(By locator) {
        try {
            DriverManager.getDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static void waitForCondition(int timeoutSeconds, java.util.function.Supplier<Boolean> condition) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (condition.get()) {
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new org.openqa.selenium.TimeoutException(
                "Condition not met within " + timeoutSeconds + " seconds");
    }
}
