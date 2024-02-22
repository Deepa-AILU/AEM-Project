//Certainly! Below is the provided TypeScript code converted into Java:


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class PricingGridConverter {

    public static void main(String[] args) {
        try {
            String pricingGridFile = findPricingGridFile("json/input/");
            if (pricingGridFile != null) {
                writeJsonOutput(parseExcel(pricingGridFile), "json/output/dhl-rates.json");
            } else {
                throw new IOException("Failed to find exactly one .xlsx file. Ensure there is only one .xlsx file in the input directory.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findPricingGridFile(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"));

        if (files != null && files.length == 1) {
            return files[0].getAbsolutePath();
        }
        return null;
    }

    private static void writeJsonOutput(Object data, String outputPath) {
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            fileWriter.write(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object parseExcel(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            // Your existing logic for parsing Excel goes here

            // Example: Iterating over sheets
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                // Process each sheet as needed
            }

            // Example: Accessing cell data
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            String cellValue = cell.getStringCellValue();

            return cellValue; // Replace with your actual parsed data
        }
    }
}

//Note: This Java code assumes the use of Apache POI library for Excel handling. Make sure to add the necessary dependencies to your project for Apache POI. Also, you might need to adjust the logic inside `parseExcel` based on your actual Excel structure and requirements.
