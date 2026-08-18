package tests;

import pages.*;
import utils.ConfigUtil;
import utils.PdfUtil;
import utils.TestListener;
import utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.util.List;
@Listeners(TestListener.class)
public class EndToEndPurchaseTest extends BaseTest {
    @Test(description = "Verify end-to-end purchase flow including PDF order summary generation & assertion",
            retryAnalyzer = RetryAnalyzer.class)
    public void testEndToEndPurchaseFlow() throws IOException {

        // 1. Login using valid credentials
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigUtil.getKey("user.name"), "secret_sauce");

        // 2. Verify the Inventory page is displayed
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isDisplayed(), "Inventory page was not displayed after login.");

        // 3. Sort products by Price (Low to High)
        inventoryPage.sortByPriceLowToHigh();

        // 4. Add the cheapest and the most expensive products to the cart
        List<InventoryPage.ProductInfo> selected = inventoryPage.addCheapestAndMostExpensiveProducts();
        InventoryPage.ProductInfo cheapest = selected.get(0);
        InventoryPage.ProductInfo expensive = selected.get(1);

        double expectedSubtotal = cheapest.price + expensive.price;
        // 5. Verify the cart count
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart count did not match expected value of 2.");

        // Navigate to Cart
        inventoryPage.goToCart();

        // Navigate to Checkout Information
        CartPage cartPage = new CartPage(driver);
        cartPage.clickCheckout();

        // Complete shipping details
        CheckoutInformationPage infoPage = new CheckoutInformationPage(driver);
        infoPage.enterInformation("Chandan", "QA", "10001");

        // Checkout Overview Validation
        CheckoutOverviewPage overviewPage = new CheckoutOverviewPage(driver);

        Assert.assertEquals(overviewPage.getSubtotal(), expectedSubtotal, 0.01, "Overview subtotal does not match sum of selected items.");

        // Step 9: Generate PDF of Order Summary & verify details
        String pdfFilePath = PdfUtil.generatePagePdf(driver, "Order_Summary.pdf");
        String pdfContent = PdfUtil.extractPdfText(pdfFilePath);

        // Verify PDF Contents
        Assert.assertTrue(pdfContent.contains(cheapest.name), "PDF missing cheapest item name: " + cheapest.name);
        Assert.assertTrue(pdfContent.contains(expensive.name), "PDF missing expensive item name: " + expensive.name);
        Assert.assertTrue(pdfContent.contains(String.valueOf(cheapest.price)), "PDF missing cheapest item price.");
        Assert.assertTrue(pdfContent.contains(String.valueOf(expensive.price)), "PDF missing expensive item price.");

        // 6. Complete the checkout process
        overviewPage.clickFinish();

        // 7. Verify the order confirmation page
        CheckoutCompletePage completePage = new CheckoutCompletePage(driver);
        Assert.assertEquals(completePage.getConfirmationHeader(), "Thank you for your order!", "Order confirmation header failed.");
    }
}