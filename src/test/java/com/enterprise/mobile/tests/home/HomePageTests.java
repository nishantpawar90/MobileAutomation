package com.enterprise.mobile.tests.home;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enterprise.mobile.pages.android.ProductCatalogPage;
import com.enterprise.mobile.pages.android.ProductDetailPage;
import com.enterprise.mobile.pages.android.NavigationMenuPage;
import com.enterprise.mobile.tests.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * Product Catalog and Product Detail test cases for Sauce Labs My Demo App.
 * The app opens directly to the Product Catalog screen.
 */
@Epic("Product Catalog")
@Feature("Browsing Products")
public class HomePageTests extends BaseTest {

    private ProductCatalogPage catalogPage;
    private ProductDetailPage detailPage;
    private NavigationMenuPage menuPage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        catalogPage = new ProductCatalogPage();
        detailPage = new ProductDetailPage();
        menuPage = new NavigationMenuPage();
    }

    @Test(groups = {"smoke", "catalog"}, description = "Verify product catalog loads on app start")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Catalog Load")
    @Description("Verify that the product catalog page loads when the app is opened")
    public void testProductCatalogLoads() {
        Assert.assertTrue(catalogPage.isPageLoaded(),
                "Product catalog should load as the home screen");
        Assert.assertEquals(catalogPage.getPageTitle(), "Products",
                "Page title should be 'Products'");
    }

    @Test(groups = {"smoke", "catalog"}, description = "Verify products are displayed in the catalog")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Listing")
    @Description("Verify that products with titles and prices are displayed")
    public void testProductsAreDisplayed() {
        Assert.assertTrue(catalogPage.getVisibleProductCount() > 0,
                "At least one product should be visible");

        List<String> titles = catalogPage.getProductTitles();
        Assert.assertFalse(titles.isEmpty(), "Product titles should not be empty");
        logger.info("Found {} products: {}", titles.size(), titles);
    }

    @Test(groups = {"smoke", "catalog"}, description = "Verify product prices are displayed")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Pricing")
    @Description("Verify that product prices are shown with dollar sign")
    public void testProductPricesDisplayed() {
        List<String> prices = catalogPage.getProductPrices();
        Assert.assertFalse(prices.isEmpty(), "Product prices should not be empty");

        for (String price : prices) {
            Assert.assertTrue(price.startsWith("$"),
                    "Price should start with '$' but was: " + price);
        }
    }

    @Test(groups = {"regression", "catalog"}, description = "Verify tapping product opens detail page")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Navigation")
    @Description("Verify that tapping a product opens the product detail page")
    public void testTapProductOpensDetail() {
        catalogPage.tapProductAtIndex(0);

        Assert.assertTrue(detailPage.isPageLoaded(),
                "Product detail page should be displayed after tapping a product");

        String price = detailPage.getProductPrice();
        Assert.assertNotNull(price, "Product price should be displayed on detail page");
        Assert.assertTrue(price.startsWith("$"), "Price should start with '$'");
    }

    @Test(groups = {"regression", "catalog"}, description = "Verify product detail shows quantity controls")
    @Severity(SeverityLevel.NORMAL)
    @Story("Quantity Controls")
    @Description("Verify plus/minus buttons and quantity display on product detail page")
    public void testProductDetailQuantityControls() {
        catalogPage.tapProductAtIndex(0);

        Assert.assertTrue(detailPage.isPageLoaded(), "Detail page should be loaded");
        Assert.assertEquals(detailPage.getQuantity(), 1, "Default quantity should be 1");

        detailPage.increaseQuantity();
        Assert.assertEquals(detailPage.getQuantity(), 2, "Quantity should be 2 after increase");

        detailPage.decreaseQuantity();
        Assert.assertEquals(detailPage.getQuantity(), 1, "Quantity should be back to 1");
    }

    @Test(groups = {"regression", "catalog"}, description = "Verify add to cart from product detail")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add to Cart")
    @Description("Verify adding a product to cart from the detail page")
    public void testAddProductToCart() {
        catalogPage.tapProductAtIndex(0);

        Assert.assertTrue(detailPage.isPageLoaded(), "Detail page should be loaded");
        detailPage.addToCart();

        // After adding to cart, navigate back to verify cart badge
        detailPage.goBackToCatalog();
        Assert.assertTrue(catalogPage.isCartBadgeVisible(),
                "Cart badge should be visible after adding a product");
    }

    @Test(groups = {"regression", "catalog"}, description = "Verify scrolling loads more products")
    @Severity(SeverityLevel.NORMAL)
    @Story("Scroll Catalog")
    @Description("Verify scrolling down reveals more products in the catalog")
    public void testScrollCatalog() {
        int initialCount = catalogPage.getVisibleProductCount();
        catalogPage.scrollDown();

        // After scrolling, we should still see products
        Assert.assertTrue(catalogPage.getVisibleProductCount() > 0,
                "Products should still be visible after scrolling");
    }

    @Test(groups = {"regression", "catalog"}, description = "Verify navigation menu opens")
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigation Menu")
    @Description("Verify the side navigation menu can be opened from catalog")
    public void testNavigationMenuOpens() {
        menuPage.openMenu();

        Assert.assertTrue(menuPage.isPageLoaded(),
                "Navigation menu should be displayed");
        Assert.assertTrue(menuPage.isLoginMenuItemVisible(),
                "Login menu item should be visible");
    }
}
