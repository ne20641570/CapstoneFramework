package ui;

import base.ui.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MarketWatchPage;
import pages.MarketsPage;
import utils.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class BseIndia extends BaseTest {
    ExtentTest tests = ExtentTestManager.getTest();
    ExtentTest test;
    @BeforeMethod
    public void beforeMethod(Method method) {
        tests = ExtentTestManager.startTest(method.getName(), "Test description");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void MarketPageDetails() throws IOException {
        HomePage home = new HomePage();
        MarketWatchPage marketWatchPage = new MarketWatchPage();
        MarketsPage marketsPage = new MarketsPage();

        test=ExtentTestManager.startNode(tests,"Home page and closing popup if present");
        home.verifyTitle(test);
        ExtentReportUtil.logInfo(test,"Title verified");
        ExtentReportUtil.logScreenshot(test,"Pop Up");
        home.closePopup();
        ExtentReportUtil.logInfo(test,"Popup closed if present");


        test=ExtentTestManager.startNode(tests,"Navigating to Market Watch page");
        ExtentReportUtil.logScreenshot(test,"Market_Watch");
        marketWatchPage.navigateToMarketWatch(test);
        ExtentReportUtil.logInfo(test,"Navigated to Market Watch page");

        test=ExtentTestManager.startNode(tests,"Capturing all the listed instruments into an excel");
        marketWatchPage.captureAllInstruments(test);
        ExtentReportUtil.logInfo(test,"Navigated to Market Watch page");

        test=ExtentTestManager.startNode(tests,"grouping type of instruments and underlying into an excel");
        marketWatchPage.groupingInsrumentsUnderlying(test);
        ExtentReportUtil.logInfo(test,"Grouped type of instruments and underlying into an excel");

        test=ExtentTestManager.startNode(tests,"Getting highest National turn over by group and capturing into an excel");
        marketWatchPage.groupingInsrumentsUnderlyingWithHighest(test);
        ExtentReportUtil.logInfo(test,"Got highest National turn over by group and captured into an excel");

        test=ExtentTestManager.startNode(tests,"Getting highest National turn over and capturing into an excel");
        marketWatchPage.captureHighestTurnOver(test);
        ExtentReportUtil.logInfo(test,"Got highest National turn over and captured into an excel");


        test=ExtentTestManager.startNode(tests,"choose Markets page");
        marketsPage.chooseMarketsPage(test);
        ExtentReportUtil.logInfo(test,"choosed Markets page");

        test=ExtentTestManager.startNode(tests,"All Index heat map Details into an excel");
        marketsPage.writeAllIndexHeatmapData(test);
        ExtentReportUtil.logInfo(test,"All Index heat map Details into an excel");

        test=ExtentTestManager.startNode(tests,"Index heat map section group indexes into two categories ");
        marketsPage.groupIndexes(test);
        ExtentReportUtil.logInfo(test,"Index heat map section group indexes into two categories ");

        test=ExtentTestManager.startNode(tests,"Index made highest gains and losses and capture into an excel");
        marketsPage.highestChange(test);
        ExtentReportUtil.logInfo(test,"Index made highest gains and losses and capture into an excel");

        test=ExtentTestManager.startNode(tests,"Indexes which gained more than 50 points");
        marketsPage.greaterThan50(test);
        ExtentReportUtil.logInfo(test,"Indexes which gained more than 50 points");


        ExtentReportUtil.logPass(test,"Test passed");
    }
}