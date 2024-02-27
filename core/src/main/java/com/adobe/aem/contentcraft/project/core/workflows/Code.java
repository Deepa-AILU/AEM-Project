import java.util.HashMap;
import java.util.HashSet;

public class RowToHashMap {

    public static void main(String[] args) {
        // Sample row
        String[] rowArray = {"Key1", "Value1", "Value2", "Value1"};

        // Convert row to HashMap<String, Object>
        HashMap<String, Object> rowHashMap = convertRowToHashMap(rowArray);

        // Display the result
        System.out.println(rowHashMap);
    }

    private static HashMap<String, Object> convertRowToHashMap(String[] row) {
        HashMap<String, Object> hashMap = new HashMap<>();

        // Use the first cell value as the key
        String key = row[0];

        // Use a HashSet to store unique values for other cells
        HashSet<Object> valuesSet = new HashSet<>();

        // Loop through the row starting from the second cell
        for (int i = 1; i < row.length; i++) {
            // Add the current cell value to the set (ignoring duplicates)
            valuesSet.add(row[i]);
        }

        // Store the key and values set in the HashMap
        hashMap.put(key, valuesSet);

        return hashMap;
    }
}
