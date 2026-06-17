package com.enterprise.mobile.pages.android;

import org.openqa.selenium.By;

import com.enterprise.mobile.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Login Page Object for Sauce Labs My Demo App (Android).
 * Maps to fragment_login.xml layout.
 *
 * App Package: com.saucelabs.mydemoapp.android
 * Valid credentials: bob@example.com / 10203040
 */
public class LoginPage extends BasePage {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    // ===== Locators (mapped from fragment_login.xml resource IDs) =====
    private final By loginTitle = By.id(APP_PACKAGE + ":id/loginTV");
    private final By usernameField = By.id(APP_PACKAGE + ":id/nameET");
    private final By passwordField = By.id(APP_PACKAGE + ":id/passwordET");
    private final By loginButton = By.id(APP_PACKAGE + ":id/loginBtn");
    private final By usernameError = By.id(APP_PACKAGE + ":id/nameErrorTV");
    private final By passwordError = By.id(APP_PACKAGE + ":id/passwordErrorTV");
    private final By usernameErrorIcon = By.id(APP_PACKAGE + ":id/usernameErrorIV");
    private final By passwordErrorIcon = By.id(APP_PACKAGE + ":id/passwordErrorIV");
    private final By biometricButton = By.id(APP_PACKAGE + ":id/bioMetricIB");

    // Pre-populated credentials on the login screen
    private final By savedUsername1 = By.id(APP_PACKAGE + ":id/username1TV");
    private final By savedPassword1 = By.id(APP_PACKAGE + ":id/password1TV");

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(loginTitle, 10);
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        sendKeys(usernameField, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        sendKeys(passwordField, password);
        hideKeyboard();
        return this;
    }

    @Step("Tap login button")
    public LoginPage tapLogin() {
        click(loginButton);
        return this;
    }

    @Step("Login with credentials: {username}")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        tapLogin();
    }

    @Step("Tap saved username (bob@example.com) to auto-fill")
    public LoginPage tapSavedUsername() {
        click(savedUsername1);
        return this;
    }

    @Step("Check if username error is displayed")
    public boolean isUsernameErrorDisplayed() {
        return isDisplayed(usernameErrorIcon, 3);
    }

    @Step("Get username error message")
    public String getUsernameError() {
        return getText(usernameError);
    }

    @Step("Check if password error is displayed")
    public boolean isPasswordErrorDisplayed() {
        return isDisplayed(passwordErrorIcon, 3);
    }

    @Step("Get password error message")
    public String getPasswordError() {
        return getText(passwordError);
    }

    @Step("Get login page title")
    public String getLoginTitle() {
        return getText(loginTitle);
    }

    @Step("Check if login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton, 5);
    }
}
