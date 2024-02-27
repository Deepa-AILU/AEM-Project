import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ExcelColumnToHashMap {

    public static void main(String[] args) {
        // Specify the path to your Excel file
        String excelFilePath = "path/to/your/excel/file.xlsx";

        try (FileInputStream inputStream = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Assuming the data is in the first sheet (index 0)
            Sheet sheet = workbook.getSheetAt(0);

            // Assuming the column you want to process is at index 0
            int columnIndex = 0;

            // Convert column to HashMap<String, Object>
            HashMap<String, Object> columnHashMap = convertColumnToHashMap(sheet, columnIndex);

            // Display the result
            System.out.println(columnHashMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, Object> convertColumnToHashMap(Sheet sheet, int columnIndex) {
        HashMap<String, Object> hashMap = new HashMap<>();

        // Loop through the column starting from the second row
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                // Use the first cell value as the key
                String key = row.getCell(0).getStringCellValue();

                // Use a HashSet to store unique values for other cells
                HashSet<Object> valuesSet = new HashSet<>();

                // Get the current cell value and add it to the set (ignoring duplicates)
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            valuesSet.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            valuesSet.add(cell.getNumericCellValue());
                            break;
                        // Add more cases as needed for other cell types
                    }
                }

                // Store the key and values set in the HashMap
                hashMap.put(key, valuesSet);
            }
        }

        return hashMap;
    }
}
