package co.cubestudio.highlight;

import java.util.ArrayList;
import java.util.List;

public class HighlightRuleGroup {
    private String name;
    private String groupComment;
    private List<HighlightRuleSubGroup> subGroups = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupComment() {
        return groupComment;
    }

    public void setGroupComment(String groupComment) {
        this.groupComment = groupComment;
    }

    public List<HighlightRuleSubGroup> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<HighlightRuleSubGroup> subGroups) {
        this.subGroups = subGroups;
    }
}
