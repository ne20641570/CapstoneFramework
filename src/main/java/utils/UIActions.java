package utils;

import base.ui.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class UIActions {

    // Get driver dynamically from DriverFactory
    private static WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    private static WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(15));
    }

    private static Actions getActions() {
        return new Actions(getDriver());
    }

    // =================== BASIC ACTIONS ===================
    public static void click(By locator) {
        getWait().until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
    public static boolean clickable(By locator) {
        if(getWait().until(ExpectedConditions.elementToBeClickable(locator))==null){
            return false;
        }else{
            return true;
        }
    }

    public static void sendKeys(By locator, String text) {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public static String getText(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }
    public static String getTitle() {
        return getDriver().getTitle();
    }

    public static boolean isDisplayed(By locator) {
        try {
            return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // =================== DROPDOWN ===================
    public static void selectByVisibleText(By locator, String text) {
        Select select = new Select(getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)));
        select.selectByVisibleText(text);
    }

    public static void selectByValue(By locator, String value) {
        Select select = new Select(getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)));
        select.selectByValue(value);
    }

    public static void selectByIndex(By locator, int index) {
        Select select = new Select(getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)));
        select.selectByIndex(index);
    }

    // =================== MOUSE ACTIONS ===================
    public static void hover(By locator) {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        getActions().moveToElement(element).perform();
    }

    public static void doubleClick(By locator) {
        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        getActions().doubleClick(element).perform();
    }

    public static void rightClick(By locator) {
        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        getActions().contextClick(element).perform();
    }

    public static void dragAndDrop(By source, By target) {
        WebElement src = getWait().until(ExpectedConditions.visibilityOfElementLocated(source));
        WebElement trg = getWait().until(ExpectedConditions.visibilityOfElementLocated(target));
        getActions().dragAndDrop(src, trg).perform();
    }

    // =================== JAVASCRIPT ACTIONS ===================
    public static void jsClick(By locator) {
        WebElement element = getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
    }

    public static void scrollToElement(By locator) {
        WebElement element = getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    public static void scrollToElement(WebElement elements) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", elements);
    }

    public static void scrollBy(int x, int y) {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    // =================== ALERT HANDLING ===================
    public static void acceptAlert() {
        getWait().until(ExpectedConditions.alertIsPresent()).accept();
    }

    public static void dismissAlert() {
        getWait().until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    public static String getAlertText() {
        return getWait().until(ExpectedConditions.alertIsPresent()).getText();
    }

    public static void sendAlertText(String text) {
        getWait().until(ExpectedConditions.alertIsPresent()).sendKeys(text);
    }

    // =================== FRAME HANDLING ===================
    public static void switchToFrame(By locator) {
        WebElement frame = getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        getDriver().switchTo().frame(frame);
    }

    public static void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    // =================== WINDOW HANDLING ===================
    public static void switchToWindow(String windowTitle) {
        for (String window : getDriver().getWindowHandles()) {
            getDriver().switchTo().window(window);
            if (getDriver().getTitle().equals(windowTitle)) {
                break;
            }
        }
    }

    // =================== CHECKBOX & RADIO ===================
    public static void check(By locator) {
        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        if (!element.isSelected()) element.click();
    }

    public static void uncheck(By locator) {
        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        if (element.isSelected()) element.click();
    }

    public static boolean isSelected(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).isSelected();
    }

    // =================== LIST HANDLING ===================
    public static WebElement getElement(By locator) {
        return getDriver().findElement(locator);
    }
    public static List<WebElement> getElements(By locator) {
        return getDriver().findElements(locator);
    }

    public static int getElementCount(By locator) {
        return getElements(locator).size();
    }

    // =================== NAVIGATION ===================
    public static void openUrl(String url) {
        getDriver().get(url);
    }

    public static void refreshPage() {
        getDriver().navigate().refresh();
    }

    public static void goBack() {
        getDriver().navigate().back();
    }

    public static void goForward() {
        getDriver().navigate().forward();
    }

    // =================== SCREENSHOT ===================
    public static byte[] takeScreenshot() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}