import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YourClass {

    public static Map<String, Map<String, Object>> rateAndAmountByKey(List<YourItemType> items) {
        return items.stream()
                .filter(item -> !"Ineligible".equals(item.getRate()))
                .collect(Collectors.toMap(
                        item -> makeRateMapKey(item),
                        item -> {
                            Map<String, Object> value = new HashMap<>();
                            value.put("maximumLoanAmountForThisItem", item.getAmountHigh());
                            value.put("rate", Double.parseDouble(String.format("%.4f", Double.parseDouble(item.getRate()))));
                            return value;
                        }
                ));
    }

    private static String makeRateMapKey(YourItemType item) {
        return String.format("%s_%s_%s_%s_%s",
                item.isFirstLien() ? "1stLien" : "Not1stLien",
                item.getScoreLow(),
                item.getCltvLow(),
                item.getAmountLow(),
                item.getState());
    }

    private static class YourItemType {
        // Define your item properties and methods here
        // Replace these placeholders with actual properties and types
        public boolean isFirstLien() {
            // Implement the actual logic
            return false;
        }

        public String getRate() {
            // Implement the actual logic
            return "";
        }

        public String getScoreLow() {
            // Implement the actual logic
            return "";
        }

        public String getCltvLow() {
            // Implement the actual logic
            return "";
        }

        public String getAmountLow() {
            // Implement the actual logic
            return "";
        }

        public String getAmountHigh() {
            // Implement the actual logic
            return "";
        }

        public String getState() {
            // Implement the actual logic
            return "";
        }
    }
}
