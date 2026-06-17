package com.enterprise.mobile.tests.login;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enterprise.mobile.data.JsonDataReader;
import com.enterprise.mobile.data.TestDataGenerator;
import com.enterprise.mobile.pages.android.LoginPage;
import com.enterprise.mobile.pages.android.NavigationMenuPage;
import com.enterprise.mobile.pages.android.ProductCatalogPage;
import com.enterprise.mobile.tests.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import java.util.Map;

/**
 * Login functionality test cases for Sauce Labs My Demo App.
 *
 * Valid credentials:
 * - bob@example.com / 10203040
 * Locked out user:
 * - alice@example.com / 10203040
 *
 * Navigation: Open app -> Product Catalog -> Menu -> Login
 */
@Epic("Authentication")
@Feature("Login")
public class LoginTests extends BaseTest {

    private LoginPage loginPage;
    private ProductCatalogPage catalogPage;
    private NavigationMenuPage menuPage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToLogin() {
        catalogPage = new ProductCatalogPage();
        menuPage = new NavigationMenuPage();
        loginPage = new LoginPage();

        // Wait for splash screen to dismiss and catalog to load
        Assert.assertTrue(catalogPage.isPageLoaded(), "Catalog page should load on app start");

        // Navigate to Login via side menu
        menuPage.openMenu();
        menuPage.goToLogin();
    }

    @Test(groups = { "smoke", "login" }, description = "Verify successful login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Valid Login")
    @Description("Test that user can successfully login with bob@example.com / 10203040")
    public void testSuccessfulLogin() {
        Map<String, Object> testData = JsonDataReader.readDataAsMap("login/valid-credentials.json");

        loginPage.login(
                (String) testData.get("username"),
                (String) testData.get("password"));

        // After successful login, app navigates back to Product Catalog
        Assert.assertTrue(catalogPage.isPageLoaded(),
                "Product catalog should be displayed after successful login");
    }

    @Test(groups = { "smoke",
            "login" }, description = "Verify login with any credentials navigates to catalog (demo app)")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid Login")
    @Description("Test that the demo app accepts any credentials and navigates to catalog")
    public void testInvalidLogin() {
        loginPage.login("invalid_user@example.com", "wrong_password");

        // Sauce Labs demo app accepts ANY credentials and navigates to catalog
        Assert.assertTrue(catalogPage.isPageLoaded(),
                "Demo app should navigate to catalog even with invalid credentials");
    }

    @Test(groups = { "regression", "login" }, description = "Verify login with empty username shows error")
    @Severity(SeverityLevel.NORMAL)
    @Story("Login Validation")
    @Description("Test that username error is shown when username field is empty")
    public void testLoginWithEmptyUsername() {
        loginPage.enterPassword("10203040").tapLogin();

        Assert.assertTrue(loginPage.isUsernameErrorDisplayed(),
                "Username error icon should be displayed for empty username");
    }

    @Test(groups = { "regression", "login" }, description = "Verify login with empty password shows error")
    @Severity(SeverityLevel.NORMAL)
    @Story("Login Validation")
    @Description("Test that password error is shown when password field is empty")
    public void testLoginWithEmptyPassword() {
        loginPage.enterUsername("bob@example.com").tapLogin();

        Assert.assertTrue(loginPage.isPasswordErrorDisplayed(),
                "Password error icon should be displayed for empty password");
    }

    @Test(groups = { "regression", "login" }, description = "Verify login with random generated credentials fails")
    @Severity(SeverityLevel.NORMAL)
    @Story("Data-Driven Login")
    @Description("Test login with faker-generated credentials to verify error handling")
    public void testLoginWithRandomCredentials() {
        String randomUser = TestDataGenerator.generateEmail();
        String randomPass = TestDataGenerator.generatePassword();

        loginPage.login(randomUser, randomPass);

        // Should remain on login page with error
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Should remain on login page with random credentials");
    }

    @Test(groups = { "regression", "login" }, description = "Verify locked out user cannot login")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Locked Account")
    @Description("Test that alice@example.com (locked out user) cannot login")
    public void testLockedOutUserLogin() {
        loginPage.login("alice@example.com", "10203040");

        // Locked user should see an error or remain on login page
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Locked out user should remain on login page");
    }

    @Test(groups = { "regression", "login" }, description = "Verify tap on saved username auto-fills field")
    @Severity(SeverityLevel.NORMAL)
    @Story("Auto-fill Credentials")
    @Description("Test tapping pre-populated username fills the username field")
    public void testTapSavedUsername() {
        loginPage.tapSavedUsername();
        loginPage.enterPassword("10203040").tapLogin();

        Assert.assertTrue(catalogPage.isPageLoaded(),
                "Should login successfully after tapping saved credentials");
    }

    @Test(groups = { "smoke", "login" }, description = "Verify login page UI elements are displayed")
    @Severity(SeverityLevel.NORMAL)
    @Story("Login Page UI")
    @Description("Verify all login page elements are present")
    public void testLoginPageElements() {
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button should be visible");
        Assert.assertNotNull(loginPage.getLoginTitle(), "Login title should be visible");
    }
}
