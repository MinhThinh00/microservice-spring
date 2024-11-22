package com.programming.product_service;
import com.programming.product_service.dto.ProductRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class ExcelReader {

    public static List<ProductRequest> readProductRequests(String filePath) throws IOException {
        List<ProductRequest> productRequests = new ArrayList<>();
        FileInputStream file = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        // Bỏ qua dòng tiêu đề (header)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            String name = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
            String description = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
            BigDecimal price = BigDecimal.ZERO;  // Giá trị mặc định nếu ô giá không có
            if (row.getCell(2) != null) {
                try {
                    price = BigDecimal.valueOf(row.getCell(2).getNumericCellValue());
                } catch (Exception e) {
                    // Xử lý nếu giá trị ô không phải là số hợp lệ
                    price = BigDecimal.ZERO;
                }
            }

            ProductRequest productRequest = ProductRequest.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .build();
            productRequests.add(productRequest);
        }
        file.close();
        workbook.close();
        return productRequests;
    }
    public static void writeTestResult(String filePath, int rowIndex, String status, String error) throws IOException {
        FileInputStream file = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowIndex);

        Cell statusCell = row.createCell(3); // Cột Status
        statusCell.setCellValue(status);

        Cell errorCell = row.createCell(4); // Cột Error
        errorCell.setCellValue(error);

        file.close();

        FileOutputStream outFile = new FileOutputStream(new File(filePath));
        workbook.write(outFile);
        workbook.close();
        outFile.close();
    }
    public static void writeTestSummary(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        int totalCases = sheet.getLastRowNum();
        int passCount = 0;

        // Đếm số test case pass
        for (int i = 1; i <= totalCases; i++) {
            Row row = sheet.getRow(i);
            Cell statusCell = row.getCell(3);
            if (statusCell != null && "Thành công".equals(statusCell.getStringCellValue())) {
                passCount++;
            }
        }

        // Tính tỷ lệ pass rate
        double passRate = ((double) passCount / totalCases) * 100;

        // Ghi tổng số test case pass và pass rate
        int summaryRowIndex = totalCases + 2;
        Row summaryRow = sheet.createRow(summaryRowIndex);

        Cell totalPassLabel = summaryRow.createCell(0);
        totalPassLabel.setCellValue("Tổng số test case pass:");

        Cell totalPassCell = summaryRow.createCell(1);
        totalPassCell.setCellValue(passCount);

        Cell passRateLabel = summaryRow.createCell(2);
        passRateLabel.setCellValue("Tỷ lệ pass:");

        Cell passRateCell = summaryRow.createCell(3);
        passRateCell.setCellValue(passRate + "%");

        file.close();

        FileOutputStream outFile = new FileOutputStream(new File(filePath));
        workbook.write(outFile);
        workbook.close();
        outFile.close();
    }
}
