package com.example.models;

import com.adobe.cq.dam.api.Asset;
import com.adobe.cq.dam.api.AssetManager;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentFragmentElement;
import com.adobe.cq.dam.cfm.FragmentData;
import com.adobe.cq.dam.cfm.FragmentTemplate;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.export.json.SlingModelExporter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = { SlingModelExporter.class },
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class JsonDropdownModel implements SlingModelExporter {

    @Inject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String jsonFilePath; // Path to JSON file in DAM

    private Map<String, String> dropdownOptions;

    @PostConstruct
    protected void init() {
        if (jsonFilePath != null) {
            try {
                Resource jsonResource = resourceResolver.getResource(jsonFilePath);
                if (jsonResource != null) {
                    Asset asset = jsonResource.adaptTo(Asset.class);
                    if (asset != null) {
                        InputStream inputStream = asset.getOriginal().getStream();
                        String jsonContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> jsonData = objectMapper.readValue(jsonContent, Map.class);
                        if (jsonData != null) {
                            dropdownOptions = new HashMap<>();
                            for (Map.Entry<String, Object> entry : jsonData.entrySet()) {
                                dropdownOptions.put(entry.getKey(), entry.getValue().toString());
                            }
                        }
                    }
                }
            } catch (IOException e) {
                // Handle exception
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> getDropdownOptions() {
        return dropdownOptions;
    }

    @Override
    @JsonIgnore
    public String getExportedType() {
        return ExporterConstants.SLING_MODEL_EXPORTER_NAME;
    }
}
