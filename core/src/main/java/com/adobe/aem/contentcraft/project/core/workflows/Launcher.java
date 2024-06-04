package com.example.aem.eventlistener;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.event.EventConstants;
import org.apache.sling.event.jobs.JobManager;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(
    service = EventHandler.class,
    immediate = true,
    property = {
        EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED"
    }
)
public class DamUploadEventListener implements EventHandler {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private JobManager jobManager;

    private static final String WORKFLOW_PATH = "/var/workflow/models/custom-excel-workflow/jcr:content/model";

    @Override
    public void handleEvent(Event event) {
        String resourcePath = (String) event.getProperty("path");

        if (resourcePath.endsWith(".xlsx") || resourcePath.endsWith(".csv")) {
            triggerWorkflow(resourcePath);
        }
    }

    private void triggerWorkflow(String resourcePath) {
        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(null)) {
            Session session = resourceResolver.adaptTo(Session.class);

            Map<String, Object> workflowData = new HashMap<>();
            workflowData.put("model", WORKFLOW_PATH);
            workflowData.put("payload", resourcePath);

            jobManager.addJob("com/day/cq/workflow/job/execute", workflowData);
        } catch (Exception e) {
            // Handle exception
        }
    }
}
function shouldTriggerWorkflow(resourcePath) {
    var excelPattern = /.*\.xlsx$/;
    var csvPattern = /.*\.csv$/;
    
    var assets = [];
    var folder = resourcePath.substring(0, resourcePath.lastIndexOf('/'));

    var resources = resourceResolver.getResource(folder);
    var children = resources.getChildren();

    for (var child in children) {
        assets.push(child.getName());
    }

    var hasExcel = assets.some(asset => excelPattern.test(asset));
    var hasCSV = assets.some(asset => csvPattern.test(asset));

    return hasExcel && hasCSV;
}

shouldTriggerWorkflow(resourcePath);
