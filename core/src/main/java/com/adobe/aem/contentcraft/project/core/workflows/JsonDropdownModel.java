package com.example.models;

import com.adobe.cq.dam.api.Asset;
import com.adobe.cq.dam.api.AssetManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Model(
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FicoBandRangeDropdownModel {

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
                        
                        // Filter out the object with key "ficoBandRange"
                        if (jsonData != null && jsonData.containsKey("ficoBandRange")) {
                            Map<String, Object> ficoBandRangeData = (Map<String, Object>) jsonData.get("ficoBandRange");
                            dropdownOptions = new HashMap<>();
                            for (Map.Entry<String, Object> entry : ficoBandRangeData.entrySet()) {
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

    @JsonIgnore
    public String getJsonFilePath() {
        return jsonFilePath;
    }
                            }
