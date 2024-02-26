//To capture Excel sheet row data and convert it into a JSON object in Java, you can use the Apache POI library for Excel manipulation and a JSON library like Jackson for JSON processing. Below is a simple example:
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelToJsonConverter {

    public static void main(String[] args) {
        try {
            File excelFile = new File("path/to/your/excel/file.xlsx");
            FileInputStream inputStream = new FileInputStream(excelFile);

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet

            List<Map<String, String>> jsonDataList = new ArrayList<>();

            // Assuming the first row contains headers
            Row headerRow = sheet.getRow(0);
            int lastCellNum = headerRow.getLastCellNum();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row != null) {
                    Map<String, String> rowData = new HashMap<>();

                    for (int j = 0; j < lastCellNum; j++) {
                        Cell cell = row.getCell(j);
                        String header = headerRow.getCell(j).getStringCellValue();
                        String cellValue = getCellValueAsString(cell);

                        rowData.put(header, cellValue);
                    }

                    jsonDataList.add(rowData);
                }
            }

            // Convert List of Map to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(jsonDataList);

            System.out.println(jsonString);
private static Boolean validateHeaderByKey(String header) {
    return Arrays.asList(LIEN_POSITION, AMOUNT_LOW, AMOUNT_HIGH, SCORE_LOW, CLTV_LOW, STATE, PRICE).contains(header);
}
            workbook.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
//according to your specific Excel file structure and requirements.
