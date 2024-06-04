import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ResultFormattedDataTest {

    @Mock
    private Workbook mockWorkbook;
    @Mock
    private Sheet mockSheet;

    private String[] tableHeader;
    private List<String> headerColumns;
    private List<String> keyHeader;
    private static final String AMOUNT_LOW = "AMOUNT_LOW";
    private static final String SCORE_LOW = "SCORE_LOW";
    private static final String CLTV_LOW = "CLTV_LOW";
    private static final String AMOUNT = "AMOUNT";
    private static final String SCORE = "SCORE";
    private static final String CLTV = "CLTV";
    private static final String FICO_BAND = "FICO_BAND";
    private static final String FLOORS_BY_INPUT = "FLOORS_BY_INPUT";
    private static final String RATE_BY_KEY = "RATE_BY_KEY";
    private static final String STATE_CODE = "STATE_CODE";
    private static final String MIN_LOAN_AMOUNT = "MIN_LOAN_AMOUNT";
    private static final String MAX_LOAN_AMOUNT = "MAX_LOAN_AMOUNT";
    private static final String DECIMAL_VALUE = ",";

    @BeforeEach
    public void setup() {
        mockSheet = Mockito.mock(Sheet.class);
        Mockito.when(mockWorkbook.getSheetAt(0)).thenReturn(mockSheet);

        tableHeader = new String[]{"HEADER1", "AMOUNT_LOW", "HEADER2", "SCORE_LOW", "HEADER3"};
        headerColumns = List.of(AMOUNT_LOW, SCORE_LOW, CLTV_LOW);
        keyHeader = List.of(AMOUNT, SCORE, CLTV);
    }

    @Test
    public void testResultFormattedData_withAllData() throws Exception {
        // Mock data for formatFloorByInput
        TreeSet<Object> mockFloorSet1 = Mockito.mock(TreeSet.class);
        TreeSet<Object> mockFloorSet2 = Mockito.mock(TreeSet.class);
        Mockito.when(formatFloorByInput(mockWorkbook, 1, 1)).thenReturn(mockFloorSet1);
        Mockito.when(formatFloorByInput(mockWorkbook, 1, 3)).thenReturn(mockFloorSet2);

        // Mock data for other methods (replace with your mocking logic)
        Map<String, Object> mockOtherData = new HashMap<>();
        Mockito.when(getFicoBandValue(mockWorkbook, 0, 4)).thenReturn("Fico Band Value");
        Mockito.when(rateByKey(mockWorkbook, 0)).thenReturn(mockOtherData);
        Mockito.when(codeByState(mockWorkbook, 2, 0)).thenReturn("State Code");

        // Mock cell values for min/max calculation
        List<Double> mockMinValues = List.of(10.0, 15.0);
        List<Double> mockMaxValues = List.of(20.0, 25.0);
        Mockito.when(getCellValues(mockWorkbook, 0, 2)).thenReturn(mockMinValues);
        Mockito.when(getCellValues(mockWorkbook, 0, 3)).thenReturn(mockMaxValues);

        // Call the method
        LinkedHashMap<String, Object> result = resultFormattedData(mockWorkbook, 0, 1);

        // Assertions
        assertEquals(6, result.size());
        assertEquals(mockFloorSet1, result.get(AMOUNT));
        assertEquals(mockFloorSet2, result.get(SCORE));
        assertEquals("Fico Band Value", result.get(FICO_BAND));
        assertEquals(mockOtherData, result.get(RATE_BY_KEY));
        assertEquals("State Code", result.get(STATE_CODE));
    }

        @Test
public void testGetJsonData_withValidXlsx() throws Exception {
    // Mock workbook and resource
    Workbook mockWorkbook = Mockito.mock(Workbook.class);
    Resource mockResource = Mockito.mock(Resource.class);
    Mockito.when(mockResource.adaptTo(InputStream.class)).thenReturn(getInputStream("valid_xlsx.xlsx"));

    // Set up resolver and asset
    ResourceResolver mockResolver = Mockito.mock(ResourceResolver.class);
    AssetManager mockAssetManager = Mockito.mock(AssetManager.class);
    Mockito.when(mockResolver.adaptTo(AssetManager.class)).thenReturn(mockAssetManager);

    // Call the method
    String jsonData = getJsonData(mockResource, "application/vnd.ms-excel");

    // Assertions
    assertNotNull(jsonData);
    // You can further validate the content of jsonData using JSON parsing libraries
}

@Test
public void testGetJsonData_withInvalidMimeType() throws Exception {
    // Mock resource
    Resource mockResource = Mockito.mock(Resource.class);
    Mockito.when(mockResource.adaptTo(InputStream.class)).thenReturn(getInputStream("valid_xlsx.xlsx"));

    // Call the method with invalid mimeType
    String jsonData = getJsonData(mockResource, "invalid/mimetype");

    // Assertions
    assertNull(jsonData);
}

// Helper method to get InputStream from a test resource file
private InputStream getInputStream(String fileName) throws IOException {
    return getClass().getClassLoader().getResourceAsStream(fileName);
}

  @Test
public void testGetJsonData_withValidXlsx() throws Exception {
    // Mock workbook and resource
    Workbook mockWorkbook = Mockito.mock(Workbook.class);
    Resource mockResource = Mockito.mock(Resource.class);
    Mockito.when(mockResource.adaptTo(InputStream.class)).thenReturn(getInputStream("valid_xlsx.xlsx"));

    // Set up resolver and asset
    ResourceResolver mockResolver = Mockito.mock(ResourceResolver.class);
    AssetManager mockAssetManager = Mockito.mock(AssetManager.class);
    Mockito.when(mockResolver.adaptTo(AssetManager.class)).thenReturn(mockAssetManager);

    // Call the method
    String jsonData = getJsonData(mockResource, "application/vnd.ms-excel");

    // Assertions
    assertNotNull(jsonData);
    // You can further validate the content of jsonData using JSON parsing libraries
}

@Test
public void testGetJsonData_withInvalidMimeType() throws Exception {
    // Mock resource
    Resource mockResource = Mockito.mock(Resource.class);
    Mockito.when(mockResource.adaptTo(InputStream.class)).thenReturn(getInputStream("valid_xlsx.xlsx"));

    // Call the method with invalid mimeType
    String jsonData = getJsonData(mockResource, "invalid/mimetype");

    // Assertions
    assertNull(jsonData);
}

// Helper method to get InputStream from a test resource file
private InputStream getInputStream(String fileName) throws IOException {
    return getClass().getClassLoader().getResourceAsStream(fileName);
}
  @Test
public void testFormatFloorByInput_ValidSheetAndColumn() {
    // Mock or create a Workbook with a sheet and numeric data
    Workbook workbook = createMockWorkbook(1, new String[]{"Data"});
    int sheetIndex = 0;
    int columnIndex = 0;
    addNumericDataRow(workbook, sheetIndex, columnIndex, 123.45);
    addNumericDataRow(workbook, sheetIndex, columnIndex, 567.89);
    addNumericDataRow(workbook, sheetIndex, columnIndex, 901.23);

    TreeSet<Object> expectedResult = new TreeSet<>(Arrays.asList(901.23, 567.89, 123.45));
    TreeSet<Object> actualResult = formatFloorByInput(workbook, sheetIndex, columnIndex);

    assertEquals(expectedResult, actualResult);
}

@Test
public void testFormatFloorByInput_EmptySheet() {
    Workbook workbook = createMockWorkbook(1, new String[]{"Data"});
    int sheetIndex = 0;
    int columnIndex = 0;

    TreeSet<Object> actualResult = formatFloorByInput(workbook, sheetIndex, columnIndex);

    assertTrue(actualResult.isEmpty());
}

@Test
public void testFormatFloorByInput_NonNumericData() {
    Workbook workbook = createMockWorkbook(1, new String[]{"Data"});
    int sheetIndex = 0;
    int columnIndex = 0;
    addDataRow(workbook, sheetIndex, columnIndex, "Non-numeric");

    expectedException(NumberFormatException.class, () -> formatFloorByInput(workbook, sheetIndex, columnIndex));
}
@Test
public void testGetFicoBandValue_ValidSheetAndColumn() {
    // Mock or create a Workbook with a sheet and band data
    Workbook workbook = createMockWorkbook(1, new String[]{"Fico Band"});
    int sheetIndex = 0;
    int columnIndex = 0;
    addDataRow(workbook, sheetIndex, columnIndex, "680-720");
    addDataRow(workbook, sheetIndex, columnIndex, "740-780");
    addDataRow(workbook, sheetIndex, columnIndex, "Invalid Range");

    TreeMap<String, String> expectedResult = new TreeMap<>(Collections.reverseOrder());
    expectedResult.put("700", "740-780");
    expectedResult.put("690", "680-720");

    TreeMap<String, String> actualResult = getFicoBandValue(workbook, sheetIndex, columnIndex);

    assertEquals(expectedResult, actualResult);
}

@Test
public void testGetFicoBandValue_EmptySheet() {
    Workbook workbook = createMockWorkbook(1, new String[]{"Fico Band"});
    int sheetIndex = 0;
    int columnIndex = 0;

    TreeMap<String, String> actualResult = getFicoBandValue(workbook, sheetIndex, columnIndex);

    assertTrue(actualResult.isEmpty());
}

@Test
public void testGetFicoBandValue_InvalidRangeFormat() {
    Workbook workbook = createMockWorkbook(1, new String[]{"Fico Band"});
    int sheetIndex = 0;
    int columnIndex = 0;
    addDataRow(workbook, sheetIndex, columnIndex, "Invalid Range");

    TreeMap<String, String> actualResult = getFicoBandValue(workbook, sheetIndex, columnIndex);

    assertTrue(actualResult.isEmpty()); // No valid ranges processed
}

@Test
public void testCodeByState_ValidSheetAndHeader() {
    // Mock or create a Workbook with a sheet and header row
    Workbook workbook = createMockWorkbook(1, new String[]{"State", "Code"});
    int sheetNum = 0;
    int headerValue = 1;
    HashMap<String, Object> expectedData = new HashMap<>();
    expectedData.put("State A", 123.45);
    expectedData.put("State B", 567.89);

    // Add rows with data to the sheet (modify as needed for your data)
    addRowsToSheet(workbook, sheetNum, expectedData);

    HashMap<String, Object> actualData = codeByState(workbook, sheetNum, headerValue);

    assertEquals(expectedData, actualData);
}

@Test
public void testCodeByState_EmptySheet() {
    Workbook workbook = createMockWorkbook(1, new String[]{"State", "Code"});
    int sheetNum = 0;
    int headerValue = 1;

    HashMap<String, Object> actualData = codeByState(workbook, sheetNum, headerValue);

    assertTrue(actualData.isEmpty());
}

@Test
public void testCodeByState_MissingHeaderCell() {
    Workbook workbook = createMockWorkbook(1, new String[]{"Code"}); // Missing "State" header
    int sheetNum = 0;
    int headerValue = 0; // Assuming "Code" is at index 0

    // Add a row with data (modify as needed)
    addDataRow(workbook, sheetNum, 0, "State A");
    addDataRow(workbook, sheetNum, 1, 123.45);

    HashMap<String, Object> actualData = codeByState(workbook, sheetNum, headerValue);

    assertTrue(actualData.isEmpty()); // No data extracted due to missing header
}
@Test
public void testGetTableHeader_ValidSheet() {
    Workbook workbook = createMockWorkbook(1, new String[]{"State", "Code", "Value"});
    int sheetNum = 0;

    String[] expectedHeader = new String[]{"State", "Code", "Value"};
    String[] actualHeader = getTableHeader(workbook, sheetNum);

    assertArrayEquals(expectedHeader, actualHeader);
}

@Test
public void testGetTableHeader_EmptySheet() {
    Workbook workbook = createMockWorkbook(1, new String[]{}); // Empty header row
    int sheetNum = 0;

    String[] actualHeader = getTableHeader(workbook, sheetNum);

    assertEquals(0, actualHeader.length); // Empty header returned
}
@Test
public void testGetCellValues_ValidSheetAndColumn() {
    Workbook workbook = createMockWorkbook(1, new String[]{"State", "Value"});
    int sheetNum = 0;
    int column = 1; // Assuming "Value" is in column 1

    // Add rows with numeric data (modify as needed)
    addNumericDataRow(workbook, sheetNum, 1, 123.45);
    addNumericDataRow(workbook, sheetNum, 1, 567.89);

    List<Double> expectedValues = Arrays.asList(123.45, 567.89);
    List<Double> actualValues = getCellValues(workbook, sheetNum, column);

    assertEquals(expectedValues, actualValues);
}

@Test
public void testGetCellValues_EmptySheet() {
    Workbook workbook = createMockWorkbook(1, new String[]{"State", "Value"});
    int sheetNum = 0;
    int column = 1;

    List<Double> actualValues = getCellValues(workbook, sheetNum, column);

    assertTrue(actualValues.isEmpty());
}

@Test
public void testGetCellValues_NonNumericColumn() {
    Workbook workbook = createMockWorkbook(1, new String[]{"State
@Test
public void testCodeByState_WithMockCells() {
    // Mock Workbook and Sheet
    Workbook mockWorkbook = Mockito.mock(Workbook.class);
    Sheet mockSheet = Mockito.mock(Sheet.class);
    Mockito.when(mockWorkbook.getSheetAt(0)).thenReturn(mockSheet);

    // Mock Cells with different types
    Cell mockStringCell = Mockito.mock(Cell.class);
    Mockito.when(mockStringCell.getCellType()).thenReturn(CellType.STRING);
    Mockito.when(mockStringCell.getStringCellValue()).thenReturn("Hello");

    Cell mockNumericCell = Mockito.mock(Cell.class);
    Mockito.when(mockNumericCell.getCellType()).thenReturn(CellType.NUMERIC);
    Mockito.when(mockNumericCell.getNumericCellValue()).thenReturn(123.45);

    Cell mockBooleanCell = Mockito.mock(Cell.class);
    Mockito.when(mockBooleanCell.getCellType()).thenReturn(CellType.BOOLEAN);
    Mockito.when(mockBooleanCell.getBooleanCellValue()).thenReturn(true);

    // Mock Row with Cells
    Row mockRow = Mockito.mock(Row.class);
    Mockito.when(mockSheet.getRow(1)).thenReturn(mockRow);
    Mockito.when(mockRow.getCell(0)).thenReturn(mockStringCell);
    Mockito.when(mockRow.getCell(1)).thenReturn(mockNumericCell);
    Mockito.when(mockRow.getCell(2)).thenReturn(mockBooleanCell);

    // Test codeByState with mock cells
    int sheetNum = 0;
    int headerValue = 0;
    HashMap<String, Object> actualData = codeByState(mockWorkbook, sheetNum, headerValue);

    // Assert expected data based on mock cell behavior
    assertEquals("Hello", actualData.get("Header from mockStringCell"));
    assertEquals(123.45, actualData.get("Header from mockNumericCell"));
    assertEquals(true, actualData.get("Header from mockBooleanCell"));
}

