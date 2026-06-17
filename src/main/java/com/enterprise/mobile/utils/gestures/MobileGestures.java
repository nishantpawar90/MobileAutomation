package com.enterprise.mobile.utils.gestures;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import com.enterprise.mobile.enums.SwipeDirection;
import com.enterprise.mobile.exceptions.GestureException;

import io.appium.java_client.AppiumDriver;

/**
 * Comprehensive mobile gesture utility supporting all common mobile interactions.
 * Uses W3C Actions API (Appium 2.x compatible).
 */
public class MobileGestures {

    private static final Logger logger = LogManager.getLogger(MobileGestures.class);
    private final AppiumDriver driver;

    private static final double SWIPE_EDGE_OFFSET = 0.2;
    private static final int DEFAULT_SWIPE_DURATION_MS = 800;
    private static final int TAP_DURATION_MS = 100;
    private static final int LONG_PRESS_DURATION_MS = 2000;

    public MobileGestures(AppiumDriver driver) {
        this.driver = driver;
    }

    // ===== TAP =====

    /**
     * Single tap on screen coordinates.
     */
    public void tap(int x, int y) {
        logger.debug("Tap at coordinates ({}, {})", x, y);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(TAP_DURATION_MS)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    /**
     * Tap on a WebElement's center.
     */
    public void tap(WebElement element) {
        Point center = getElementCenter(element);
        tap(center.getX(), center.getY());
    }

    // ===== DOUBLE TAP =====

    /**
     * Double tap at specified coordinates.
     */
    public void doubleTap(int x, int y) {
        logger.debug("Double tap at ({}, {})", x, y);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence doubleTap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(50)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(100)))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(50)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(doubleTap));
    }

    /**
     * Double tap on a WebElement.
     */
    public void doubleTap(WebElement element) {
        Point center = getElementCenter(element);
        doubleTap(center.getX(), center.getY());
    }

    // ===== LONG PRESS =====

    /**
     * Long press at coordinates.
     */
    public void longPress(int x, int y) {
        longPress(x, y, LONG_PRESS_DURATION_MS);
    }

    /**
     * Long press at coordinates with custom duration.
     */
    public void longPress(int x, int y, int durationMs) {
        logger.debug("Long press at ({}, {}) for {}ms", x, y, durationMs);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(durationMs)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(longPress));
    }

    /**
     * Long press on a WebElement.
     */
    public void longPress(WebElement element) {
        Point center = getElementCenter(element);
        longPress(center.getX(), center.getY());
    }

    /**
     * Long press on a WebElement with custom duration.
     */
    public void longPress(WebElement element, int durationMs) {
        Point center = getElementCenter(element);
        longPress(center.getX(), center.getY(), durationMs);
    }

    // ===== SWIPE =====

    /**
     * Swipe in specified direction using default parameters.
     */
    public void swipe(SwipeDirection direction) {
        swipe(direction, DEFAULT_SWIPE_DURATION_MS);
    }

    /**
     * Swipe in specified direction with custom duration.
     */
    public void swipe(SwipeDirection direction, int durationMs) {
        Dimension size = driver.manage().window().getSize();
        int startX, startY, endX, endY;

        switch (direction) {
            case UP -> {
                startX = size.getWidth() / 2;
                startY = (int) (size.getHeight() * (1 - SWIPE_EDGE_OFFSET));
                endX = startX;
                endY = (int) (size.getHeight() * SWIPE_EDGE_OFFSET);
            }
            case DOWN -> {
                startX = size.getWidth() / 2;
                startY = (int) (size.getHeight() * SWIPE_EDGE_OFFSET);
                endX = startX;
                endY = (int) (size.getHeight() * (1 - SWIPE_EDGE_OFFSET));
            }
            case LEFT -> {
                startX = (int) (size.getWidth() * (1 - SWIPE_EDGE_OFFSET));
                startY = size.getHeight() / 2;
                endX = (int) (size.getWidth() * SWIPE_EDGE_OFFSET);
                endY = startY;
            }
            case RIGHT -> {
                startX = (int) (size.getWidth() * SWIPE_EDGE_OFFSET);
                startY = size.getHeight() / 2;
                endX = (int) (size.getWidth() * (1 - SWIPE_EDGE_OFFSET));
                endY = startY;
            }
            default -> throw new GestureException("Unsupported swipe direction: " + direction);
        }

        performSwipe(startX, startY, endX, endY, durationMs);
    }

    /**
     * Swipe from one point to another.
     */
    public void swipe(int startX, int startY, int endX, int endY) {
        performSwipe(startX, startY, endX, endY, DEFAULT_SWIPE_DURATION_MS);
    }

    // ===== SCROLL =====

    /**
     * Scroll until element is found (max attempts limited).
     */
    public boolean scrollToElement(WebElement element, SwipeDirection direction, int maxAttempts) {
        logger.debug("Scrolling {} to find element (max {} attempts)", direction, maxAttempts);
        for (int i = 0; i < maxAttempts; i++) {
            try {
                if (element.isDisplayed()) {
                    logger.debug("Element found after {} scroll(s)", i);
                    return true;
                }
            } catch (Exception ignored) {
                // Element not visible yet
            }
            swipe(direction);
        }
        logger.warn("Element not found after {} scroll attempts", maxAttempts);
        return false;
    }

    /**
     * Scroll within a specific element's bounds.
     */
    public void scrollWithinElement(WebElement container, SwipeDirection direction) {
        Point location = container.getLocation();
        Dimension size = container.getSize();

        int centerX = location.getX() + size.getWidth() / 2;
        int startY, endY;

        if (direction == SwipeDirection.UP) {
            startY = location.getY() + (int) (size.getHeight() * 0.8);
            endY = location.getY() + (int) (size.getHeight() * 0.2);
        } else {
            startY = location.getY() + (int) (size.getHeight() * 0.2);
            endY = location.getY() + (int) (size.getHeight() * 0.8);
        }

        performSwipe(centerX, startY, centerX, endY, DEFAULT_SWIPE_DURATION_MS);
    }

    // ===== DRAG AND DROP =====

    /**
     * Drag from source element to target element.
     */
    public void dragAndDrop(WebElement source, WebElement target) {
        Point sourceCenter = getElementCenter(source);
        Point targetCenter = getElementCenter(target);
        dragAndDrop(sourceCenter.getX(), sourceCenter.getY(),
                targetCenter.getX(), targetCenter.getY());
    }

    /**
     * Drag from one coordinate to another.
     */
    public void dragAndDrop(int startX, int startY, int endX, int endY) {
        logger.debug("Drag from ({}, {}) to ({}, {})", startX, startY, endX, endY);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence drag = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(500)))
                .addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, endY))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(drag));
    }

    // ===== PINCH AND ZOOM =====

    /**
     * Pinch (zoom out) gesture at center of screen.
     */
    public void pinch() {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.getWidth() / 2;
        int centerY = size.getHeight() / 2;
        pinch(centerX, centerY);
    }

    /**
     * Pinch at specified coordinates.
     */
    public void pinch(int x, int y) {
        logger.debug("Pinch gesture at ({}, {})", x, y);
        int offset = 100;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence pinchFinger1 = new Sequence(finger1, 0)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x - offset, y))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), x - 10, y))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence pinchFinger2 = new Sequence(finger2, 0)
                .addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x + offset, y))
                .addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), x + 10, y))
                .addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(pinchFinger1, pinchFinger2));
    }

    /**
     * Zoom (spread) gesture at center of screen.
     */
    public void zoom() {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.getWidth() / 2;
        int centerY = size.getHeight() / 2;
        zoom(centerX, centerY);
    }

    /**
     * Zoom at specified coordinates.
     */
    public void zoom(int x, int y) {
        logger.debug("Zoom gesture at ({}, {})", x, y);
        int offset = 100;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence zoomFinger1 = new Sequence(finger1, 0)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x - 10, y))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), x - offset, y))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence zoomFinger2 = new Sequence(finger2, 0)
                .addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x + 10, y))
                .addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), x + offset, y))
                .addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(zoomFinger1, zoomFinger2));
    }

    /**
     * Zoom on a specific element.
     */
    public void zoom(WebElement element) {
        Point center = getElementCenter(element);
        zoom(center.getX(), center.getY());
    }

    // ===== HELPERS =====

    private void performSwipe(int startX, int startY, int endX, int endY, int durationMs) {
        logger.debug("Swipe from ({}, {}) to ({}, {})", startX, startY, endX, endY);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    private Point getElementCenter(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        return new Point(
                location.getX() + size.getWidth() / 2,
                location.getY() + size.getHeight() / 2
        );
    }
}
