import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;

public class ExcelValidator {

    public static ValidationResult validateExcel(InputStream inputStream) {
        ValidationResult validationResult = new ValidationResult();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            // Check total number of sheets
            if (workbook.getNumberOfSheets() != 3) {
                validationResult.addError("Total number of sheets should be 3 (Pricing Grid, Term, State).");
            }

            // Validate each sheet
            validatePricingGridSheet(workbook.getSheet("Pricing Grid"), validationResult);
            validateTermGroupingSheet(workbook.getSheet("Term"), validationResult);
            validateStateGroupingSheet(workbook.getSheet("State"), validationResult);

        } catch (Exception e) {
            validationResult.addError("Error occurred while reading the Excel file: " + e.getMessage());
        }

        return validationResult;
    }

    private static void validatePricingGridSheet(Sheet sheet, ValidationResult validationResult) {
        // Validate number of columns
        if (sheet.getRow(0).getLastCellNum() != 14) {
            validationResult.addError("Pricing Grid sheet should have 14 columns.");
        }
        // Validate column names
        Row headerRow = sheet.getRow(0);
        String[] expectedColumnNames = {"Lien Position", "LoanAmountBand", "LoanAmount-Lou", "Loan Amount High FICOBand", "FICO LOVEFICO-High",
                                        "CLTVBand", "CLTVWLOWCLTV-High", "State Grouping Term Price", "Qualify?"};
        for (int i = 0; i < expectedColumnNames.length; i++) {
            if (!headerRow.getCell(i).getStringCellValue().equals(expectedColumnNames[i])) {
                validationResult.addError("Column name mismatch in Pricing Grid sheet.");
                break;
            }
        }
        // Additional validations...
    }

    private static void validateTermGroupingSheet(Sheet sheet, ValidationResult validationResult) {
        // Validate number of columns
        if (sheet.getRow(0).getLastCellNum() != 2) {
            validationResult.addError("Term Grouping sheet should have 2 columns.");
        }
        // Validate column names
        // Additional validations...
    }

    private static void validateStateGroupingSheet(Sheet sheet, ValidationResult validationResult) {
        // Validate number of columns
        if (sheet.getRow(0).getLastCellNum() != 2) {
            validationResult.addError("State Grouping sheet should have 2 columns.");
        }
        // Validate column names
        // Additional validations...
    }

    // Other validation methods for each specific rule...

}
