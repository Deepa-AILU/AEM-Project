import static org.junit.jupiter.api.Assertions.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WorkbookFactoryTest {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String XLS = "application/vnd.ms-excel";

    @Test
    public void testCreateXSSFWorkbook() throws IOException {
        String mimeType = XLSX;
        InputStream stream = new ByteArrayInputStream(new byte[0]);  // empty stream for test
        
        Workbook workbook = createWorkbook(mimeType, stream);
        
        assertNotNull(workbook);
        assertTrue(workbook instanceof XSSFWorkbook);
    }

    @Test
    public void testCreateHSSFWorkbook() throws IOException {
        String mimeType = XLS;
        InputStream stream = new ByteArrayInputStream(new byte[0]);  // empty stream for test
        
        Workbook workbook = createWorkbook(mimeType, stream);
        
        assertNotNull(workbook);
        assertTrue(workbook instanceof HSSFWorkbook);
    }

    @Test
    public void testInvalidMimeType() throws IOException {
        String mimeType = "invalid/mimetype";
        InputStream stream = new ByteArrayInputStream(new byte[0]);  // empty stream for test
        
        Workbook workbook = createWorkbook(mimeType, stream);
        
        assertNull(workbook);
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
