import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelMinMax {

    public static void main(String[] args) {
        String filePath = "path/to/your/excel/file.xlsx";
        int columnIndexToCheck = 0; // Specify the column index (0-based) you want to find min and max for

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming you are working with the first sheet

            double minValue = Double.MAX_VALUE;
            double maxValue = Double.MIN_VALUE;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(columnIndexToCheck, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    if (cell.getCellType() == CellType.NUMERIC) {
                        double cellValue = cell.getNumericCellValue();
                        minValue = Math.min(minValue, cellValue);
                        maxValue = Math.max(maxValue, cellValue);
                    }
                }
            }

            System.out.println("Min Value: " + minValue);
            System.out.println("Max Value: " + maxValue);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
