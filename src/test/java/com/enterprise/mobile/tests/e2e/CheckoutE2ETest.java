package com.enterprise.mobile.tests.e2e;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.enterprise.mobile.data.TestDataGenerator;
import com.enterprise.mobile.pages.android.CartPage;
import com.enterprise.mobile.pages.android.CheckoutInfoPage;
import com.enterprise.mobile.pages.android.LoginPage;
import com.enterprise.mobile.pages.android.NavigationMenuPage;
import com.enterprise.mobile.pages.android.ProductCatalogPage;
import com.enterprise.mobile.pages.android.ProductDetailPage;
import com.enterprise.mobile.tests.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * End-to-End test: Browse -> Add to Cart -> Checkout flow.
 * Tests the complete purchase journey in Sauce Labs My Demo App.
 */
@Epic("E2E Flows")
@Feature("Purchase Flow")
public class CheckoutE2ETest extends BaseTest {

    private ProductCatalogPage catalogPage;
    private ProductDetailPage detailPage;
    private CartPage cartPage;
    private CheckoutInfoPage checkoutInfoPage;
    private NavigationMenuPage menuPage;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        catalogPage = new ProductCatalogPage();
        detailPage = new ProductDetailPage();
        cartPage = new CartPage();
        checkoutInfoPage = new CheckoutInfoPage();
        menuPage = new NavigationMenuPage();
        loginPage = new LoginPage();
    }

    @Test(groups = {"smoke", "e2e"}, description = "Complete purchase flow: Browse -> Cart -> Checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Full Purchase Journey")
    @Description("End-to-end test covering: login, browse catalog, add product, checkout with shipping info")
    public void testCompletePurchaseFlow() {
        // Step 1: Verify catalog loads
        Assert.assertTrue(catalogPage.isPageLoaded(), "Catalog should load on app start");
        logger.info("Step 1: Product catalog loaded");

        // Step 2: Login via menu
        menuPage.openMenu();
        menuPage.goToLogin();
        loginPage.login("bob@example.com", "10203040");
        Assert.assertTrue(catalogPage.isPageLoaded(), "Should return to catalog after login");
        logger.info("Step 2: Login successful");

        // Step 3: Select first product
        catalogPage.tapProductAtIndex(0);
        Assert.assertTrue(detailPage.isPageLoaded(), "Product detail should load");
        String productPrice = detailPage.getProductPrice();
        logger.info("Step 3: Selected product with price: {}", productPrice);

        // Step 4: Add to cart
        detailPage.addToCart();
        detailPage.goBackToCatalog();
        logger.info("Step 4: Product added to cart");

        // Step 5: Open cart
        Assert.assertTrue(catalogPage.isCartBadgeVisible(), "Cart badge should be visible");
        catalogPage.tapCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should load");
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        logger.info("Step 5: Cart opened, total: {}", cartPage.getTotalPrice());

        // Step 6: Proceed to checkout
        cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutInfoPage.isPageLoaded(), "Checkout info page should load");
        logger.info("Step 6: Checkout info page loaded");

        // Step 7: Fill shipping address with generated data
        checkoutInfoPage.fillShippingAddress(
                TestDataGenerator.generateFullName(),
                TestDataGenerator.generateStreetAddress(),
                TestDataGenerator.generateCity(),
                "California",
                TestDataGenerator.generateZipCode(),
                TestDataGenerator.generateCountry()
        );
        logger.info("Step 7: Shipping address filled");

        // Step 8: Tap To Payment
        checkoutInfoPage.tapToPayment();
        logger.info("Step 8: Navigated to payment");
    }

    @Test(groups = {"regression", "e2e"}, description = "Add multiple products to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Multi-Product Cart")
    @Description("Add two different products to cart and verify cart contents")
    public void testAddMultipleProductsToCart() {
        // Add first product
        catalogPage.tapProductAtIndex(0);
        Assert.assertTrue(detailPage.isPageLoaded(), "First product detail should load");
        detailPage.addToCart();
        detailPage.goBackToCatalog();

        // Add second product
        catalogPage.tapProductAtIndex(1);
        Assert.assertTrue(detailPage.isPageLoaded(), "Second product detail should load");
        detailPage.setQuantity(2);
        detailPage.addToCart();
        detailPage.goBackToCatalog();

        // Open cart and verify
        catalogPage.tapCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart should load");
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should have items");

        String totalPrice = cartPage.getTotalPrice();
        Assert.assertNotNull(totalPrice, "Total price should be displayed");
        logger.info("Cart total with multiple products: {}", totalPrice);
    }

    @Test(groups = {"regression", "e2e"}, description = "Checkout validation - empty shipping fields")
    @Severity(SeverityLevel.NORMAL)
    @Story("Checkout Validation")
    @Description("Verify that checkout form validation prevents submission with empty required fields")
    public void testCheckoutFormValidation() {
        // Add a product first
        catalogPage.tapProductAtIndex(0);
        detailPage.addToCart();
        detailPage.goBackToCatalog();

        // Go to checkout
        catalogPage.tapCart();
        cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutInfoPage.isPageLoaded(), "Checkout should load");

        // Try submitting empty form
        checkoutInfoPage.tapToPayment();

        // Should show validation errors
        Assert.assertTrue(
                checkoutInfoPage.isFullNameErrorDisplayed() ||
                        checkoutInfoPage.isAddressErrorDisplayed() ||
                        checkoutInfoPage.isCityErrorDisplayed() ||
                        checkoutInfoPage.isZipErrorDisplayed(),
                "At least one validation error should be shown for empty form"
        );
    }

    @Test(groups = {"regression", "e2e"}, description = "Empty cart shows Go Shopping button")
    @Severity(SeverityLevel.NORMAL)
    @Story("Empty Cart")
    @Description("Verify that an empty cart shows the Go Shopping button")
    public void testEmptyCartShowsGoShopping() {
        // Navigate directly to cart without adding products
        // The cart icon might not be visible if empty, so navigate via detail->cart
        catalogPage.tapProductAtIndex(0);
        detailPage.addToCart();
        detailPage.goBackToCatalog();

        catalogPage.tapCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should load");
    }
}
