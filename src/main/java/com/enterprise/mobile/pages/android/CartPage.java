package com.enterprise.mobile.pages.android;

import org.openqa.selenium.By;

import com.enterprise.mobile.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Cart Page Object for Sauce Labs My Demo App (Android).
 * Maps to fragment_cart.xml.
 */
public class CartPage extends BasePage {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    // ===== Locators =====
    private final By cartTitle = By.id(APP_PACKAGE + ":id/productTV");
    private final By cartItemsList = By.id(APP_PACKAGE + ":id/productRV");
    private final By totalPrice = By.id(APP_PACKAGE + ":id/totalPriceTV");
    private final By itemCount = By.id(APP_PACKAGE + ":id/itemsTV");
    private final By proceedToCheckoutButton = By.id(APP_PACKAGE + ":id/cartBt");

    // Empty cart
    private final By emptyCartTitle = By.id(APP_PACKAGE + ":id/noItemTitleTV");
    private final By goShoppingButton = By.id(APP_PACKAGE + ":id/shoppingBt");

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(cartTitle, 10) || isDisplayed(emptyCartTitle, 3);
    }

    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        return isDisplayed(emptyCartTitle, 3);
    }

    @Step("Get total price from cart")
    public String getTotalPrice() {
        return getText(totalPrice);
    }

    @Step("Get item count text")
    public String getItemCountText() {
        return getText(itemCount);
    }

    @Step("Tap Proceed To Checkout button")
    public void proceedToCheckout() {
        click(proceedToCheckoutButton);
    }

    @Step("Tap Go Shopping button (empty cart)")
    public void goShopping() {
        click(goShoppingButton);
    }
}
