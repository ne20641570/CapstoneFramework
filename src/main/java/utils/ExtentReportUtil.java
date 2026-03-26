package utils;

import base.ui.DriverFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportUtil {
    private static final String SCREENSHOT_FOLDER = "reports/screenshots/";

    // Log info to current thread's test and console
    public static void logInfo(ExtentTest test, String message) {
        if (test != null) {
            test.info(message);
        }
        System.out.println("[INFO] " + message);
    }

    // Log pass status
    public static void logPass(ExtentTest test, String message) {

        if (test != null) {
            test.pass(message);
        }
        System.out.println("[PASS] " + message);
    }

    // Log fail status
    public static void logFail(ExtentTest test,String message) {

        if (test != null) {
            test.fail(message);
        }
        System.err.println("[FAIL] " + message);
    }

    private static WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
    public static void logScreenshot(ExtentTest test,String message) {
        try {
            // Create screenshots folder if not exists
            File folder = new File(SCREENSHOT_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Capture screenshot as bytes
            byte[] src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            // Capture screenshot as base64 string
            String base64 = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);

            // Prepare unique filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HH"));
            String filePath = SCREENSHOT_FOLDER + timestamp + "screenshot_" +message+ ".png";

            // Save screenshot PNG file
            Files.write(Paths.get(filePath), src);

            // Attach screenshot to extent report test with base64

            if (test != null) {
                test.info(message,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            }

            System.out.println("[INFO] Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save screenshot: " + e.getMessage());
        }
    }
}