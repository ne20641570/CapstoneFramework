package base.ui;

import config.ConfigReader;
import listeners.TestListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ExcelUtils;
import utils.ExtentManager;

@Listeners(TestListener.class)
public class BaseTest {

    @BeforeMethod
    public void setup() {
        DriverFactory.initDriver();
        DriverFactory.getDriver().get(ConfigReader.get("url"));
        ExtentManager.getInstance();
        ExcelUtils.deleteExcelFile();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quit();
        ExtentManager.flushReports();
    }
}