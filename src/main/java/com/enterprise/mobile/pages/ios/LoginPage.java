package com.enterprise.mobile.pages.ios;

import org.openqa.selenium.By;

import com.enterprise.mobile.pages.BasePage;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

/**
 * Sample Login Page Object for iOS.
 * Demonstrates iOS-specific locator strategies.
 */
public class LoginPage extends BasePage {

    // iOS Locators (using accessibility ID and class chain)
    private final By usernameField = AppiumBy.accessibilityId("username_input");
    private final By passwordField = AppiumBy.accessibilityId("password_input");
    private final By loginButton = AppiumBy.accessibilityId("login_button");
    private final By errorMessage = AppiumBy.iOSClassChain(
            "**/XCUIElementTypeStaticText[`name == 'error_message'`]");
    private final By forgotPasswordLink = AppiumBy.accessibilityId("forgot_password");
    private final By signUpLink = AppiumBy.accessibilityId("signup_link");
    private final By biometricButton = AppiumBy.accessibilityId("biometric_login");

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(usernameField, 10);
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
    public void tapLogin() {
        click(loginButton);
    }

    @Step("Login with credentials: {username}")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        tapLogin();
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage, 5);
    }

    @Step("Tap biometric login")
    public void tapBiometricLogin() {
        click(biometricButton);
    }

    @Step("Tap forgot password link")
    public void tapForgotPassword() {
        click(forgotPasswordLink);
    }

    @Step("Tap sign up link")
    public void tapSignUp() {
        click(signUpLink);
    }
}
