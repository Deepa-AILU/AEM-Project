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
import java.io.OutputStream;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=AEM Clientlib CSS Update Servlet",
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/clientlibcss/update",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=POST"
})
public class ClientlibCssUpdateServlet extends SlingAllMethodsServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientlibCssUpdateServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        
        String clientlibPath = "/apps/myapp/clientlibs"; // Change this to your client library path
        String newCssFileName = request.getParameter("newCssFileName");
        
        if (newCssFileName == null || newCssFileName.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("New CSS file name parameter is missing");
            return;
        }
        
        ResourceResolver resourceResolver = null;
        try {
            // Get a resource resolver
            resourceResolver = resolverFactory.getServiceResourceResolver(null);
            
            // Get the clientlib resource
            Resource clientlibResource = resourceResolver.getResource(clientlibPath);
            
            if (clientlibResource != null) {
                // Get the css.txt file
                Resource cssTxtResource = clientlibResource.getChild("css.txt");
                
                if (cssTxtResource != null) {
                    // Read the existing content of css.txt
                    InputStream inputStream = cssTxtResource.adaptTo(InputStream.class);
                    List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
                    lines.add(newCssFileName); // Append the new CSS file name
                    
                    // Write the modified content back to css.txt
                    OutputStream outputStream = resourceResolver.getResource(cssTxtResource.getPath()).adaptTo(OutputStream.class);
                    IOUtils.writeLines(lines, null, outputStream, "UTF-8");
                    outputStream.flush();
                    outputStream.close();
                    
                    response.getWriter().println("New CSS file name added successfully");
                } else {
                    LOGGER.error("css.txt not found in clientlib: {}", clientlibPath);
                    response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("css.txt not found in clientlib");
                }
            } else {
                LOGGER.error("Clientlib not found: {}", clientlibPath);
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Clientlib not found");
            }
        } catch (Exception e) {
            LOGGER.error("Error updating clientlib CSS file names", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error updating clientlib CSS file names");
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
}
