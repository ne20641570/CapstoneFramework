package listeners;

import org.testng.*;
import utils.ExtentManager;
import utils.ScreenshotUtils;
import com.aventstack.extentreports.*;

public class TestListener implements ITestListener {

    ExtentReports extent = ExtentManager.getInstance();
    ExtentTest test;

    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getName());
    }

    public void onTestFailure(ITestResult result) {
        String path = ScreenshotUtils.capture(result.getName());
        test.fail(result.getThrowable())
                .addScreenCaptureFromPath(path);
    }

    public void onTestSuccess(ITestResult result) {
        test.pass("Test Passed");
    }

    public void onFinish(ITestContext context) {
        extent.flush();
    }
}