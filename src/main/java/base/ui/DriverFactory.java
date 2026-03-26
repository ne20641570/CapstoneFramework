package base.ui;

import config.ConfigReader;
import enums.BrowserType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {

        String browser = ConfigReader.get("browser").toUpperCase();
        BrowserType type = BrowserType.valueOf(browser);

        switch (type) {

            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver(new ChromeOptions()));
                break;

            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver());
                break;

            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver.set(new EdgeDriver());
                break;
        }

        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Integer.parseInt(ConfigReader.get("implicitWait")))
        );
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quit() {
        driver.get().quit();
        driver.remove();
    }
}