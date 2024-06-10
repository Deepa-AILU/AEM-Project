import java.util.HashMap;
import java.util.Map;

public class UniqueKeyMap {
    private Map<String, String> map;

    public UniqueKeyMap() {
        map = new HashMap<>();
    }

    public void add(String key, String value) {
        if (map.containsKey(key)) {
            int count = 1;
            String newKey = key + count;
            while (map.containsKey(newKey)) {
                count++;
                newKey = key + count;
            }
            map.put(newKey, value);
        } else {
            map.put(key, value);
        }
    }

    public String get(String key) {
        return map.get(key);
    }

    public void printMap() {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        UniqueKeyMap uniqueKeyMap = new UniqueKeyMap();
        uniqueKeyMap.add("key", "value1");
        uniqueKeyMap.add("key", "value2");
        uniqueKeyMap.add("key", "value3");
        uniqueKeyMap.add("key", "value4");

        uniqueKeyMap.printMap();
    }
}
