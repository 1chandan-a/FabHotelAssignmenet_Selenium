package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import utils.ConfigUtil;
import utils.LoggerUtil;
import utils.WaitUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventoryPage {

    private static final Logger logger = LoggerUtil.getLogger(InventoryPage.class);

    private final WebDriver driver;

    private final By pageTitle = By.className("title");
    private final By sortDropdown = By.className("product_sort_container");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemPrice = By.className("inventory_item_price");
    private final By itemName = By.className("inventory_item_name");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDisplayed() {
        return driver.findElement(pageTitle).getText().equalsIgnoreCase("Products");
    }

    public void sortByPriceLowToHigh() {
        new Select(driver.findElement(sortDropdown)).selectByValue("lohi");
    }

    public static class ProductInfo {
        public String name;
        public double price;

        public ProductInfo(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    public List<ProductInfo> addCheapestAndMostExpensiveProducts() {
        List<ProductInfo> products = new ArrayList<>();
        for (WebElement item : driver.findElements(inventoryItems)) {

            String name = item.findElement(itemName).getText();

            double price = Double.parseDouble(item.findElement(itemPrice)
                            .getText()
                            .replace("$", ""));
            products.add(new ProductInfo(name, price));
        }

        ProductInfo cheapest = products.stream()
                .min(Comparator.comparingDouble(p -> p.price)).orElseThrow();

        ProductInfo mostExpensive = products.stream()
                .max(Comparator.comparingDouble(p -> p.price)).orElseThrow();

        int waitSecs = Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds"));
        clickAddButtonByProductName(cheapest.name, waitSecs);
        waitForCartCount(1, waitSecs);
        clickAddButtonByProductName(mostExpensive.name, waitSecs);
        waitForCartCount(2, waitSecs);

        logger.info("Added to cart: {} (${}) and {} (${})", cheapest.name, cheapest.price,
                mostExpensive.name, mostExpensive.price);

        return List.of(cheapest, mostExpensive);
    }

    private void clickAddButtonByProductName(String productName, int waitSecs) {
        String xpath = "//div[contains(@class,'inventory_item')" +
                " and .//div[contains(@class,'inventory_item_name') and normalize-space()='" +
                productName + "']]//button";
        By addButton = By.xpath(xpath);
        WaitUtil.clickWhenReady(driver, addButton, waitSecs);
    }

    private void waitForCartCount(int expectedCount, int waitSecs) {

        new WebDriverWait(driver, java.time.Duration.ofSeconds(waitSecs)
        ).until(driver -> getCartBadgeCount() == expectedCount);
    }

    public int getCartBadgeCount() {

        List<WebElement> badges = driver.findElements(cartBadge);

        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText());
    }

    public void goToCart() {
        int waitSecs = Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds"));
        WaitUtil.clickWhenReady(driver, cartLink, waitSecs);
        WaitUtil.waitForVisibility(driver, By.id("checkout"), waitSecs);
    }
}