//package oder_service.oder_service;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.*;
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//@SpringBootTest
//public class OrderStatusUpdater {
//    @Test
//    void Test() throws IOException {
//
//        Map<String, Boolean> inventoryData = readInventoryData("D:\\UTC_Y4\\Inventory.xlsx");
//
//        updateOrderStatus("D:\\UTC_Y4\\Order.xlsx", inventoryData);
//    }
//
//    public static Map<String, Boolean> readInventoryData(String fileName) throws IOException {
//        Map<String, Boolean> inventoryData = new HashMap<>();
//
//        try (FileInputStream file = new FileInputStream(fileName);
//             Workbook workbook = new XSSFWorkbook(file)) {
//            Sheet sheet = workbook.getSheetAt(0);
//
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                String skuCode = row.getCell(0).getStringCellValue();
//                boolean inStock = row.getCell(1).getBooleanCellValue();
//                inventoryData.put(skuCode, inStock);
//            }
//        }
//        return inventoryData;
//    }
//
//    public static void updateOrderStatus(String fileName, Map<String, Boolean> inventoryData) throws IOException {
//        try (FileInputStream file = new FileInputStream(fileName);
//             Workbook workbook = new XSSFWorkbook(file)) {
//            Sheet sheet = workbook.getSheetAt(0);
//
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                String skuCode = row.getCell(0).getStringCellValue();
//                BigDecimal price = BigDecimal.valueOf(row.getCell(1).getNumericCellValue());
//                int quantity = (int) row.getCell(2).getNumericCellValue();
//
//                boolean isInStock = inventoryData.getOrDefault(skuCode, false);
//                String status = isInStock ? "Order Placed" : "Out of Stock";
//
//                Cell statusCell = row.createCell(3, CellType.STRING);
//                statusCell.setCellValue(status);
//
//                System.out.printf("SKU: %s, Price: %s, Quantity: %d, Status: %s%n", skuCode, price, quantity, status);
//            }
//
//            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
//                workbook.write(outputStream);
//            }
//        }
//    }
//}
