import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ExcelToHashMap {

    public static void main(String[] args) {
        // Specify the path to your Excel file
        String excelFilePath = "path/to/your/excel/file.xlsx";

        try (FileInputStream inputStream = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Assuming the data is in the first sheet (index 0)
            Sheet sheet = workbook.getSheetAt(0);

            // Assuming the row you want to process is at index 0
            Row row = sheet.getRow(0);

            // Convert row to HashMap<String, Object>
            HashMap<String, Object> rowHashMap = convertRowToHashMap(row);

            // Display the result
            System.out.println(rowHashMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, Object> convertRowToHashMap(Row row) {
        HashMap<String, Object> hashMap = new HashMap<>();

        // Use the first cell value as the key
        String key = row.getCell(0).getStringCellValue();

        // Use a HashSet to store unique values for other cells
        HashSet<Object> valuesSet = new HashSet<>();

        // Loop through the row starting from the second cell
        for (int i = 1; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                // Add the current cell value to the set (ignoring duplicates)
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
        }

        // Store the key and values set in the HashMap
        hashMap.put(key, valuesSet);

        return hashMap;
    }
}
