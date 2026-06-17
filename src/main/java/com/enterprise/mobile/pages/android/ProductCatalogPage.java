package com.enterprise.mobile.pages.android;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enterprise.mobile.enums.SwipeDirection;
import com.enterprise.mobile.pages.BasePage;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

/**
 * Product Catalog Page Object for Sauce Labs My Demo App (Android).
 * Maps to fragment_product_catalog.xml — the main product listing screen.
 *
 * This is the home/landing page after opening the app (no login required to
 * browse).
 */
public class ProductCatalogPage extends BasePage {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    // ===== Locators =====
    private final By pageTitle = By.id(APP_PACKAGE + ":id/productTV");
    private final By productsRecyclerView = By.id(APP_PACKAGE + ":id/productRV");
    private final By productTitle = By.id(APP_PACKAGE + ":id/titleTV");
    private final By productPrice = By.id(APP_PACKAGE + ":id/priceTV");
    private final By productImage = By.id(APP_PACKAGE + ":id/productIV");

    // Navigation / Menu
    private final By menuButton = AppiumBy.accessibilityId("View menu");
    private final By cartBadge = AppiumBy.accessibilityId("View cart");
    private final By sortButton = AppiumBy
            .accessibilityId("Shows current sorting order and displays available sorting options");

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(pageTitle, 15);
    }

    @Step("Get catalog page title")
    public String getPageTitle() {
        return getText(pageTitle);
    }

    @Step("Get all product titles from catalog")
    public List<String> getProductTitles() {
        List<WebElement> titles = findElements(productTitle);
        return titles.stream().map(WebElement::getText).toList();
    }

    @Step("Get all product prices from catalog")
    public List<String> getProductPrices() {
        List<WebElement> prices = findElements(productPrice);
        return prices.stream().map(WebElement::getText).toList();
    }

    @Step("Get number of visible products")
    public int getVisibleProductCount() {
        return getElementCount(productTitle);
    }

    @Step("Tap on product at index: {index}")
    public void tapProductAtIndex(int index) {
        List<WebElement> products = findElements(productImage);
        if (index < products.size()) {
            click(products.get(index));
        } else {
            throw new IndexOutOfBoundsException(
                    "Product index " + index + " out of range. Total products: " + products.size());
        }
    }

    @Step("Tap on product by name: {productName}")
    public void tapProductByName(String productName) {
        List<WebElement> titles = findElements(productTitle);
        for (WebElement title : titles) {
            if (title.getText().equalsIgnoreCase(productName)) {
                click(title);
                return;
            }
        }
        // Try scrolling to find the product
        for (int i = 0; i < 5; i++) {
            gestures.swipe(SwipeDirection.UP);
            titles = findElements(productTitle);
            for (WebElement title : titles) {
                if (title.getText().equalsIgnoreCase(productName)) {
                    click(title);
                    return;
                }
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    @Step("Open navigation menu")
    public void openMenu() {
        click(menuButton);
    }

    @Step("Scroll down to see more products")
    public void scrollDown() {
        gestures.swipe(SwipeDirection.UP);
    }

    @Step("Check if cart badge is visible")
    public boolean isCartBadgeVisible() {
        return isDisplayed(cartBadge, 3);
    }

    @Step("Tap cart icon")
    public void tapCart() {
        click(cartBadge);
    }
}
