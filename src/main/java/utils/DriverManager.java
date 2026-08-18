package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void initializeDriver(String browser) {

        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", "true"));

        WebDriver driverInstance;

        switch (browser.toLowerCase()) {

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }

                driverInstance = new FirefoxDriver(firefoxOptions);
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();

                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }

                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-notifications");

                driverInstance = new ChromeDriver(chromeOptions);
                break;
        }

        driverInstance.manage().window().maximize();
        driver.set(driverInstance);
    }

    public static void quitDriver() {
        WebDriver driverInstance = driver.get();

        if (driverInstance != null) {
            driverInstance.quit();
            driver.remove();
        }
    }
}