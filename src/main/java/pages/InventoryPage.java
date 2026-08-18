package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LoggerUtil;
import utils.WaitUtil;
import utils.ConfigUtil;
import org.slf4j.Logger;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {

    private static final Logger logger = LoggerUtil.getLogger(InventoryPage.class);

    private final WebDriver driver;

    private final By pageTitle = By.className("title");
    private final By sortDropdown = By.className("product_sort_container");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemPrice = By.className("inventory_item_price");
    private final By itemName = By.className("inventory_item_name");
    private final By addToCartButton = By.cssSelector("button.btn_inventory");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDisplayed() {
        return driver.findElement(pageTitle).getText().equalsIgnoreCase("Products");
    }

    public void sortByPriceLowToHigh() {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByValue("lohi");
    }

    public static class ProductInfo {
        public String name;
        public double price;
        public WebElement button;

        public ProductInfo(String name, double price, WebElement button) {
            this.name = name;
            this.price = price;
            this.button = button;
        }
    }

    public List<ProductInfo> addCheapestAndMostExpensiveProducts() {
        List<WebElement> items = driver.findElements(inventoryItems);
        List<ProductInfo> products = new ArrayList<>();

        for (WebElement item : items) {
            String name = item.findElement(itemName).getText();

            String priceText = item.findElement(itemPrice)
                    .getText()
                    .replace("$", "");

            double price = Double.parseDouble(priceText);

            WebElement button = item.findElement(addToCartButton);

            products.add(new ProductInfo(name, price, button));
        }

        ProductInfo cheapest = products.stream().min(Comparator.comparingDouble(p -> p.price)).orElseThrow();
        ProductInfo mostExpensive = products.stream().max(Comparator.comparingDouble(p -> p.price)).orElseThrow();

        int waitSecs = Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds"));
        clickAddButtonByProductName(cheapest.name, waitSecs);
        clickAddButtonByProductName(mostExpensive.name, waitSecs);

        logger.info("Clicked add-to-cart for: {} (${}) and {} (${})", cheapest.name, cheapest.price, mostExpensive.name, mostExpensive.price);

        // Wait for the cart badge to reflect the two added items
        int expectedCount = 2;
        try {
            int waitTime = Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds"));
            new WebDriverWait(driver, Duration.ofSeconds(waitTime))
                    .until(ExpectedConditions.textToBe(cartBadge, String.valueOf(expectedCount)));
        } catch (Exception e) {
            logger.warn("Cart badge did not reach expected count {} within timeout", expectedCount);
        }
        return List.of(cheapest,mostExpensive);

    }

    private void clickAddButtonByProductName(String productName, int waitSecs) {
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        for (WebElement item : items) {
            try {
                String name = item.findElement(itemName).getText().trim();
                if (name.equals(productName)) {
                    WebElement button = item.findElement(By.cssSelector("button.btn_inventory"));
                    WaitUtil.clickWhenReady(driver, button, waitSecs);
                    return;
                }
            } catch (NoSuchElementException ignored) {
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Add button for product '" + productName + "' not found using CSS fallback");
    }

    public int getCartBadgeCount() {
        var badges = driver.findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        String text = badges.get(0).getText();
        return Integer.parseInt(text);
    }

    public void goToCart() {
        WaitUtil.clickWhenReady(driver, cartLink, Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds")));
        WaitUtil.waitForVisibility(driver, By.id("checkout"), Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds")));
    }
}