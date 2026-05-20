package co.cubestudio.highlight;

import co.cubestudio.SoftwarePackageGraph;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighlightRulesEngine {
    private final SoftwarePackageGraph graph;
    private final HighlightRuleSet ruleSet;
    private final Map<String, HighlightRuleGroup> groupsByName;

    public HighlightRulesEngine(SoftwarePackageGraph graph, HighlightRuleSet ruleSet) {
        this.graph = graph;
        this.ruleSet = ruleSet;
        this.groupsByName = new HashMap<>();
        for (HighlightRuleGroup group : ruleSet.getGroups()) {
            groupsByName.put(group.getName(), group);
        }
    }

    public static HighlightRulesEngine fromJsonFile(SoftwarePackageGraph graph, Path path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HighlightRuleSet set = mapper.readValue(path.toFile(), HighlightRuleSet.class);
            return new HighlightRulesEngine(graph, set);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load highlight rules from " + path, e);
        }
    }

    public void applyProfile(String profileName) {
        List<String> orderedGroups = ruleSet.getProfiles().get(profileName);
        if (orderedGroups == null) {
            throw new IllegalArgumentException("Unknown highlight profile: " + profileName);
        }

        for (String groupName : orderedGroups) {
            HighlightRuleGroup group = groupsByName.get(groupName);
            if (group == null) {
                continue;
            }
            applyGroup(group);
        }
    }

    private void applyGroup(HighlightRuleGroup group) {
        for (HighlightRuleSubGroup subGroup : group.getSubGroups()) {
            for (HighlightRule rule : subGroup.getRules()) {
                switch (rule.getOperation()) {
                    case "markPackageAndItsDependencies":
                        graph.markPackageAndItsDependencies(rule.getPackageName());
                        break;
                    case "markPackageAndItsClients":
                        graph.markPackageAndItsClients(rule.getPackageName());
                        break;
                    case "annotateSingleNode":
                        graph.anotateSingleNode(rule.getPackageName());
                        break;
                    case "annotateSingleNodeArchVariants":
                        graph.anotateSingleNode(rule.getPackageName());
                        graph.anotateSingleNode(rule.getPackageName() + ":amd64");
                        graph.anotateSingleNode(rule.getPackageName() + ":i386");
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unknown rule operation '" + rule.getOperation() + "' for package " + rule.getPackageName());
                }
            }
        }
    }
}
