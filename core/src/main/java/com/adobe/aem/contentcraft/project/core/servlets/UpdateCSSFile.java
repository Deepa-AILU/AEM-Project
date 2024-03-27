package com.example.servlets;

import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Reference;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=AEM Clientlib CSS Servlet",
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/clientlibcss",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=GET"
})
public class ClientlibCssServlet extends SlingSafeMethodsServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientlibCssServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        
        ResourceResolver resourceResolver = null;
        try {
            // Get a resource resolver
            resourceResolver = resolverFactory.getServiceResourceResolver(null);
            
            // Get the clientlib path
            String clientlibPath = "/apps/myapp/clientlibs";
            Resource clientlibResource = resourceResolver.getResource(clientlibPath);
            
            if (clientlibResource != null) {
                // Get the css.txt file
                Resource cssTxtResource = clientlibResource.getChild("css.txt");
                
                if (cssTxtResource != null) {
                    // Read the content of css.txt
                    InputStream inputStream = cssTxtResource.adaptTo(InputStream.class);
                    if (inputStream != null) {
                        List<String> cssFileNames = new ArrayList<>();
                        List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
                        
                        // Extract CSS file names from css.txt
                        for (String line : lines) {
                            if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
                                String[] parts = line.split("\\|", 2);
                                if (parts.length > 1) {
                                    cssFileNames.add(parts[1].trim());
                                }
                            }
                        }
                        
                        // Write CSS file names to the response
                        for (String cssFileName : cssFileNames) {
                            response.getWriter().println(cssFileName);
                        }
                    }
                } else {
                    LOGGER.error("css.txt not found in clientlib: {}", clientlibPath);
                }
            } else {
                LOGGER.error("Clientlib not found: {}", clientlibPath);
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving clientlib CSS file names", e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
                        }
