import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExcelReader {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("your_excel_file.xlsx")) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming you are working with the first sheet

            int[] keyRowIndices = {0, 2, 4, 7, 9}; // Indices of the key rows (0-based)
            int valueColumnIndex1 = 3; // Index of the 4th column (0-based)
            int valueColumnIndex2 = 11; // Index of the 12th column (0-based)

            Map<String, Set<String>> resultMap = new HashMap<>();

            for (int keyRowIndex : keyRowIndices) {
                Row keyRow = sheet.getRow(keyRowIndex);
                String key = keyRow.getCell(0).getStringCellValue();
                Set<String> valuesSet = new HashSet<>();

                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row currentRow = sheet.getRow(rowIndex);
                    String value1 = currentRow.getCell(valueColumnIndex1).getStringCellValue();
                    String value2 = currentRow.getCell(valueColumnIndex2).getStringCellValue();

                    valuesSet.add(value1);
                    valuesSet.add(value2);
                }

                resultMap.put(key, valuesSet);
            }

            // Displaying the result
            for (Map.Entry<String, Set<String>> entry : resultMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Values: " + entry.getValue());
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
