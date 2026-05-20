package core.highlight;

import java.util.ArrayList;
import java.util.List;

public class HighlightRuleSubGroup {
    private String name;
    private List<HighlightRule> rules = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HighlightRule> getRules() {
        return rules;
    }

    public void setRules(List<HighlightRule> rules) {
        this.rules = rules;
    }
}
