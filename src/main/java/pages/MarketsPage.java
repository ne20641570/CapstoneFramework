package pages;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.*;

import java.util.*;

public class MarketsPage {
    static ExtentTest test;

    private By marketsTab = By.xpath("//a[@class=\"nav-link\" and contains(@href,'markets.')]");
    private By indexHeatMapName = By.xpath("//a[contains(@href,\"/sensex/code\")]/child::div[@class=\"wraptext ng-binding\"]");
    private By indexHeatMapAmount = By.xpath("//a[contains(@href,\"/sensex/code\")]/child::div[@class=\"wraptext ng-binding\"]/following-sibling::div/strong[@class=\"ng-binding\"]");
    private By indexHeatMapshare = By.xpath("//a[contains(@href,\"/sensex/code\")]/child::div[@class=\"wraptext ng-binding\"]/following-sibling::div[@class=\"ng-binding\"]");




    public void chooseMarketsPage(ExtentTest tests) {
        test = ExtentTestManager.startNode(tests, "Choosing Markets page");
        do {
            UIActions.scrollToElement(marketsTab);
        }while (!UIActions.clickable(marketsTab));
        try {
            Thread.sleep(3000); // Wait for the page to load
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UIActions.scrollToElement(marketsTab);
        WaitUtils.explicitWait(marketsTab);
        UIActions.click(marketsTab);
        ExtentReportUtil.logInfo(test, "Clicked on Markets tab");
        ExtentReportUtil.logScreenshot(test, "Markets_Page");
        ExtentReportUtil.logPass(test, "Successfully navigated to Markets page");
    }

    public void writeAllIndexHeatmapData(ExtentTest tests) {

        test = ExtentTestManager.startNode(tests, "Writing all index heatmap data to Excel");

        List<String> names = getIndexNames();
        List<String> values = getIndexValues();
        List<String> changes = getIndexChangeText();

        String sheetName = "IndexHeatmap";

        try {
            // ✅ HEADER ROW
            ExcelUtils.writeMarket(sheetName, 0, 0, "Name");
            ExcelUtils.writeMarket(sheetName, 0, 1, "Value");
            ExcelUtils.writeMarket(sheetName, 0, 2, "Points");
            ExcelUtils.writeMarket(sheetName, 0, 3, "% Change");

            // ✅ DATA ROWS
            for (int i = 0; i < names.size(); i++) {

                String name = names.get(i);
                String value = values.get(i);

                String changeText = changes.get(i);

                double points = getPoints(changeText);
                String percent = getPercentage(changeText);

                int row = i + 1;

                ExcelUtils.writeMarket(sheetName, row, 0, name);
                ExcelUtils.writeMarket(sheetName, row, 1, value);
                ExcelUtils.writeMarket(sheetName, row, 2, String.valueOf(points));
                ExcelUtils.writeMarket(sheetName, row, 3, percent);
            }

            ExtentReportUtil.logPass(test, "Successfully wrote all index heatmap data");
            ExtentReportUtil.logScreenshot(test, "IndexHeatmap_Data");

        } catch (Exception e) {
            ExtentReportUtil.logFail(test, "Failed to write index heatmap data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void groupIndexes(ExtentTest tests) {
        test = ExtentTestManager.startNode(tests, "Grouping indexes into Gain & Loss tables");

        List<String> names = getIndexNames();
        List<String> values = getIndexValues();
        List<String> changes = getIndexChangeText();

        int gainRow = 0;
        int lossRow = 0;

        // Headers
        ExcelUtils.writeMarket("Gain", 0, 0, "Name");
        ExcelUtils.writeMarket("Gain", 0, 1, "Value");
        ExcelUtils.writeMarket("Gain", 0, 2, "Change");

        ExcelUtils.writeMarket("Loss", 0, 0, "Name");
        ExcelUtils.writeMarket("Loss", 0, 1, "Value");
        ExcelUtils.writeMarket("Loss", 0, 2, "Change");

        gainRow++;
        lossRow++;

        for (int i = 0; i < names.size(); i++) {

            double points = getPoints(changes.get(i));
            String percent = getPercentage(changes.get(i));

            if (points >= 0) {
                ExcelUtils.writeMarket("Gain", gainRow, 0, names.get(i));
                ExcelUtils.writeMarket("Gain", gainRow, 1, values.get(i));
                ExcelUtils.writeMarket("Gain", gainRow, 2, points + " " + percent);
                gainRow++;
            } else {
                ExcelUtils.writeMarket("Loss", lossRow, 0, names.get(i));
                ExcelUtils.writeMarket("Loss", lossRow, 1, values.get(i));
                ExcelUtils.writeMarket("Loss", lossRow, 2, points + " " + percent);
                lossRow++;
            }
        }
    }

    public void highestChange(ExtentTest tests) {
        test = ExtentTestManager.startNode(tests, "Finding highest gain and loss");

        List<String> names = getIndexNames();
        List<String> values = getIndexValues();
        List<String> changes = getIndexChangeText();

        double maxGain = Double.MIN_VALUE;
        double maxLoss = Double.MAX_VALUE;

        String gainIndex = "";
        String lossIndex = "";
        String gainValue = "";
        String lossValue = "";

        for (int i = 0; i < names.size(); i++) {

            double points = getPoints(changes.get(i));

            if (points > maxGain) {
                maxGain = points;
                gainIndex = names.get(i);
                gainValue = values.get(i);
            }

            if (points < maxLoss) {
                maxLoss = points;
                lossIndex = names.get(i);
                lossValue = values.get(i);
            }
        }

        // ✅ HEADER ROW (Row 0)
        ExcelUtils.writeMarket("HighLow", 0, 0, "Type");
        ExcelUtils.writeMarket("HighLow", 0, 1, "Index Name");
        ExcelUtils.writeMarket("HighLow", 0, 2, "Value");
        ExcelUtils.writeMarket("HighLow", 0, 3, "Points");

        // ✅ DATA ROWS (start from row 1)
        ExcelUtils.writeMarket("HighLow", 1, 0, "Highest Gain");
        ExcelUtils.writeMarket("HighLow", 1, 1, gainIndex);
        ExcelUtils.writeMarket("HighLow", 1, 2, gainValue);
        ExcelUtils.writeMarket("HighLow", 1, 3, String.valueOf(maxGain));

        ExcelUtils.writeMarket("HighLow", 2, 0, "Highest Loss");
        ExcelUtils.writeMarket("HighLow", 2, 1, lossIndex);
        ExcelUtils.writeMarket("HighLow", 2, 2, lossValue);
        ExcelUtils.writeMarket("HighLow", 2, 3, String.valueOf(maxLoss));
    }


    public void greaterThan50(ExtentTest tests) {
        test = ExtentTestManager.startNode(tests, "Indexes with >50 points");

        List<String> names = getIndexNames();
        List<String> values = getIndexValues();
        List<String> changes = getIndexChangeText();

        int row = 0;

        // Header
        ExcelUtils.writeMarket("Above50", row, 0, "Name");
        ExcelUtils.writeMarket("Above50", row, 1, "Value");
        ExcelUtils.writeMarket("Above50", row, 2, "Points");
        row++;

        for (int i = 0; i < names.size(); i++) {

            double points = getPoints(changes.get(i));

            if (points > 50) {
                ExcelUtils.writeMarket("Above50", row, 0, names.get(i));
                ExcelUtils.writeMarket("Above50", row, 1, values.get(i));
                ExcelUtils.writeMarket("Above50", row, 2, String.valueOf(points));
                row++;
            }
        }
    }

    public List<String> getIndexValues() {
        List<WebElement> elements = UIActions.getElements(indexHeatMapAmount);
        List<String> values = new ArrayList<>();

        for (WebElement el : elements) {
            values.add(el.getText().replace(",", "").trim());
        }
        return values;
    }

    public String getPercentage(String text) {
        try {
            if (text == null || text.trim().isEmpty()) return "0%";
            String[] parts = text.trim().split(" ");
            return parts.length > 1 ? parts[1] : "0%";
        } catch (Exception e) {
            return "0%";
        }
    }


    public List<String> getIndexNames() {
        List<WebElement> elements = UIActions.getElements(indexHeatMapName);
        List<String> names = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            names.add(elements.get(i).getText());
        }
        return names;
    }

    public List<String> getIndexChangeText() {
        List<WebElement> elements = UIActions.getElements(indexHeatMapshare);
        List<String> changes = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            changes.add(elements.get(i).getText());
        }
        return changes;
    }

    // Extract numeric points from "+1437.48 +2.43%"
    public double getPoints(String text) {

        try {
            if (text == null || text.trim().isEmpty()) {
//                throw new IllegalArgumentException("Input text is null or empty");
                text="+0.00 +0.00%";
            }

            String value = text.trim().split(" ")[0]
                    .replace("+", "")
                    .replace(",", "")
                    .trim();

            if (value.isEmpty()) {
                value = "0.00"; // Default to 0 if no valid number is found
//                throw new IllegalArgumentException("Extracted value is empty from text: " + text);
            }

            return Double.parseDouble(value);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for text: " + text);
            throw e; // rethrow or return default
        }
    }
}
