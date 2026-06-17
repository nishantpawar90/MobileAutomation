package com.enterprise.mobile.pages.android;

import org.openqa.selenium.By;

import com.enterprise.mobile.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Checkout Info Page Object for Sauce Labs My Demo App (Android).
 * Maps to fragment_checkout_info.xml — shipping address form.
 */
public class CheckoutInfoPage extends BasePage {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    // ===== Locators =====
    private final By checkoutTitle = By.id(APP_PACKAGE + ":id/checkoutTitleTV");
    private final By fullNameField = By.id(APP_PACKAGE + ":id/fullNameET");
    private final By addressLine1Field = By.id(APP_PACKAGE + ":id/address1ET");
    private final By addressLine2Field = By.id(APP_PACKAGE + ":id/address2ET");
    private final By cityField = By.id(APP_PACKAGE + ":id/cityET");
    private final By stateField = By.id(APP_PACKAGE + ":id/stateET");
    private final By zipField = By.id(APP_PACKAGE + ":id/zipET");
    private final By countryField = By.id(APP_PACKAGE + ":id/countryET");
    private final By toPaymentButton = By.id(APP_PACKAGE + ":id/paymentBtn");

    // Error messages
    private final By fullNameError = By.id(APP_PACKAGE + ":id/fullNameErrorTV");
    private final By addressError = By.id(APP_PACKAGE + ":id/address1ErrorTV");
    private final By cityError = By.id(APP_PACKAGE + ":id/cityErrorTV");
    private final By zipError = By.id(APP_PACKAGE + ":id/zipErrorTV");

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(checkoutTitle, 10);
    }

    @Step("Enter full name: {fullName}")
    public CheckoutInfoPage enterFullName(String fullName) {
        sendKeys(fullNameField, fullName);
        return this;
    }

    @Step("Enter address line 1: {address}")
    public CheckoutInfoPage enterAddress1(String address) {
        sendKeys(addressLine1Field, address);
        return this;
    }

    @Step("Enter address line 2: {address}")
    public CheckoutInfoPage enterAddress2(String address) {
        sendKeys(addressLine2Field, address);
        return this;
    }

    @Step("Enter city: {city}")
    public CheckoutInfoPage enterCity(String city) {
        sendKeys(cityField, city);
        return this;
    }

    @Step("Enter state: {state}")
    public CheckoutInfoPage enterState(String state) {
        sendKeys(stateField, state);
        return this;
    }

    @Step("Enter zip code: {zip}")
    public CheckoutInfoPage enterZip(String zip) {
        sendKeys(zipField, zip);
        return this;
    }

    @Step("Enter country: {country}")
    public CheckoutInfoPage enterCountry(String country) {
        sendKeys(countryField, country);
        return this;
    }

    @Step("Fill complete shipping address")
    public CheckoutInfoPage fillShippingAddress(String fullName, String address1,
                                                 String city, String state,
                                                 String zip, String country) {
        enterFullName(fullName);
        enterAddress1(address1);
        enterCity(city);
        enterState(state);
        enterZip(zip);
        enterCountry(country);
        hideKeyboard();
        return this;
    }

    @Step("Tap To Payment button")
    public void tapToPayment() {
        click(toPaymentButton);
    }

    @Step("Check if full name error is displayed")
    public boolean isFullNameErrorDisplayed() {
        return isDisplayed(fullNameError, 3);
    }

    @Step("Check if address error is displayed")
    public boolean isAddressErrorDisplayed() {
        return isDisplayed(addressError, 3);
    }

    @Step("Check if city error is displayed")
    public boolean isCityErrorDisplayed() {
        return isDisplayed(cityError, 3);
    }

    @Step("Check if zip error is displayed")
    public boolean isZipErrorDisplayed() {
        return isDisplayed(zipError, 3);
    }
}
