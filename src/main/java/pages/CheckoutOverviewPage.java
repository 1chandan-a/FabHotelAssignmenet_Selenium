package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutOverviewPage {

    private final WebDriver driver;

    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By finishButton = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
    }

    public double getSubtotal() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(subtotalLabel));
        String text = driver.findElement(subtotalLabel).getText().replace("Item total: $", "");
        return Double.parseDouble(text);
    }

    public void clickFinish() {

        driver.findElement(finishButton).click();
    }
}