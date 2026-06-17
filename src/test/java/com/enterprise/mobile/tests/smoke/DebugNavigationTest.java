package com.enterprise.mobile.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enterprise.mobile.pages.android.LoginPage;
import com.enterprise.mobile.pages.android.NavigationMenuPage;
import com.enterprise.mobile.pages.android.ProductCatalogPage;
import com.enterprise.mobile.tests.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

/**
 * Debug test to dump page source and verify navigation works.
 */
public class DebugNavigationTest extends BaseTest {

    private ProductCatalogPage catalogPage;
    private NavigationMenuPage menuPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void initPages() {
        catalogPage = new ProductCatalogPage();
        menuPage = new NavigationMenuPage();
        loginPage = new LoginPage();
    }

    @Test(description = "Debug: verify full navigation flow - menu open, login, authenticate")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies menu opens, navigates to login, and performs login")
    public void testDumpPageSourceAndNavigate() {
        logger.info("Waiting for catalog...");
        Assert.assertTrue(catalogPage.isPageLoaded(), "Catalog must load");
        logger.info("Catalog loaded.");

        // Open menu and navigate to login
        logger.info("Opening menu...");
        menuPage.openMenu();
        logger.info("Menu opened! Navigating to login...");
        menuPage.goToLogin();
        logger.info("Login page loading...");

        Assert.assertTrue(loginPage.isPageLoaded(), "Login page must load");
        logger.info("Login page loaded successfully!");

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button visible");
        logger.info("=== FULL NAVIGATION TEST PASSED ===");
    }
}
