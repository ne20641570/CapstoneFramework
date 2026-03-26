package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    public static String reportFolderPath;

    public static ExtentReports getInstance() {

        if (extent == null) {

            // Create timestamp for folder
            String timestamp = new SimpleDateFormat("yyyyMMdd_HH").format(new Date());

            // Folder structure: reports/extentReports/yyyyMMdd_HH/report.html
            reportFolderPath = "reports/extentReports/" + timestamp + "/";

            // Create folders if not exist
            new File(reportFolderPath).mkdirs();

            ExtentSparkReporter reporter = new ExtentSparkReporter(reportFolderPath + "report.html");

            reporter.config().setReportName("Automation Test Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }

        return extent;
    }

    // Flush the report (call at end of all tests)
    public static void flushReports() {
        extent.flush();
    }
}