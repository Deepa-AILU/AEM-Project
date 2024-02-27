import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PricingGridConverter {
    public static void main(String[] args) {
        List<Map<String, Object>> pricingGridData = /* Your pricingGrid data */;
        
        Map<String, Double> rateAndAmountByKey = pricingGridData.stream()
                .filter(item -> !"Ineligible".equals(item.get("rate"))) // Filter out items with rate "Ineligible"
                .reduce(new HashMap<>(), (byKey, item) -> {
                    String key = makeRateMapKey(item); // Assuming you have a function for this
                    boolean isFirstLien = "1st Lien".equals(item.get("lien"));
                    double score = Double.parseDouble(item.get("scorelow").toString());
                    double cltv = Double.parseDouble(item.get("cltvLow").toString().replace("", ""));
                    double amountLow = Double.parseDouble(item.get("amountLow").toString());
                    double amountHigh = Double.parseDouble(item.get("amountHigh").toString());
                    String state = item.get("state").toString();
                    double rate = Double.parseDouble(item.get("rate").toString());
                    
                    byKey.put(key, calculateRate(isFirstLien, score, cltv, amountLow, amountHigh, state, rate));
                    
                    return byKey;
                }, (m1, m2) -> {
                    m1.putAll(m2);
                    return m1;
                });

        System.out.println("Rate and Amount by Key: " + rateAndAmountByKey);
    }

    private static String makeRateMapKey(Map<String, Object> item) {
        // Implement the logic to generate the key based on item values
        // For example, concatenate relevant values or use a specific format
        return item.get("someKey").toString(); // Replace with your actual key generation logic
    }

    private static double calculateRate(boolean isFirstLien, double score, double cltv, double amountLow,
                                        double amountHigh, String state, double rate) {
        // Implement your logic to calculate the rate based on the provided parameters
        // For example, you can use a formula or apply specific business rules
        return rate; // Replace with your actual rate calculation logic
    }
}
