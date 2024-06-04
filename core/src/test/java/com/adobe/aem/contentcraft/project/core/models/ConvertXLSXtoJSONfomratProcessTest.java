import org.apache.sling.api.resource.*;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConvertXLSXtoJSONfomratProcessTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertXLSXtoJSONfomratProcess.class);

    @Mock
    private ResourceResolverFactory resolverFactory;

    @Mock
    private ResourceResolver resourceResolver;

    @InjectMocks
    private ConvertXLSXtoJSONfomratProcess convertXLSXtoJSONfomratProcess;

    private SlingContext context;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        context = new SlingContext();
        when(resolverFactory.getServiceResourceResolver(any(Map.class))).thenReturn(resourceResolver);
    }

    @Test
    void testExecute() throws Exception {
        // Setup
        String testExcelPath = "/content/dam/test/test.xlsx";
        String testJsonData = "{\"key\":\"value\"}";
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "DhlPricingGridWorkflowService");

        WorkItem workItem = mock(WorkItem.class);
        WorkflowSession workflowSession = mock(WorkflowSession.class);
        MetaDataMap metaDataMap = mock(MetaDataMap.class);

        when(workItem.getWorkflowData().getPayload()).thenReturn(testExcelPath);
        Resource resource = mock(Resource.class);
        when(resourceResolver.getResource(testExcelPath)).thenReturn(resource);
        Asset asset = mock(Asset.class);
        when(resource.adaptTo(Asset.class)).thenReturn(asset);
        Resource original = mock(Resource.class);
        when(asset.getOriginal()).thenReturn(original);
        InputStream stream = new ByteArrayInputStream(testJsonData.getBytes(StandardCharsets.UTF_8));
        when(original.adaptTo(InputStream.class)).thenReturn(stream);

        // Execute
        convertXLSXtoJSONfomratProcess.execute(workItem, workflowSession, metaDataMap);

        // Verify
        verify(resourceResolver, times(1)).commit();
    }

    @Test
    void testGetJsonData() throws Exception {
        // Setup
        Resource original = mock(Resource.class);
        InputStream stream = new ByteArrayInputStream("{\"key\":\"value\"}".getBytes(StandardCharsets.UTF_8));
        when(original.adaptTo(InputStream.class)).thenReturn(stream);

        // Execute
        String jsonData = convertXLSXtoJSONfomratProcess.getJsonData(original, "xlsx");

        // Verify
        assertNotNull(jsonData);
        JSONObject jsonObject = new JSONObject(jsonData);
        assertEquals("value", jsonObject.getString("key"));
    }
}
