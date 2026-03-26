package pages;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import utils.*;

import java.io.IOException;
import java.util.*;

public class MarketWatchPage {
    static ExtentTest test;
    // Locator for Index Derivative menu
    private By marketWatchTab = By.xpath("//li/child::a[@id=\"mwatch\"]");
    private By navigatingArrow = By.xpath("//i[@title=\"More About Market watch\"]");
    private By tableColumnNames = By.xpath("//tbody/child::tr/child::th[@class=\"tdcolumntdm\"]");
    private static By typesOfInstruments = By.xpath("//tbody[@ng-repeat='id in indexderi.Table']/child::tr/child::td[1] [not(@bgcolor=\"#aeaeae\")]");
    private By indexTable = By.xpath("//div[@class='largetable drimktwdiv fixTableHead']");
    private By allRowsOfIndexTable = By.xpath("//tbody[@ng-repeat='id in indexderi.Table']/child::tr[1]");
    private By nationalTurnOverValues =    By.xpath("//tbody[@ng-repeat='id in indexderi.Table']/child::tr/child::td[9]");




    String groypBytypeHeader = "Type of Instrument";
    String groupByunderlyingHeader = "Underlying";
    String groupedInstrumentssheetName = "Grouped_Instruments_Underlying";
    String allInstrumentssheetName = "all_Instruments";
    String allTurnOversheetName = "National_TurnOver";

    public void navigateToMarketWatch(ExtentTest tests) {
        test = ExtentTestManager.startNode(tests, "Navigating to Market Watch Section");
        WaitUtils.explicitWait(marketWatchTab);
        UIActions.hover(marketWatchTab);
        UIActions.click(marketWatchTab);
        UIActions.scrollToElement(marketWatchTab);
        ExtentReportUtil.logInfo(test, "Clicked on Market Watch tab");
        WaitUtils.explicitWait(navigatingArrow);
        ExtentReportUtil.logScreenshot(test, "Market_Watch_Section");
        ExtentReportUtil.logPass(test, "Successfully navigated to Market Watch section");
    }


    public void captureAllInstruments(ExtentTest tests) {
        test = ExtentTestManager.startNode(tests, "Capturing all listed instruments in Market Watch");
        WaitUtils.explicitWait(tableColumnNames);
        List<WebElement> columnNames = UIActions.getElements(tableColumnNames);
        List<String> columnNamesText = new ArrayList<>();
        for (int i = 0; i < columnNames.size(); i++) {
            WebElement column = columnNames.get(i);
            columnNamesText.add(column.getText());
            if (column.getText().equals("Type of Instrument")) {
                //ExcelUtils.writeToColumn("capstone_update_01","Types_of_instrument",column.getText());
                List<WebElement> instruments = UIActions.getElements(typesOfInstruments);
                List<String> instrumentsText = new ArrayList<>();
                for (WebElement instrument : instruments) {
//                    UIActions.scrollToElement(instrument);
                    instrumentsText.add(instrument.getText());
                    ExcelUtils.writeToColumn(allInstrumentssheetName, "Types_of_instrument", instrument.getText());
                }
            }
        }
        ExtentReportUtil.logInfo(test, "Captured all the listed instruments in Market Watch");
        ExtentReportUtil.logScreenshot(test, "Market_Watch_Instruments");
    }

    public void groupingInsrumentsUnderlying(ExtentTest tests) {
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                List<String> headers = new ArrayList<>();
                List<WebElement> headerElements = UIActions.getElements(tableColumnNames);
                Map<String, Integer> columnIndexMap = new HashMap<>();
                for (int i = 0; i < headerElements.size(); i++) {
                    String headerText = headerElements.get(i).getText().trim();
                    columnIndexMap.put(headerText, i);
                    headers.add(headerText);
                }

                int typeIndex = columnIndexMap.get(groypBytypeHeader);
                int underlyingIndex = columnIndexMap.get(groupByunderlyingHeader);

                List<WebElement> rows = UIActions.getElements(allRowsOfIndexTable);
                Map<String, List<List<String>>> groupedData = new TreeMap<>();

                for (WebElement row : rows) {
                    UIActions.scrollToElement(row);
                    List<WebElement> cols = row.findElements(By.tagName("td"));
                    if (cols.isEmpty()) continue;

                    List<String> rowData = new ArrayList<>();
                    for (WebElement col : cols) {
                        rowData.add(col.getText().trim());
                    }

                    String type = cols.get(typeIndex).getText().trim();
                    String underlying = cols.get(underlyingIndex).getText().trim();

                    if (type.isEmpty() || underlying.isEmpty()) continue;

                    String key = type + "|" + underlying;
                    groupedData.computeIfAbsent(key, k -> new ArrayList<>()).add(rowData);
                }

                // Print to console (optional)
                for (String key : groupedData.keySet()) {
                    System.out.println("Group: " + key);
                    for (List<String> row : groupedData.get(key)) {
                        System.out.println(row);
                    }
                    System.out.println("----------------------------------");
                }

                // Write to Excel file
                ExcelUtils.writeGroupedDataToExcel(groupedInstrumentssheetName, headers, groupedData);

                break; // exit after success

            } catch (StaleElementReferenceException e) {
                System.out.println("Retrying due to DOM refresh... Attempt: " + attempt);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to write Excel file: " + e.getMessage());
            }
        }
    }

    public void groupingInsrumentsUnderlyingWithHighest(ExtentTest tests) {
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                List<String> headers = new ArrayList<>();
                List<WebElement> headerElements = UIActions.getElements(tableColumnNames);
                Map<String, Integer> columnIndexMap = new HashMap<>();

                for (int i = 0; i < headerElements.size(); i++) {
                    String headerText = headerElements.get(i).getText().trim();
                    columnIndexMap.put(headerText, i);
                    headers.add(headerText);
                }

                int typeIndex = columnIndexMap.get(groypBytypeHeader);
                int underlyingIndex = columnIndexMap.get(groupByunderlyingHeader);

                // ⚠️ IMPORTANT → update this EXACT header name from UI
                int turnoverIndex = -1;

                for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
                    String header = entry.getKey().toLowerCase();

                    if (header.contains("notional") && header.contains("turnover")) {
                        turnoverIndex = entry.getValue();
                        break;
                    }
                }
                if (turnoverIndex == -1) {
                    throw new RuntimeException("❌ Notional Turnover column not found!");
                }

                List<WebElement> rows = UIActions.getElements(allRowsOfIndexTable);
                Map<String, List<List<String>>> groupedData = new TreeMap<>();

                for (WebElement row : rows) {
                    UIActions.scrollToElement(row);

                    List<WebElement> cols = row.findElements(By.tagName("td"));
                    if (cols.isEmpty()) continue;

                    List<String> rowData = new ArrayList<>();
                    for (WebElement col : cols) {
                        rowData.add(col.getText().trim());
                    }

                    String type = cols.get(typeIndex).getText().trim();
                    String underlying = cols.get(underlyingIndex).getText().trim();

                    if (type.isEmpty() || underlying.isEmpty()) continue;

                    String key = type + "|" + underlying;
                    groupedData.computeIfAbsent(key, k -> new ArrayList<>()).add(rowData);
                }

                // ✅ Write grouped data (Sheet 1)
                ExcelUtils.writeGroupedDataToExcel(
                        "Grouped Data",
                        headers,
                        groupedData
                );

                // ✅ Get highest turnover rows per group
                List<List<String>> highestRows =
                        ExcelUtils.getHighestTurnoverPerGroup(groupedData, turnoverIndex);

                // ✅ Write highest turnover per group (Sheet 2)
                ExcelUtils.writeHighestTurnoverPerGroup(
                        "Highest Turnover By Group",
                        headers,
                        highestRows
                );

                break;


            } catch (StaleElementReferenceException e) {
                System.out.println("Retrying due to DOM refresh... Attempt: " + attempt);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Excel write failed: " + e.getMessage());
            }
        }
    }
    public void captureHighestTurnOver(ExtentTest tests)  {
        test = ExtentTestManager.startNode(tests, "Capturing all national turnovers and highest turnover");
        try {
        WaitUtils.explicitWait(tableColumnNames);
        List<WebElement> columnNames = UIActions.getElements(tableColumnNames);
        UIActions.scrollToElement(tableColumnNames);
        Thread.sleep(500); // small wait to ensure visibility
        List<String> headers = new ArrayList<>();
        Map<String, Integer> columnIndexMap = new HashMap<>();

        // Collect headers & their indexes
        for (int i = 0; i < columnNames.size(); i++) {
            String headerText = columnNames.get(i).getText().trim();
            headers.add(headerText);
            columnIndexMap.put(headerText, i);
        }

        String turnoverHeader = null;
        for (String header : headers) {
            if (header.contains("Notional Turnover")) {
                turnoverHeader = header;
                break;
            }
        }

        if (turnoverHeader == null) {
            test.fail("No 'Notional Turnover' column found.");
            return;
        }

        int turnoverIndex = columnIndexMap.get(turnoverHeader);
        List<WebElement> rows = UIActions.getElements(allRowsOfIndexTable);

        List<List<String>> allRowsData = new ArrayList<>();
        List<String> allTurnovers = new ArrayList<>();

        double maxTurnover = Double.MIN_VALUE;
        List<String> maxTurnoverRow = null;

        // Loop over rows, collect data and find max turnover
        for (WebElement row : rows) {
            UIActions.scrollToElement(row);
            List<WebElement> cols = row.findElements(By.tagName("td"));
            if (cols.isEmpty()) continue;

            List<String> rowData = new ArrayList<>();
            for (WebElement col : cols) {
                rowData.add(col.getText().trim());
            }

            allRowsData.add(rowData);

            if (turnoverIndex < rowData.size()) {
                String turnoverStr = rowData.get(turnoverIndex).replaceAll("[,₹ ]", "");
                try {
                    double turnoverValue = Double.parseDouble(turnoverStr);
                    allTurnovers.add(rowData.get(turnoverIndex));

                    if (turnoverValue > maxTurnover) {
                        maxTurnover = turnoverValue;
                        maxTurnoverRow = rowData;
                    }
                } catch (NumberFormatException e) {
                    // skip invalid turnover data
                }
            }
        }


            // Write highest turnover details and all turnovers to Excel
            ExcelUtils.writeHighestTurnoverDetails(allTurnOversheetName, headers, maxTurnoverRow);

            // Write all turnovers under the "Notional Turnover" column
//            ExcelUtils.writeToColumns(allTurnOversheetName, turnoverHeader, allTurnovers);

            test.pass("Captured national turnovers and highest turnover details successfully.");
        } catch (Exception e) {
            test.fail("Failed to write turnovers to Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }










}