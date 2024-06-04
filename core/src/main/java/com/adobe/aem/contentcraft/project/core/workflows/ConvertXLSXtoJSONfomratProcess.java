component service work 100PFOLESS.Liass,

property = { "process.label Convert Pricing Grid XLSX file to 350N format process" ))

public class ConvertXLSXtoJSONfomratProcess Implements WorkflowProcess [

private static final Logger LOGGER LoggerFactory.getLogger(ConvertXLSXtoJSONfomratProcess.class); @Reference

protected ResourceResolverFactory resolverFactory;

private static final String XLSX = "xlsx";

private static final String XLS = "xls";

private static final String JSON_MIME_TYPE="application/json";

private static final String JSON_FILE_EXTENSION - ".json";

private static final String AMOUNT LOW "LoanAmount-Low";

private static final String SCORE LOW "FICO-Low";

private static final String CLTV LOW "CLTV-LOW";

private static final String AMOUNT

"amount";

private static final String SCORE "score";

private static final String CLTV "cltv";

private static final String DECIMAL VALUE = "\\..*";

private static final String FICO_BAND "ficoBandValues";

private static final String FLOORS_BY_INPUT "floorsByInput";

private static final String RATE_BY_KEY- "rateAndAmountByKey";

private static final String STATE CODE "codeByState":

private static final String MIN LOAN_AMOUNT "minimumLoanAmount":

private static final String MAX LOAN_AMOUNT "maximumLoanAmount":

private static final String FIRST LIEN "1st Lien";

private static final String FIRST "Ist";

private static final String SECOND "2nd";

private static final String INELIGIBLE "Ineligible";

private static final String LOAN AMOUNT"maximumloanAmount For Thisitem":

private static final String RATE "rate";

private static final String 350N PATH"/content/dam/dfs/dfs-home-loans/vendor-scripts/pricing-gri";

Override

public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {

Map<String, Object> param= new HashMap<String, Object>();

param.put(ResourceResolverFactory. SUBSERVICE, "DhlPricingüridWorkflowService");

String excelPath workItem.getWorkflowData().getPayload().toString(); excelPath excelPath.replace("/jcr:content/renditions/original", "");

String mimeType FilenameUtils.getExtension(excelPath);

try (final ResourceResolver resourceResolver resolverFactory.getServiceResourceResolver(param)) ( resourceResolver.commit();

Resource resource resourceResolver.getResource(excelPath);

String org3sonData-"";

if (resource 1-null) {

Asset asset resource.adaptTo(Asset.class);

if (asset 1 null) {

LOGGER.info("Asset" + asset);

Resource original asset.getOriginal();

org3sonData getJsonData(original, mimetуре);

Convert 350N array to stream Keep encoding as UTF-8/ if(org)sonData != null) {

InputStream jsonStream new ByteArrayInputStream(org)sonData.getBytes(StandardCharsets.UTF_8));

Asset fileInJCR storeFileIn)CR(JSON PATH, Filenameutils.getBaseName("Dhl-Rates").concat(JSON_FILE_EXTENSION), JsonStream);

catch (LoginException | PersistenceException e) (

LOGGER.error("Error Occurred: ()", e.getMessage());

e.printStackTrace()!}
  
Methad to get Rate by key, State, FloorbyInput, Min & Max loan amount values from excel sheet */

DHL wel

private String get sonData(Resource original, String mimeType) (

//File's mime type

Workbook workbook null;

JSONObject jsonObject null;

Map<String, Object> excelData new LinkedHashMap<>();

LOGGER.info("Resource" + original);

if (StringUtils.isNotEmpty(mimeType) && mimeType.contains(XLS)) {

try (final InputStream stream original.adaptTo(InputStream.class)) {

if (Objects.nonNull(stream)) {

if (mimeType.equals(XLSX)) (

workbook new XSSFWorkbook(stream);

} else if (mimeType.equals(XLS)) {

workbook

new HSSFWorkbook(stream);

}

1

} else (

return null;

/Get excel data as list of map objects and parse into JSON array /

excelData resultFormattedData(workbook, 8, 8);

JsonObject

new JSONObject(excelData);

} catch (IOException e) {

LOGGER.error("Error Occurred: ()", e.gethessage());

e.printStackTrace();

return jsonObject 1- null ? jsonObject.toString(): null;

  }
  private LinkedHashMap<String, Object> resultFormattedData (Workbook workbook, int sheetIndex, int headerRowIndex) ( LinkedHashMap<String, Object> resultFloorData new LinkedHashMap<>(); Map<String, TreeSet<Object>> formattedFloorData = new HashMap<>(); List<String> headerColumns Arrays.asList(AMOUNT_LOW, SCORE LOW, CLTV_LOW); List<String> keyHeader Arrays.asList(AMOUNT, SCORE, CLTV); String[] tableHeader getTableHeader (workbook, sheetIndex); int keyHeaderVal =0;

for (int 18;1< tableHeader.length; i++) {

if (headerColumns.contains(tableHeader[i])) { TreeSet<Object> resultFloorSet formatFloorByInput (workbook, headerRowIndex, i); formatted FloorData.put(keyHeader.get(keyHeaderVal), resultFloorSet); keyHeaderVal keyHeaderVal+1;

}

}

resultFloorData.put(FICO_BAND, getFicoBandValue(workbook, 8, 4)); resultFloorData.put(FLOORS_BY_INPUT, formatted FloorData); result FloorData.put(RATE_BY_KEY, rateByKey (workbook, sheetIndex)); resultFloorData.put(STATE_CODE, codeByState(workbook, 2, 0));

List<Double> minColumnValues getCellValues (workbook, 8, 2); List<Double> maxColumnValues getCellValues (workbook, θ, 3);

if (IminColumnValues.isEmpty() && ImaxColumnValues.isEmpty()) { double minvalue minColumnValues.stream().min(Double::compareTo).orElse (Double.NaN); double maxValue maxColumnvalues.stream().max(Double::compareTo), orElse (Double,NaN); resultFloorData.put(MIN_LOAN_AMOUNT, minValue); result FloorData.put(MAX LOAN AMOUNT, maxValue);

return resultFloorData;

/ Method to get Loan amount low, Fico low & City low"/
  / Method to get Loan amount low, Fico low & Cltv low */

private TreeSet<Object> formatFloor By Input (Workbook workbook, int sheetIndex, int columnIndex) {

Sheet sheet workbook.getSheetAt (sheetIndex); TreeSet<Object> resultSet new TreeSet<>();

for (int rowIndex 1; rowIndex < sheet.getLastRowNum(); rowIndex++) (

Row row sheet.getRow(rowIndex);

if (row ! null) {

Cell cell row.getCell(columnIndex);

if (cell != null) {

String cellvalue getCellValueAsString(cell).replaceAll(DECIMAL VALUE, "");

double value Double.parseDouble(cellValue);

resultSet.add(value);

return (TreeSet<Object>) resultSet.descendingSet();

2

}

}

4

75

76

}

77

78

79

80

81

82

183

54

/ Method to get Fico Band and Average value */
  Method to get Fico Band and Average value */

private TreeMap<String, String> getFicoBandValue (Workbook workbook, int sheetIndex, int columnIndex) ( Sheet sheet workbook.getSheetAt(sheetIndex); TreeSet<String> resultSet new TreeSet<>(); TreeMap<String, String> averageRangeMap new TreeMap<>(Collections.reverseOrder());

for (int rowIndex 1; rowIndex < sheet.getLastRowNum(); rowIndex++) {

Row row sheet.getRow(rowIndex);

if (row I null) (

Cell cell row.getCell(columnIndex);

if (cell != null) {

String cellvalue getCellValueAsString(cell);

resultSet.add(cellValue);

1

for (String range resultSet) (

String[] rangeParts range.split("-");

If (rangeParts.length-2) (

int lowerBound Integer.parseInt(rangeParts[0]);

Int upperBound Integer.parseInt(rangeParts[1]);

double averagevalue (lowerBound upperBound) / 2.01

String avgValue Double.toString(averageValue).replaceAll(DECIMAL VALUE;

average RangeMap.put(avgValue, range);

return averageRangeMap;

} //Method to retrive excel rate and Inan amount by key format lien loanamount-fice-city-state

  * Method to get State data */

DHL webs

}

private HashMap<String, Object> codeByState(Workbook workbook, int num, int headerValue) (

Sheet sheet workbook.getSheetAt(num);

HashMap<String, Object> rowData new HashMap<>();

for (int i=1; i < sheet.getLastRowNum(); i++) {

Row row sheet.getRow(1);

if (row 1- null) (

int staeCode-headerValue+1;

Cell celli row.getCell(headerValue);

Cell cell2 row.getCell(staeCode);

if (celli 1 null && cell2 != null) {

String key getCellValueAsString(celli);

String value getCellValueAsString(cell2).replaceAll(DECIMAL_VALUE, "");

double stateCode Double.parseDouble(value);

rowData.put(key,stateCode);

return rowData;

// Get table's header/first row

private String[] getTableHeader (Workbook workbook, int i) {

Sheet sheet workbook.getSheetAt(i);

Iterator<Row> rowIterator sheet, iterator();

Row row rowlterator.next();

Iterator<Cell> cellIteratorrow.cellIterator();

String[] tableHeader new String[now.getLastCellNum());

for (int index 0; cellIterator.hasNext(); index++) [

Cell cell celliterator.next();

tableHeader[index] cell.getRichStringCellValue().getString();

return tableHeader;
  private List<Double> getCellValues (Workbook workbook, int num, int column) ( Sheet sheet workbook.getSheetAt(num); List<Double> columnValues = new ArrayList<>();

int columnIndexToCheck column;

for (int i=1; i < sheet.getLastRowNum(); i++) (

Row row sheet.getRow(i);

if (row 1 null) (

cell cell row.getCell(columnIndexToCheck, Row.MissingCellPolicy.CREATE NULL AS_BLANK);

if (cell.getCellType() CellType.NUMERIC) (

columnValues.add(cell.getNumericCellValue());

return columnValues;

Methor for Cell data validation */

private static String getCellValueAsString(Cell cell) (

if (cell null) (

return

switch (cell.getCellType()) {

case STRING:

return cell.getStringCellValue();

case NUMERIC:

return String.valueOf(cell.getNumericCellvalue());

case BOOLFAN:

return String.valueOf(cell.getBooleanCellValue());

case FORMULAY

return cell.getCellForsula();

default:

return
  Methor to Store file in DAM JCR *

private Asset storeFileInJCR(String destinationPath, String fileName, InputStream jsonStream)

throws LoginException {

Map<String, Object> serviceNameParam = new HashMap<>();

serviceNameParam.put(Resource ResolverFactory.SUBSERVICE, "DhlPricingGridWorkflowService"); ResourceResolver resolver resolverFactory.getServiceResource Resolver (serviceNameParam);

Asset Manager asset Manager

resolver.adaptTo(AssetManager.class);

if (objects.nonNull(assetManager)) {

LOGGER.info("::: Created:::");

return assetManager.createAsset(

destinationPath.concat("/".concat(fileName)),

jsonStream,

JSON MIME TYPE,

true);

8

1

2

} else {

LOGGER, Info("::: NOT Created:::");

return null;

25

26

}
