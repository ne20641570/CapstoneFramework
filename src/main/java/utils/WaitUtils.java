package utils;

import base.ui.DriverFactory;
import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class WaitUtils {

    public static WebElement explicitWait(By locator) {
        return new WebDriverWait(
                DriverFactory.getDriver(),
                Duration.ofSeconds(Integer.parseInt(ConfigReader.get("explicitWait")))
        ).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement fluentWait(By locator) {

        Wait<WebDriver> wait = new FluentWait<>(DriverFactory.getDriver())
                .withTimeout(Duration.ofSeconds(Integer.parseInt(ConfigReader.get("fluentWait"))))
                .pollingEvery(Duration.ofSeconds(Integer.parseInt(ConfigReader.get("polling"))))
                .ignoring(Exception.class);

        return wait.until(driver -> driver.findElement(locator));
    }
}