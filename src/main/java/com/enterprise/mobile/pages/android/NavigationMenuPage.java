package com.enterprise.mobile.pages.android;

import org.openqa.selenium.By;

import com.enterprise.mobile.pages.BasePage;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

/**
 * Navigation Menu Page Object for Sauce Labs My Demo App (Android).
 * Handles the side navigation drawer menu.
 */
public class NavigationMenuPage extends BasePage {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    // ===== Locators =====
    private final By menuButton = AppiumBy.accessibilityId("View menu");
    private final By catalogMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='Catalog' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");
    private final By webViewMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='WebView' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");
    private final By qrCodeMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='QR Code Scanner' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");
    private final By geoLocationMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='Geo Location' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");
    private final By drawingMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='Drawing' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");
    private final By aboutMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='About' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");
    private final By loginMenuItem = AppiumBy.accessibilityId("Login Menu Item");
    private final By logoutMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='Log Out' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");
    private final By resetAppMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='Reset App State' and @resource-id='" + APP_PACKAGE + ":id/itemTV']");

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(catalogMenuItem, 5);
    }

    @Step("Open navigation menu")
    public NavigationMenuPage openMenu() {
        click(menuButton);
        return this;
    }

    @Step("Navigate to Catalog from menu")
    public void goToCatalog() {
        click(catalogMenuItem);
    }

    @Step("Navigate to Login from menu")
    public void goToLogin() {
        click(loginMenuItem);
    }

    @Step("Tap Logout from menu")
    public void logout() {
        click(logoutMenuItem);
    }

    @Step("Navigate to WebView from menu")
    public void goToWebView() {
        click(webViewMenuItem);
    }

    @Step("Navigate to About from menu")
    public void goToAbout() {
        click(aboutMenuItem);
    }

    @Step("Navigate to Geo Location from menu")
    public void goToGeoLocation() {
        click(geoLocationMenuItem);
    }

    @Step("Navigate to Drawing from menu")
    public void goToDrawing() {
        click(drawingMenuItem);
    }

    @Step("Check if Login menu item is displayed")
    public boolean isLoginMenuItemVisible() {
        return isDisplayed(loginMenuItem, 3);
    }

    @Step("Check if Logout menu item is displayed")
    public boolean isLogoutMenuItemVisible() {
        return isDisplayed(logoutMenuItem, 3);
    }
}
