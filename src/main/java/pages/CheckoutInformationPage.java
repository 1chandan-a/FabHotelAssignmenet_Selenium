package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtil;
import utils.LoggerUtil;
import utils.ConfigUtil;
import org.slf4j.Logger;

public class CheckoutInformationPage {

    private final WebDriver driver;

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");

    public CheckoutInformationPage(WebDriver driver) {
        this.driver = driver;
    }

    private static final Logger logger = LoggerUtil.getLogger(CheckoutInformationPage.class);

    public void enterInformation(String firstName, String lastName, String zipCode) {
        WaitUtil.waitForVisibility(driver, firstNameInput, Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds")));
        driver.findElement(firstNameInput).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(postalCodeInput).sendKeys(zipCode);
        WaitUtil.clickWhenReady(driver, continueButton, Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds")));
        logger.info("After clicking continue, current URL: {}", driver.getCurrentUrl());
        logger.info("Summary subtotal elements found: {}", driver.findElements(By.className("summary_subtotal_label")).size());
        WaitUtil.waitForVisibility(driver, By.className("summary_subtotal_label"), Integer.parseInt(ConfigUtil.getKey("explicit.wait.seconds")));
    }
}