import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelMinMax {

    public static void main(String[] args) {
        String filePath = "path/to/your/excel/file.xlsx";
        int columnIndexToCheck = 0; // Specify the column index (0-based) you want to find min and max for

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming you are working with the first sheet

            List<Double> columnValues = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(columnIndexToCheck, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    if (cell.getCellType() == CellType.NUMERIC) {
                        columnValues.add(cell.getNumericCellValue());
                    }
                }
            }

            if (!columnValues.isEmpty()) {
                double minValue = columnValues.stream().min(Double::compareTo).orElse(Double.NaN);
                double maxValue = columnValues.stream().max(Double::compareTo).orElse(Double.NaN);

                System.out.println("Min Value: " + minValue);
                System.out.println("Max Value: " + maxValue);
            } else {
                System.out.println("No numeric values found in the specified column.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
