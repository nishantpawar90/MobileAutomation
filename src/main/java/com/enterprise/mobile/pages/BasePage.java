package com.enterprise.mobile.pages;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enterprise.mobile.config.ConfigManager;
import com.enterprise.mobile.config.FrameworkConstants;
import com.enterprise.mobile.driver.DriverManager;
import com.enterprise.mobile.enums.Platform;
import com.enterprise.mobile.enums.WaitStrategy;
import com.enterprise.mobile.exceptions.ElementNotFoundException;
import com.enterprise.mobile.utils.gestures.MobileGestures;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

/**
 * Base page class providing common mobile interactions.
 * All page objects must extend this class.
 * Implements explicit wait strategies and element interaction utilities.
 */
public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final AppiumDriver driver;
    protected final MobileGestures gestures;
    protected final ConfigManager config;
    private final WebDriverWait wait;
    private final FluentWait<AppiumDriver> fluentWait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.config = ConfigManager.getInstance();
        this.gestures = new MobileGestures(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(config.getExplicitWait()))
                .pollingEvery(Duration.ofMillis(FrameworkConstants.DEFAULT_POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        PageFactory.initElements(new AppiumFieldDecorator(driver,
                Duration.ofSeconds(config.getExplicitWait())), this);
    }

    // ===== Element Interaction Methods =====

    protected void click(By locator) {
        click(locator, WaitStrategy.CLICKABLE);
    }

    protected void click(By locator, WaitStrategy waitStrategy) {
        WebElement element = findElement(locator, waitStrategy);
        logger.debug("Clicking element: {}", locator);
        element.click();
    }

    protected void click(WebElement element) {
        waitForElementToBeClickable(element);
        logger.debug("Clicking element: {}", element);
        element.click();
    }

    protected void sendKeys(By locator, String text) {
        sendKeys(locator, text, WaitStrategy.VISIBLE);
    }

    protected void sendKeys(By locator, String text, WaitStrategy waitStrategy) {
        WebElement element = findElement(locator, waitStrategy);
        logger.debug("Entering text '{}' into element: {}", text, locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void sendKeys(WebElement element, String text) {
        waitForElementToBeVisible(element);
        logger.debug("Entering text into element");
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return getText(locator, WaitStrategy.VISIBLE);
    }

    protected String getText(By locator, WaitStrategy waitStrategy) {
        WebElement element = findElement(locator, waitStrategy);
        return element.getText();
    }

    protected String getText(WebElement element) {
        waitForElementToBeVisible(element);
        return element.getText();
    }

    protected String getAttribute(By locator, String attribute) {
        WebElement element = findElement(locator, WaitStrategy.PRESENCE);
        return element.getAttribute(attribute);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return findElement(locator, WaitStrategy.VISIBLE).isDisplayed();
        } catch (TimeoutException | ElementNotFoundException e) {
            return false;
        }
    }

    protected boolean isDisplayed(By locator, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isEnabled(By locator) {
        return findElement(locator, WaitStrategy.PRESENCE).isEnabled();
    }

    protected List<WebElement> findElements(By locator) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator);
    }

    protected int getElementCount(By locator) {
        try {
            return driver.findElements(locator).size();
        } catch (Exception e) {
            return 0;
        }
    }

    // ===== Wait Strategies =====

    protected WebElement findElement(By locator, WaitStrategy waitStrategy) {
        return switch (waitStrategy) {
            case CLICKABLE -> wait.until(ExpectedConditions.elementToBeClickable(locator));
            case VISIBLE -> wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            case PRESENCE -> wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            case NONE -> driver.findElement(locator);
        };
    }

    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementToDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForTextToBePresent(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    protected WebElement waitWithFluentWait(By locator) {
        return fluentWait.until(d -> d.findElement(locator));
    }

    // ===== Platform-specific Methods =====

    protected boolean isAndroid() {
        return config.getPlatform() == Platform.ANDROID;
    }

    protected boolean isIOS() {
        return config.getPlatform() == Platform.IOS;
    }

    protected By platformLocator(By androidLocator, By iosLocator) {
        return isAndroid() ? androidLocator : iosLocator;
    }

    // ===== Screen Utilities =====

    protected Dimension getScreenSize() {
        return driver.manage().window().getSize();
    }

    protected void hideKeyboard() {
        try {
            if (driver instanceof io.appium.java_client.android.AndroidDriver androidDriver) {
                androidDriver.executeScript("mobile: hideKeyboard");
            } else if (driver instanceof io.appium.java_client.ios.IOSDriver iosDriver) {
                iosDriver.executeScript("mobile: hideKeyboard", Map.of());
            }
        } catch (Exception e) {
            logger.debug("Keyboard was not visible or could not be hidden");
        }
    }

    protected void navigateBack() {
        driver.navigate().back();
    }

    // ===== Page Validation =====

    public abstract boolean isPageLoaded();

    protected void verifyPageLoaded() {
        if (!isPageLoaded()) {
            throw new IllegalStateException(
                    this.getClass().getSimpleName() + " is not loaded as expected");
        }
    }
}
