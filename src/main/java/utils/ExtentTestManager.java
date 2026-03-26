package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    private static Map<Long, ExtentTest> extentTestMap = new HashMap<>();
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ExtentTest test;
    // Create a new test for current thread and store it
    public static synchronized ExtentTest startTest(String testName, String description) {
        test = extent.createTest(testName, description);
        extentTestMap.put(Thread.currentThread().getId(), test);
        return test;
    }
    public static synchronized ExtentTest startNode(ExtentTest test,String nodeName) {
        test.createNode(nodeName);
        extentTestMap.put(Thread.currentThread().getId(), test);
        return test;
    }

    // Get test instance for current thread
    public static synchronized ExtentTest getTest() {
        return extentTestMap.get(Thread.currentThread().getId());
    }


}