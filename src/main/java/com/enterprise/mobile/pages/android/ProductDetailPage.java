package com.enterprise.mobile.pages.android;

import org.openqa.selenium.By;

import com.enterprise.mobile.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Product Detail Page Object for Sauce Labs My Demo App (Android).
 * Maps to fragment_product_detail.xml.
 */
public class ProductDetailPage extends BasePage {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    // ===== Locators =====
    private final By pageTitle = By.id(APP_PACKAGE + ":id/productTV");
    private final By productImage = By.id(APP_PACKAGE + ":id/productIV");
    private final By productPrice = By.id(APP_PACKAGE + ":id/priceTV");
    private final By colorRecyclerView = By.id(APP_PACKAGE + ":id/colorRV");
    private final By minusButton = By.id(APP_PACKAGE + ":id/minusIV");
    private final By plusButton = By.id(APP_PACKAGE + ":id/plusIV");
    private final By quantityText = By.id(APP_PACKAGE + ":id/noTV");
    private final By addToCartButton = By.id(APP_PACKAGE + ":id/cartBt");
    private final By productHighlights = By.id(APP_PACKAGE + ":id/productHeightLightsTV");
    private final By productDescription = By.id(APP_PACKAGE + ":id/descTV");

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(productImage, 10);
    }

    @Step("Get product price")
    public String getProductPrice() {
        return getText(productPrice);
    }

    @Step("Get product quantity")
    public int getQuantity() {
        return Integer.parseInt(getText(quantityText));
    }

    @Step("Increase quantity by tapping plus button")
    public ProductDetailPage increaseQuantity() {
        click(plusButton);
        return this;
    }

    @Step("Decrease quantity by tapping minus button")
    public ProductDetailPage decreaseQuantity() {
        click(minusButton);
        return this;
    }

    @Step("Set quantity to {quantity}")
    public ProductDetailPage setQuantity(int quantity) {
        int current = getQuantity();
        while (current < quantity) {
            increaseQuantity();
            current++;
        }
        while (current > quantity && current > 1) {
            decreaseQuantity();
            current--;
        }
        return this;
    }

    @Step("Select product color at index: {colorIndex}")
    public ProductDetailPage selectColor(int colorIndex) {
        var colors = findElements(By.className("android.view.ViewGroup"));
        // Color selection in the colorRV RecyclerView
        if (colorIndex < colors.size()) {
            click(colors.get(colorIndex));
        }
        return this;
    }

    @Step("Tap Add To Cart button")
    public void addToCart() {
        click(addToCartButton);
    }

    @Step("Get product description text")
    public String getProductDescription() {
        return getText(productDescription);
    }

    @Step("Check if product highlights section is displayed")
    public boolean isProductHighlightsDisplayed() {
        return isDisplayed(productHighlights, 5);
    }

    @Step("Navigate back to catalog")
    public void goBackToCatalog() {
        navigateBack();
    }
}
