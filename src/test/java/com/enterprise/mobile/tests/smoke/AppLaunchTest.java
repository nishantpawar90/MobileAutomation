package com.enterprise.mobile.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enterprise.mobile.pages.android.ProductCatalogPage;
import com.enterprise.mobile.tests.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

/**
 * Single smoke test to verify app launches and catalog is visible.
 */
public class AppLaunchTest extends BaseTest {

    private ProductCatalogPage catalogPage;

    @BeforeMethod
    public void initPages() {
        catalogPage = new ProductCatalogPage();
    }

    @Test(description = "Verify app launches and product catalog is displayed")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Simple test: app launches -> product catalog page is visible")
    public void testAppLaunchShowsCatalog() {
        logger.info("Checking if catalog page is loaded...");
        boolean loaded = catalogPage.isPageLoaded();
        logger.info("Catalog page loaded: {}", loaded);

        if (loaded) {
            String title = catalogPage.getPageTitle();
            logger.info("Page title: {}", title);
            Assert.assertEquals(title, "Products", "Title should be 'Products'");
        } else {
            // Log page source for debugging
            logger.error("Page source: {}", driver.getPageSource().substring(0, Math.min(2000, driver.getPageSource().length())));
            Assert.fail("Product catalog page did not load within timeout");
        }
    }
}
