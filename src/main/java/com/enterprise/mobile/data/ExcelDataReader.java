package com.enterprise.mobile.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.enterprise.mobile.config.FrameworkConstants;
import com.enterprise.mobile.exceptions.TestDataException;

/**
 * Excel test data reader using Apache POI.
 * Reads .xlsx files and returns data as List<Map<String, String>>.
 */
public final class ExcelDataReader {

    private static final Logger logger = LogManager.getLogger(ExcelDataReader.class);

    private ExcelDataReader() {
    }

    /**
     * Reads all rows from the specified sheet.
     * First row is treated as header.
     */
    public static List<Map<String, String>> readData(String fileName, String sheetName) {
        String filePath = FrameworkConstants.TEST_DATA_PATH + fileName;
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new TestDataException("Sheet not found: " + sheetName + " in " + fileName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new TestDataException("Header row is empty in sheet: " + sheetName);
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    rowData.put(headers.get(j), getCellValueAsString(cell));
                }
                data.add(rowData);
            }

            logger.debug("Read {} rows from {}:{}", data.size(), fileName, sheetName);
        } catch (IOException e) {
            throw new TestDataException("Failed to read Excel file: " + filePath, e);
        }

        return data;
    }

    /**
     * Reads specific columns from a sheet, filtered by a condition.
     */
    public static List<Map<String, String>> readData(String fileName, String sheetName,
            String filterColumn, String filterValue) {
        return readData(fileName, sheetName).stream()
                .filter(row -> filterValue.equals(row.get(filterColumn)))
                .toList();
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null)
            return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
