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
            private LinkedHashMap<String, Object> resultFormattedData(Workbook workbook, int sheetIndex, int headerRowIndex) {
    Sheet sheet = workbook.getSheetAt(sheetIndex);
    Row headerRow = sheet.getRow(headerRowIndex);

    LinkedHashMap<String, Object> resultFloorData = new LinkedHashMap<>();

    Map<String, TreeSet<Object>> formattedFloorData = new HashMap<>();
    List<String> headerColumns = Arrays.asList(AMOUNT_LOW, SCORE_LOW, CLTV_LOW);
    List<String> keyHeader = Arrays.asList("amount", "score", "cltv");
    String[] tableHeader = getTableHeader(workbook, sheetIndex);

    for (int i = 0, j = 0; i < tableHeader.length; i++) {
        String columnHeader = headerRow.getCell(i).getStringCellValue();
        if (headerColumns.contains(tableHeader[i])) {
            TreeSet<Object> resultFloorSet = formatFloorByInput(workbook, headerRowIndex, i, tableHeader[i]);
            formattedFloorData.put(keyHeader.get(j), resultFloorSet);
            j++;
        }
    }

    resultFloorData.put("floorsByInput", formattedFloorData);
    resultFloorData.put("rateAndAmountByKey", rateByKey(workbook, sheetIndex, 0));
    resultFloorData.put("codeByState", codeByState(workbook, 2, 0));

    List<Double> minColumnValues = getCellValues(workbook, 0, 2);
    List<Double> maxColumnValues = getCellValues(workbook, 0, 3);

    if (!minColumnValues.isEmpty() && !maxColumnValues.isEmpty()) {
        double minValue = minColumnValues.stream().min(Double::compareTo).orElse(Double.NaN);
        double maxValue = maxColumnValues.stream().max(Double::compareTo).orElse(Double.NaN);
        resultFloorData.put("minimumLoanAmount", minValue);
        resultFloorData.put("maximumLoanAmount", maxValue);
    }

    return resultFloorData;
}
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
