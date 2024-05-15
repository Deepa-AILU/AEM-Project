import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangeAverageCalculator {

    public static void main(String[] args) {
        List<String> rangeList = new ArrayList<>();
        rangeList.add("800-850");
        rangeList.add("780-799");
        rangeList.add("760-779");
        rangeList.add("740-759");
        rangeList.add("720-739");
        rangeList.add("700-719");
        rangeList.add("680-699");
        rangeList.add("670-679");

        Map<Double, String> averageRangeMap = new HashMap<>();

        for (String range : rangeList) {
            // Split the range string into two parts based on the "-"
            String[] rangeParts = range.split("-");
            if (rangeParts.length == 2) {
                try {
                    int lowerBound = Integer.parseInt(rangeParts[0]);
                    int upperBound = Integer.parseInt(rangeParts[1]);

                    // Calculate the average of the range
                    double averageValue = (lowerBound + upperBound) / 2.0;

                    // Store the average value as key and the original range as value in the map
                    averageRangeMap.put(averageValue, range);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid range format: " + range);
                }
            } else {
                System.out.println("Invalid range format: " + range);
            }
        }

        // Print the averageRangeMap or use it as needed
        for (Map.Entry<Double, String> entry : averageRangeMap.entrySet()) {
            System.out.println("Average Value: " + entry.getKey() + ", Range: " + entry.getValue());
        }
    }
}
