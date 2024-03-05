import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelReader {

    public static void main(String[] args) {
        try {
            // Provide the path to your Excel file
            String filePath = "path/to/your/excel/file.xlsx";

            // Read the Excel file
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Initialize the HashMap
            Map<String, Map<String, String>> hashMap = new HashMap<>();

            // Iterate through each row
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Skip the header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Generate the key by concatenating specific header values
                String key = row.getCell(0) + "_" + row.getCell(2) + "_" + row.getCell(5) + "_" +
                        row.getCell(8) + "_" + row.getCell(10);

                // Initialize the inner HashMap for the current row
                Map<String, String> innerMap = new HashMap<>();

                // Add values to the inner HashMap based on specified headers
                innerMap.put("LoanAmount-High", getCellValue(row.getCell(3)));
                innerMap.put("Price", getCellValue(row.getCell(12)));

                // Add the inner HashMap to the main HashMap
                hashMap.put(key, innerMap);
            }

            // Close the workbook and file stream
            workbook.close();
            fileInputStream.close();

            // Print or use the resulting HashMap as needed
            System.out.println(hashMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return ""; // or handle it differently based on your requirements
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
