package com.adminpanel.dataproviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class TestDataProvider {

    private static final String RES = System.getProperty("user.dir") + "/src/test/resources/";

    private TestDataProvider() {}

    /** Reads formData.json → { username, password, comments, filename, description } */
    public static Object[][] getFormDataFromJson() {
        try {
            ObjectMapper m = new ObjectMapper();
            JsonNode root  = m.readTree(new File(RES + "formData.json"));
            List<Object[]> rows = new ArrayList<>();
            for (JsonNode n : root)
                rows.add(new Object[]{
                        n.get("username").asText(),
                        n.get("password").asText(),
                        n.get("comments").asText(),
                        n.get("filename").asText(),
                        n.get("description").asText()
                });
            return rows.toArray(new Object[0][]);
        } catch (IOException e) {
            throw new RuntimeException("formData.json read failed: " + e.getMessage());
        }
    }

    /** Reads FormData sheet from testdata.xlsx */
    public static Object[][] getFormDataFromExcel() {
        return readSheet("FormData", 5);
    }

    /** Reads AlertData sheet from testdata.xlsx */
    public static Object[][] getAlertDataFromExcel() {
        return readSheet("AlertData", 3);
    }

    private static Object[][] readSheet(String sheetName, int cols) {
        List<Object[]> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(RES + "testdata.xlsx");
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null)
                throw new RuntimeException("Sheet not found: " + sheetName);
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                Object[] data = new Object[cols];
                for (int c = 0; c < cols; c++) {
                    Cell cell = row.getCell(c);
                    data[c] = cell == null ? "" : cellVal(cell);
                }
                rows.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel read failed [" + sheetName + "]: " + e.getMessage());
        }
        return rows.toArray(new Object[0][]);
    }

    private static String cellVal(Cell c) {
        switch (c.getCellType()) {
            case STRING:  return c.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) c.getNumericCellValue());
            default:      return "";
        }
    }
}
