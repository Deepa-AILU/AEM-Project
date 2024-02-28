import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("your_excel_file.xlsx")) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming you are working with the first sheet

            Row headerRow = sheet.getRow(0);
            int keyColumnIndex1 = 0; // Index of the first key column
            int keyColumnIndex2 = 1; // Index of the second key column

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                String key1 = headerRow.getCell(keyColumnIndex1).getStringCellValue();
                String key2 = headerRow.getCell(keyColumnIndex2).getStringCellValue();
                String concatenatedValue = currentRow.getCell(keyColumnIndex1).getStringCellValue()
                        + currentRow.getCell(keyColumnIndex2).getStringCellValue();

                System.out.println("Key1: " + key1 + ", Key2: " + key2 + ", Concatenated Value: " + concatenatedValue);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
