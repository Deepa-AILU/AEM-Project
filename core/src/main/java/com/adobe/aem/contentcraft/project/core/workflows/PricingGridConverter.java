import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.WorkflowProcess;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PricingGridConverter  implements WorkflowProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingGridWorkflowProcess.class);
    private static final String SUBSERVICE_NAME = "DhiPricingGridWorkflowService";
    private static final String DESTINATION_PATH = "/content/dam/dfs/dfs-home-loans/vendor-scripts/pricing-grid";
    private static final String FILE_EXTENSION = ".json";
    
    private final ResourceResolverFactory resolverFactory;

    public PricingGridWorkflowProcess(ResourceResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
    }

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE_NAME);

        try (final ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(param)) {
            String path = workItem.getWorkflowData().getPayload().toString();
            path = path.replace("/jcr:content/renditions/original", "");

            String orgJsonData = getJsonData(path, resourceResolver, workItem);

            if (orgJsonData != null) {
                InputStream jsonStream = new ByteArrayInputStream(orgJsonData.getBytes(StandardCharsets.UTF_8));
                storeFileInJCR(DESTINATION_PATH, "Ohl-Rates" + FILE_EXTENSION, jsonStream, resourceResolver);
            }
        } catch (LoginException | PersistenceException e) {
            LOGGER.error("Error Occurred: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private String getJsonData(String path, ResourceResolver resourceResolver, WorkItem workItem) {
        // Implement logic to retrieve JSON data from the path
        // Return the JSON data as a string
        return null; // Placeholder return statement
    }

    private void storeFileInJCR(String destinationPath, String fileName, InputStream contentStream, ResourceResolver resourceResolver) throws PersistenceException {
        // Implement logic to store the file in JCR
        // Commit changes to the repository
    }
}
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ExcelToJsonConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelToJsonConverter.class);
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    private String getJsonData(String excelPath, ResourceResolver resourceResolver, WorkItem workItem) {
        // File's mime type
        String mimeType = FilenameUtils.getExtension(excelPath);

        Resource resource = resourceResolver.getResource(excelPath);
        LOGGER.info("Resource: {}", resource);

        if (resource == null) {
            LOGGER.error("Resource not found at path: {}", excelPath);
            return null;
        }

        Asset asset = resource.adaptTo(Asset.class);
        LOGGER.info("Asset: {}", asset);

        if (asset == null) {
            LOGGER.error("Asset not found at path: {}", excelPath);
            return null;
        }

        Resource original = asset.getOriginal();
        if (original == null) {
            LOGGER.error("Original resource not found for asset at path: {}", excelPath);
            return null;
        }

        Workbook workbook = null;
        JSONObject jsonObject = null;
        MetaDataMap wfd = workItem.getWorkflow().getMetaDataMap();
        Map<String, Object> excelData = new LinkedHashMap<>();

        if (StringUtils.isNotEmpty(mimeType) && (mimeType.equalsIgnoreCase(XLS) || mimeType.equalsIgnoreCase(XLSX))) {
            try (InputStream stream = original.adaptTo(InputStream.class)) {
                if (Objects.nonNull(stream)) {
                    if (mimeType.equalsIgnoreCase(XLSX)) {
                        workbook = new XSSFWorkbook(stream);
                    } else if (mimeType.equalsIgnoreCase(XLS)) {
                        workbook = new HSSFWorkbook(stream);
                    } else {
                        return null;
                    }

                    // Get list of map objects
                    excelData = parseExcelData(workbook, 0, 0); // Placeholder for actual implementation

                    // Parse list of map objects to JSON array
                    jsonObject = new JSONObject(excelData);
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred: {}", e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return jsonObject != null ? jsonObject.toString() : null;
    }

    private Map<String, Object> parseExcelData(Workbook workbook, int sheetIndex, int startRow) {
        // Implement logic to parse the Excel workbook into a Map
        // Placeholder for actual implementation
        return new LinkedHashMap<>();
    }
}
