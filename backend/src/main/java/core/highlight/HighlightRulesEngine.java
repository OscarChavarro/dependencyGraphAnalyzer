package core.highlight;

import core.graph.SoftwarePackageGraph;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Path resolvedPath = resolveRulesPath(path);
        try {
            ObjectMapper mapper = new ObjectMapper();
            HighlightRuleSet set = mapper.readValue(resolvedPath.toFile(), HighlightRuleSet.class);
            return new HighlightRulesEngine(graph, set);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load highlight rules. input='" + path + "', tried=["
                    + toAbsolute(path) + ", " + toAbsolute(path.normalize()) + ", "
                    + toAbsolute(Paths.get(path.toString().replaceFirst("^\\.\\./", ""))) + ", "
                    + toAbsolute(Paths.get(path.toString().replaceFirst("^\\./", ""))) + ", "
                    + toAbsolute(Paths.get("../" + path.toString())) + "]", e);
        }
    }

    private static Path resolveRulesPath(Path inputPath) {
        Path firstTry = inputPath;
        if (Files.isRegularFile(firstTry)) {
            return firstTry.normalize();
        }

        Path normalized = inputPath.normalize();
        if (Files.isRegularFile(normalized)) {
            return normalized;
        }

        String raw = inputPath.toString();
        Path withoutParentPrefix = Paths.get(raw.replaceFirst("^\\.\\./", ""));
        if (Files.isRegularFile(withoutParentPrefix)) {
            return withoutParentPrefix.normalize();
        }

        Path withoutDotPrefix = Paths.get(raw.replaceFirst("^\\./", ""));
        if (Files.isRegularFile(withoutDotPrefix)) {
            return withoutDotPrefix.normalize();
        }

        Path withParentPrefix = Paths.get("../" + raw);
        if (Files.isRegularFile(withParentPrefix)) {
            return withParentPrefix.normalize();
        }

        return inputPath.normalize();
    }

    private static Path toAbsolute(Path path) {
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        return cwd.resolve(path).normalize();
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
