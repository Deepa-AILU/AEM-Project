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

raparatlan) throws
