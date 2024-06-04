import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.BundleContext;
import org.osgi.service.jcr.RepositoryAccessBundle;

public class AddCustomMetadataServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String CUSTOM_PROPERTY_NAME = "customProperty";
    private static final String FOLDER_PATH = "/content/dam/myDAMFolder"; // Replace with your folder path

    private Session session;

    @Override
    protected void activate(BundleContext context) throws Exception {
        super.activate(context);
        session = getService(RepositoryAccessBundle.class).getRepository().login(administrative);
    }

    @Override
    protected void deactivate(BundleContext context) throws Exception {
        if (session != null) {
            session.logout();
        }
        super.deactivate(context);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Resource resource = request.getResource();
        if (resource == null) {
            return;
        }

        String path = resource.getPath();
        if (path.startsWith(FOLDER_PATH) && (path.endsWith(".csv") || path.endsWith(".xlsx"))) {
            try {
                updateMetadata(path);
                response.setStatus(200);
            } catch (RepositoryException e) {
                log.error("Error adding custom metadata:", e);
                response.sendError(500);
            }
        }
    }

    private void updateMetadata(String path) throws RepositoryException {
        Node node = session.getNode(path);

        // Get today's date in ISO 8601 format (YYYY-MM-DD)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(Calendar.getInstance().getTime());

        node.setProperty(CUSTOM_PROPERTY_NAME, date);
        session.save();
    }
}
