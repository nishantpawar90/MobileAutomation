package com.enterprise.mobile.enterprise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.enterprise.mobile.driver.DriverManager;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

/**
 * Self-healing locator mechanism.
 * When a primary locator fails, attempts alternative locators
 * and records the successful alternative for future use.
 *
 * AI-assisted recovery: Analyzes element attributes to generate
 * alternative locator strategies dynamically.
 */
public final class SelfHealingLocator {

    private static final Logger logger = LogManager.getLogger(SelfHealingLocator.class);

    // Stores healed locators for the session: original -> healed
    private static final ConcurrentHashMap<String, By> healedLocators = new ConcurrentHashMap<>();

    // Stores locator alternatives: elementName -> list of alternative locators
    private static final Map<String, List<By>> locatorAlternatives = new ConcurrentHashMap<>();

    private SelfHealingLocator() {
    }

    /**
     * Attempts to find element with self-healing capability.
     * First tries primary locator, then falls back to alternatives.
     */
    public static WebElement findElement(String elementName, By primaryLocator, By... alternativeLocators) {
        AppiumDriver driver = DriverManager.getDriver();

        // Check if we already have a healed locator
        By healedLocator = healedLocators.get(elementName);
        if (healedLocator != null) {
            try {
                WebElement element = driver.findElement(healedLocator);
                logger.debug("Found element '{}' using healed locator: {}", elementName, healedLocator);
                return element;
            } catch (NoSuchElementException e) {
                // Healed locator also failed, remove it
                healedLocators.remove(elementName);
            }
        }

        // Try primary locator
        try {
            WebElement element = driver.findElement(primaryLocator);
            logger.debug("Found element '{}' using primary locator", elementName);
            return element;
        } catch (NoSuchElementException e) {
            logger.warn("Primary locator failed for '{}'. Attempting self-healing...", elementName);
        }

        // Try alternative locators
        for (By alternative : alternativeLocators) {
            try {
                WebElement element = driver.findElement(alternative);
                logger.info("SELF-HEALED: '{}' found using alternative locator: {}", elementName, alternative);
                healedLocators.put(elementName, alternative);
                return element;
            } catch (NoSuchElementException ignored) {
                // Continue to next alternative
            }
        }

        // Try AI-generated alternatives
        List<By> aiAlternatives = generateAlternativeLocators(elementName, primaryLocator);
        for (By aiAlternative : aiAlternatives) {
            try {
                WebElement element = driver.findElement(aiAlternative);
                logger.info("AI-HEALED: '{}' found using generated locator: {}", elementName, aiAlternative);
                healedLocators.put(elementName, aiAlternative);
                return element;
            } catch (NoSuchElementException ignored) {
                // Continue to next alternative
            }
        }

        throw new NoSuchElementException(
                "Self-healing failed for element: " + elementName + " [" + primaryLocator + "]");
    }

    /**
     * Generates alternative locator strategies based on the original locator.
     * Simulates AI-assisted locator recovery by trying common patterns.
     */
    private static List<By> generateAlternativeLocators(String elementName, By originalLocator) {
        List<By> alternatives = new ArrayList<>();
        String locatorStr = originalLocator.toString();

        // Generate accessibility ID variant
        if (!locatorStr.contains("accessibility")) {
            alternatives.add(AppiumBy.accessibilityId(elementName));
            alternatives.add(AppiumBy.accessibilityId(elementName.replace("_", " ")));
            alternatives.add(AppiumBy.accessibilityId(elementName.replace(" ", "_")));
        }

        // Generate XPath variants
        if (!locatorStr.contains("xpath")) {
            alternatives.add(By.xpath("//*[contains(@text, '" + elementName + "')]"));
            alternatives.add(By.xpath("//*[contains(@content-desc, '" + elementName + "')]"));
            alternatives.add(By.xpath("//*[contains(@resource-id, '" + elementName + "')]"));
        }

        return alternatives;
    }

    /**
     * Registers alternative locators for an element.
     */
    public static void registerAlternatives(String elementName, List<By> alternatives) {
        locatorAlternatives.put(elementName, alternatives);
    }

    /**
     * Returns all healed locators (for reporting).
     */
    public static Map<String, By> getHealedLocators() {
        return new HashMap<>(healedLocators);
    }

    /**
     * Gets the count of healed locators in this session.
     */
    public static int getHealedCount() {
        return healedLocators.size();
    }

    /**
     * Clears healed locator cache.
     */
    public static void clearCache() {
        healedLocators.clear();
    }

    /**
     * Generates a healing report for the test session.
     */
    public static String generateHealingReport() {
        if (healedLocators.isEmpty()) {
            return "No locators were healed during this session.";
        }

        StringBuilder report = new StringBuilder("=== Self-Healing Report ===\n");
        report.append("Total healed locators: ").append(healedLocators.size()).append("\n\n");

        healedLocators.forEach((name, locator) ->
                report.append(String.format("Element: '%s' -> Healed to: %s%n", name, locator)));

        return report.toString();
    }
}
