package utils;

import base.ui.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtils {

    public static String capture(String testName) {

        File src = ((TakesScreenshot) DriverFactory.getDriver())
                .getScreenshotAs(OutputType.FILE);

        String path = "reports/" + testName + ".png";

        try {
            Files.copy(
                    src.toPath(),
                    Paths.get(path),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }
}