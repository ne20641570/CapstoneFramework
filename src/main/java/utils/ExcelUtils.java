package utils;

import config.FrameworkConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static void writeToColumn(String sheetName, String columnName, String value) {

        FileInputStream fis = null;
        FileOutputStream fos = null;
        Workbook workbook = null;

        try {

            File file = new File(FrameworkConstants.EXCEL_PATH);

            // Create file if not exists
            if (!file.exists()) {
                workbook = new XSSFWorkbook();
                workbook.createSheet(sheetName);
            } else {
                fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
            }

            Sheet sheet = workbook.getSheet(sheetName);

            // Create sheet if not exists
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            // Header row
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                headerRow = sheet.createRow(0);
            }

            int colIndex = -1;

            // Find column index
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {

                Cell cell = headerRow.getCell(i);

                if (cell != null &&
                        cell.getStringCellValue().equalsIgnoreCase(columnName)) {

                    colIndex = i;
                    break;
                }
            }

            // ✅ CREATE COLUMN IF NOT EXISTS
            if (colIndex == -1) {
                colIndex = headerRow.getLastCellNum() == -1 ? 0 : headerRow.getLastCellNum();
                headerRow.createCell(colIndex).setCellValue(columnName);
            }

            // Find next empty row
            int lastRow = sheet.getLastRowNum();
            int targetRow = -1;

            for (int i = 1; i <= lastRow; i++) {

                Row row = sheet.getRow(i);

                if (row == null ||
                        row.getCell(colIndex) == null ||
                        row.getCell(colIndex).toString().isEmpty()) {

                    targetRow = i;
                    break;
                }
            }

            // If no empty row → append new
            if (targetRow == -1) {
                targetRow = lastRow + 1;
            }

            Row row = sheet.getRow(targetRow);

            if (row == null) {
                row = sheet.createRow(targetRow);
            }

            Cell cell = row.createCell(colIndex);
            cell.setCellValue(value);

            // Write back to file
            fos = new FileOutputStream(file);
            workbook.write(fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (workbook != null) workbook.close();
                if (fis != null) fis.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // update and grouping with underlying

    public static void writeGroupedData(String sheetName,
                                        String typeColumn,
                                        String underlyingColumn,
                                        Map<String, List<String>> data) {

        FileInputStream fis = null;
        FileOutputStream fos = null;
        Workbook workbook = null;

        try {

            File file = new File(FrameworkConstants.EXCEL_PATH);

            // Create or load workbook
            if (!file.exists()) {
                workbook = new XSSFWorkbook();
            } else {
                fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
            }

            // Get or create sheet
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            // Header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                headerRow = sheet.createRow(0);
            }

            // Get/Create column indexes
            int typeColIndex = getOrCreateColumn(headerRow, typeColumn);
            int underlyingColIndex = getOrCreateColumn(headerRow, underlyingColumn);

            int rowNum = sheet.getLastRowNum() + 1;

            // Write grouped data
            for (String type : data.keySet()) {

                List<String> underlyings = data.get(type);

                for (int i = 0; i < underlyings.size(); i++) {

                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        row = sheet.createRow(rowNum);
                    }

                    // Write Type only once per group
                    if (i == 0) {
                        row.createCell(typeColIndex).setCellValue(type);
                    }

                    row.createCell(underlyingColIndex)
                            .setCellValue(underlyings.get(i));

                    rowNum++;
                }
            }

            // Auto-size columns
            sheet.autoSizeColumn(typeColIndex);
            sheet.autoSizeColumn(underlyingColIndex);

            fos = new FileOutputStream(file);
            workbook.write(fos);

            System.out.println("Grouped data written successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (workbook != null) workbook.close();
                if (fis != null) fis.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 🔥 Reusable method (core logic)
    private static int getOrCreateColumn(Row headerRow, String columnName) {

        int colIndex = -1;

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {

            Cell cell = headerRow.getCell(i);

            if (cell != null &&
                    cell.getStringCellValue().equalsIgnoreCase(columnName)) {

                colIndex = i;
                break;
            }
        }

        // Create column if not exists
        if (colIndex == -1) {
            colIndex = headerRow.getLastCellNum() == -1 ? 0 : headerRow.getLastCellNum();
            headerRow.createCell(colIndex).setCellValue(columnName);
        }

        return colIndex;
    }
    // OPTIONAL: Existing read method
    public static String getData(String sheet, int row, int col) {
        try {
            FileInputStream fis = new FileInputStream(FrameworkConstants.EXCEL_PATH);
            Workbook wb = new XSSFWorkbook(fis);
            return wb.getSheet(sheet).getRow(row).getCell(col).toString();
        } catch (Exception e) {
            return "";
        }
    }


    public static void writeGroupedDataToExcelLatest(String sheetName,
                                               List<String> headers,
                                               Map<String, List<List<String>>> groupedData)  {

        Workbook workbook;
        Sheet sheet;

        File file = new File(FrameworkConstants.EXCEL_PATH);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
            }catch (IOException e) {
                throw new RuntimeException("Failed to read existing Excel file: " + e.getMessage());
            }
            // If sheet exists, remove it for fresh write
            sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                int idx = workbook.getSheetIndex(sheet);
                workbook.removeSheetAt(idx);
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        sheet = workbook.createSheet(sheetName);

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        int rowNum = 1;

        for (String groupKey : groupedData.keySet()) {

            // Optional: Insert group name as a row
            Row groupRow = sheet.createRow(rowNum++);
            Cell groupCell = groupRow.createCell(0);
            groupCell.setCellValue("Group: " + groupKey);
            CellStyle groupStyle = workbook.createCellStyle();
            Font groupFont = workbook.createFont();
            groupFont.setBold(true);
            groupFont.setColor(IndexedColors.DARK_BLUE.getIndex());
            groupStyle.setFont(groupFont);
            groupCell.setCellStyle(groupStyle);

            // Add data rows for this group
            List<List<String>> rows = groupedData.get(groupKey);
            for (List<String> dataRow : rows) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < dataRow.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(dataRow.get(i));
                    sheet.autoSizeColumn(i);
                }
            }

            // Empty line for spacing
            rowNum++;
        }

        // Write workbook to file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Excel file written: " + file);
    }



    public static void writeGroupedDataToExcel(String sheetName,
                                               List<String> headers,
                                               Map<String, List<List<String>>> groupedData) throws IOException {

        Workbook workbook;
        File file = new File(FrameworkConstants.EXCEL_PATH);

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
            } catch (IOException e) {
                System.err.println("Failed to read existing Excel file, creating new one.");
                workbook = new XSSFWorkbook();
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        // Remove existing sheet if any
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            int idx = workbook.getSheetIndex(sheet);
            workbook.removeSheetAt(idx);
        }

        sheet = workbook.createSheet(sheetName);

        // Create header row with styling
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;

        CellStyle groupStyle = createGroupStyle(workbook);

        for (Map.Entry<String, List<List<String>>> entry : groupedData.entrySet()) {
            String groupKey = entry.getKey();
            List<List<String>> rows = entry.getValue();

            // Write group header merged across all columns
            Row groupRow = sheet.createRow(rowNum++);
            Cell groupCell = groupRow.createCell(0);
            groupCell.setCellValue("Group: " + groupKey);
            groupCell.setCellStyle(groupStyle);

            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                    groupRow.getRowNum(), groupRow.getRowNum(), 0, headers.size() - 1
            ));

            // Write the headers again for each group (optional)
            Row headerRowGroup = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRowGroup.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Write grouped rows
            for (List<String> dataRow : rows) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < dataRow.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(dataRow.get(i));
                }
            }
            rowNum++; // empty row after each group
        }

        // Auto size columns
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            System.out.println("Excel file written successfully: " + file.getAbsolutePath());
        } finally {
            workbook.close();
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createGroupStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }



    public static void writeHighestTurnoverDetails(String sheetName, List<String> headers, List<String> highestRowData) throws IOException {
        File file = new File(FrameworkConstants.EXCEL_PATH);
        Workbook workbook;
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        // Remove sheet if exists for fresh write
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            workbook.removeSheetAt(workbook.getSheetIndex(sheet));
        }
        sheet = workbook.createSheet(sheetName);

        // Styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle labelStyle = createGroupStyle(workbook);
        labelStyle.setAlignment(HorizontalAlignment.CENTER);

        // Label row
        Row labelRow = sheet.createRow(0);
        Cell labelCell = labelRow.createCell(0);
        labelCell.setCellValue("Highest Notional Turnover Details");
        labelCell.setCellStyle(labelStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, headers.size() - 1));

        // Header row (1)
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }

        // Data row (2)
        Row dataRow = sheet.createRow(2);
        for (int i = 0; i < highestRowData.size(); i++) {
            Cell cell = dataRow.createCell(i);
            cell.setCellValue(highestRowData.get(i));
        }

        // Auto size columns
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();

        System.out.println("Highest turnover details written to sheet: " + sheetName);
    }

    public static void writeHighestTurnoverPerGroup(String sheetName,
                                                    List<String> headers,
                                                    List<List<String>> highestRows) throws IOException {

        File file = new File(FrameworkConstants.EXCEL_PATH);
        Workbook workbook;

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        // Remove old sheet
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            workbook.removeSheetAt(workbook.getSheetIndex(sheet));
        }

        sheet = workbook.createSheet(sheetName);

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle labelStyle = createGroupStyle(workbook);
        labelStyle.setAlignment(HorizontalAlignment.CENTER);

        // Title
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Highest Turnover Per Group");
        titleCell.setCellStyle(labelStyle);

        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                0, 0, 0, headers.size() - 1
        ));

        // Header row
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }

        // Data rows
        int rowNum = 2;
        for (List<String> rowData : highestRows) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.size(); i++) {
                row.createCell(i).setCellValue(rowData.get(i));
            }
        }

        // Auto-size
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }

        workbook.close();
        System.out.println("Highest turnover per group written successfully!");
    }


    public static List<List<String>> getHighestTurnoverPerGroup(
            Map<String, List<List<String>>> groupedData,
            int turnoverIndex) {

        List<List<String>> highestRows = new ArrayList<>();

        for (Map.Entry<String, List<List<String>>> entry : groupedData.entrySet()) {
            List<List<String>> rows = entry.getValue();

            List<String> maxRow = null;
            double maxValue = Double.MIN_VALUE;

            for (List<String> row : rows) {
                try {
                    String raw = row.get(turnoverIndex).replaceAll(",", "").trim();
                    if (raw.isEmpty()) continue;

                    double value = Double.parseDouble(raw);

                    if (value > maxValue) {
                        maxValue = value;
                        maxRow = row;
                    }
                } catch (Exception e) {
                    // skip invalid values
                }
            }

            if (maxRow != null) {
                highestRows.add(maxRow);
            }
        }

        return highestRows;
    }

    public static void writeMarket(String sheetName, int rowNum, int colNum, String value) {

        try {
            File file = new File(FrameworkConstants.EXCEL_PATH);
            Workbook wb;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                wb = new XSSFWorkbook(fis);
            } else {
                wb = new XSSFWorkbook();
            }

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                sheet = wb.createSheet(sheetName);
            }

            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
            }

            Cell cell = row.createCell(colNum);
            cell.setCellValue(value);

            // ✅ Apply style for header row
            if (rowNum == 0) {

                CellStyle headerStyle = wb.createCellStyle();

                // Background color (Light Blue like your first image)
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                // Borders (optional but looks clean)
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);

                // Font styling
                Font font = wb.createFont();
                font.setBold(true);
                font.setColor(IndexedColors.BLACK.getIndex());
                headerStyle.setFont(font);

                // Alignment (optional)
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                cell.setCellStyle(headerStyle);
            }

            // Optional: auto-size column
            sheet.autoSizeColumn(colNum);

            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
            wb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteExcelFile() {
        File file = new File(FrameworkConstants.EXCEL_PATH);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Excel file deleted successfully: " + FrameworkConstants.EXCEL_PATH);
            } else {
                System.out.println("Failed to delete Excel file: " + FrameworkConstants.EXCEL_PATH);
            }
        } else {
            System.out.println("Excel file does not exist: " + FrameworkConstants.EXCEL_PATH);
        }
    }
}