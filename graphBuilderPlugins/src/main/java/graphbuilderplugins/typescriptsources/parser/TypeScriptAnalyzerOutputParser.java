package graphbuilderplugins.typescriptsources.parser;

import graphbuilderplugins.typescriptsources.model.TypeScriptDependencyGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TypeScriptAnalyzerOutputParser {
    private static final Pattern NODES_PATTERN = Pattern.compile("\\\"nodes\\\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
    private static final Pattern BAD_NODES_PATTERN = Pattern.compile("\\\"badNodes\\\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
    private static final Pattern EDGES_PATTERN = Pattern.compile("\\\"edges\\\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
    private static final Pattern EDGE_PATTERN = Pattern.compile(
            "\\{\\s*\\\"source\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*,\\s*\\\"target\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*\\}");

    public TypeScriptDependencyGraph parse(String json) {
        if (json == null || json.isBlank()) {
            return TypeScriptDependencyGraph.empty();
        }

        List<String> nodes = parseStringArray(json, NODES_PATTERN);
        List<String> badNodes = parseStringArray(json, BAD_NODES_PATTERN);
        List<TypeScriptDependencyGraph.DependencyEdge> edges = parseEdges(json);

        return TypeScriptDependencyGraph.fromLists(nodes, edges, badNodes);
    }

    private List<String> parseStringArray(String json, Pattern pattern) {
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return List.of();
        }

        String body = matcher.group(1);
        List<String> values = new ArrayList<>();
        if (body == null || body.isBlank()) {
            return values;
        }

        String[] rawValues = body.split(",");
        for (String rawValue : rawValues) {
            String cleaned = rawValue.trim();
            if (cleaned.startsWith("\"") && cleaned.endsWith("\"") && cleaned.length() >= 2) {
                cleaned = cleaned.substring(1, cleaned.length() - 1);
            }
            cleaned = cleaned.replace("\\\\", "\\").trim();
            if (!cleaned.isEmpty()) {
                values.add(cleaned);
            }
        }
        return values;
    }

    private List<TypeScriptDependencyGraph.DependencyEdge> parseEdges(String json) {
        Matcher edgesMatcher = EDGES_PATTERN.matcher(json);
        if (!edgesMatcher.find()) {
            return List.of();
        }

        String edgesBody = edgesMatcher.group(1);
        if (edgesBody == null || edgesBody.isBlank()) {
            return List.of();
        }

        List<TypeScriptDependencyGraph.DependencyEdge> edges = new ArrayList<>();
        Matcher edgeMatcher = EDGE_PATTERN.matcher(edgesBody);
        while (edgeMatcher.find()) {
            String source = edgeMatcher.group(1).trim();
            String target = edgeMatcher.group(2).trim();
            if (!source.isEmpty() && !target.isEmpty()) {
                edges.add(new TypeScriptDependencyGraph.DependencyEdge(source, target));
            }
        }
        return edges;
    }
}
