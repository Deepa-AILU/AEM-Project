package com.example.aem.workflow;

import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.exec.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.workflow.exec.WorkflowData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

@Component(
        service = WorkflowProcess.class,
        property = {"process.label=Read XLSX and CSV Workflow Process"}
)
public class ReadExcelAndCSVProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(ReadExcelAndCSVProcess.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        WorkflowData workflowData = workItem.getWorkflowData();
        String payloadPath = workflowData.getPayload().toString();

        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(null)) {
            Resource resource = resolver.getResource(payloadPath);
            if (resource != null) {
                Asset asset = resource.adaptTo(Asset.class);
                if (asset != null) {
                    String mimeType = asset.getMimeType();
                    if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(mimeType)) {
                        readExcelFile(asset);
                    } else if ("text/csv".equals(mimeType)) {
                        readCSVFile(asset);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error processing workflow step", e);
        }
    }

    private void readExcelFile(Asset asset) {
        Rendition original = asset.getOriginal();
        try (InputStream inputStream = original.adaptTo(InputStream.class)) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            log.info("String value: {}", cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            log.info("Numeric value: {}", cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error reading Excel file", e);
        }
    }

    private void readCSVFile(Asset asset) {
        Rendition original = asset.getOriginal();
        try (InputStream inputStream = original.adaptTo(InputStream.class);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("CSV line: {}", line);
            }
        } catch (Exception e) {
            log.error("Error reading CSV file", e);
        }
    }
}
