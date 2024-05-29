import static org.junit.jupiter.api.Assertions.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelTest {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String XLS = "application/vnd.ms-excel";

    @Test
    public void testCreateXSSFWorkbook() throws IOException {
        String mimeType = XLSX;
        InputStream stream = new ByteArrayInputStream(new byte[0]);  // empty stream for test
        
        Workbook workbook = createWorkbook(mimeType, stream);
        
        assertNotNull(workbook, "Workbook should not be null for XLSX mime type");
        assertTrue(workbook instanceof XSSFWorkbook, "Workbook should be an instance of XSSFWorkbook");
    }

    @Test
    public void testCreateHSSFWorkbook() throws IOException {
        String mimeType = XLS;
        InputStream stream = new ByteArrayInputStream(new byte[0]);  // empty stream for test
        
        Workbook workbook = createWorkbook(mimeType, stream);
        
        assertNotNull(workbook, "Workbook should not be null for XLS mime type");
        assertTrue(workbook instanceof HSSFWorkbook, "Workbook should be an instance of HSSFWorkbook");
    }

    @Test
    public void testInvalidMimeType() throws IOException {
        String mimeType = "invalid/mimetype";
        InputStream stream = new ByteArrayInputStream(new byte[0]);  // empty stream for test
        
        Workbook workbook = createWorkbook(mimeType, stream);
        
        assertNull(workbook, "Workbook should be null for invalid mime type");
    }

    // Helper method to simulate the original code
    private Workbook createWorkbook(String mimeType, InputStream stream) throws IOException {
        Workbook workbook;
        if (mimeType.equals(XLSX)) {
            workbook = new XSSFWorkbook(stream);
        } else if (mimeType.equals(XLS)) {
            workbook = new HSSFWorkbook(stream);
        } else {
            return null;
        }
        return workbook;
    }
}
