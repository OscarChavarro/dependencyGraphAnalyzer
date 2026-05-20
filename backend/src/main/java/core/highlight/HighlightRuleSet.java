package core.highlight;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HighlightRuleSet {
    private int version;
    private Map<String, List<String>> profiles = new LinkedHashMap<>();
    private List<HighlightRuleGroup> groups = new ArrayList<>();

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Map<String, List<String>> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, List<String>> profiles) {
        this.profiles = profiles;
    }

    public List<HighlightRuleGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<HighlightRuleGroup> groups) {
        this.groups = groups;
    }
}
