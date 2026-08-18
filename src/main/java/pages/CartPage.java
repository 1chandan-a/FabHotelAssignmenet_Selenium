package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtil;
import utils.LoggerUtil;
import utils.ConfigUtil;
import org.slf4j.Logger;

public class CartPage {

    private final WebDriver driver;
    private final By checkoutButton = By.id("checkout");
    private static final Logger logger = LoggerUtil.getLogger(CartPage.class);

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickCheckout() {
        WaitUtil.clickWhenReady(driver, checkoutButton, Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds")));
        WaitUtil.waitForVisibility(driver, By.id("first-name"), Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds")));
        logger.info("Navigated to checkout information page");
    }
}