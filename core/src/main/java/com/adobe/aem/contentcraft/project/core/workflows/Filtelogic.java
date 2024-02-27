import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        // Assume you have a List<HashMap<String, Object>> jsonDataList

        List<HashMap<String, Object>> jsonDataList = new ArrayList<>();

        // Populate jsonDataList from your Excel sheet

        String targetKey = "specificKey"; // Replace with your desired key

        // Assuming you have jsonDataList populated from your Excel sheet

        HashMap<String, Object> reducedData = jsonDataList.stream()
                .flatMap(map -> map.entrySet().stream()) // Flatten the maps to stream of entries
                .filter(entry -> entry.getKey().equals(targetKey)) // Filter by the specific key
                .collect(HashMap::new, (resultMap, entry) -> {
                    // Accumulate values for the specific key
                    resultMap.merge(entry.getKey(), entry.getValue(), (oldValue, newValue) -> {
                        // Your logic to reduce values for the specific key
                        // For example, summing numeric values
                        if (oldValue instanceof Number && newValue instanceof Number) {
                            return ((Number) oldValue).doubleValue() + ((Number) newValue).doubleValue();
                        }
                        return newValue; // Default behavior if not numeric
                    });
                }, HashMap::putAll);

        System.out.println("Reduced Data: " + reducedData);
    }
}
