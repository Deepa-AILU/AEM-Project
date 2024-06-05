package com.example.aem.core.services;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component(service = CsvReaderService.class)
public class CsvReaderService {

    private static final Logger LOG = LoggerFactory.getLogger(CsvReaderService.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public String readCsvFile(String csvFilePath) {
        ResourceResolver resourceResolver = null;
        String csvContent = null;

        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "readService"));

            Resource csvResource = resourceResolver.getResource(csvFilePath + "/jcr:content/renditions/original/jcr:content");
            if (csvResource != null) {
                ValueMap properties = csvResource.getValueMap();
                InputStream inputStream = properties.get("jcr:data", InputStream.class);

                if (inputStream != null) {
                    csvContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            LOG.error("Error reading CSV file from DAM", e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }

        return csvContent;
    }
}
