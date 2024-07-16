import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<CheckResult> results = Arrays.asList(
            new CheckResult("rule1", true, "no error"),
            new CheckResult("rule2", false, "some error"),
            new CheckResult("rule3", true, "no error")
        );

        boolean hasFailed = false;
        for (CheckResult result : results) {
            if (!result.isPassed()) {
                hasFailed = true;
                break;
            }
        }

        System.out.println("Contains a failed check: " + hasFailed);
    }
}

class CheckResult {
    private String rule;
    private boolean passed;
    private String error;

    public CheckResult(String rule, boolean passed, String error) {
        this.rule = rule;
        this.passed = passed;
        this.error = error;
    }

    public boolean isPassed() {
        return passed;
    }

    // getters and setters for other fields (if needed)
}
document.querySelectorAll('.bar-Labels').forEach(label => {
    label.innerHTML = `<div class="value">${formatValue(pmtValue)}</div>`;
});
