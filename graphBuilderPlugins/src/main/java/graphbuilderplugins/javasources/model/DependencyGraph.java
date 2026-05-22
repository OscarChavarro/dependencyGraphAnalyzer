package graphbuilderplugins.javasources.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class DependencyGraph {
    private final Map<String, SourceClassNode> nodes;

    public DependencyGraph() {
        this(Map.of());
    }

    public DependencyGraph(Map<String, SourceClassNode> nodes) {
        LinkedHashMap<String, SourceClassNode> copy = new LinkedHashMap<>();
        if (nodes != null) {
            for (Map.Entry<String, SourceClassNode> entry : nodes.entrySet()) {
                SourceClassNode node = Objects.requireNonNull(entry.getValue(), "node must not be null");
                copy.put(node.fqcn(), node);
            }
        }
        this.nodes = Collections.unmodifiableMap(copy);
    }

    public static DependencyGraph empty() {
        return new DependencyGraph();
    }

    public Map<String, SourceClassNode> nodes() {
        return nodes;
    }

    public Optional<SourceClassNode> findNode(String fqcn) {
        return Optional.ofNullable(nodes.get(normalizeFqcn(fqcn)));
    }

    public DependencyGraph registerNode(String fqcn) {
        String normalized = normalizeFqcn(fqcn);
        if (nodes.containsKey(normalized)) {
            return this;
        }

        LinkedHashMap<String, SourceClassNode> updated = new LinkedHashMap<>(nodes);
        updated.put(normalized, new SourceClassNode(normalized));
        return new DependencyGraph(updated);
    }

    public DependencyGraph registerEdge(String sourceFqcn, String targetFqcn) {
        String source = normalizeFqcn(sourceFqcn);
        String target = normalizeFqcn(targetFqcn);
        if (source.equals(target)) {
            return registerNode(source);
        }

        DependencyGraph withNodes = registerNode(source).registerNode(target);
        SourceClassNode sourceNode = withNodes.nodes.get(source);
        SourceClassNode updatedSource = sourceNode.withDependency(target);

        if (updatedSource == sourceNode) {
            return withNodes;
        }

        LinkedHashMap<String, SourceClassNode> updated = new LinkedHashMap<>(withNodes.nodes);
        updated.put(source, updatedSource);
        return new DependencyGraph(updated);
    }

    public List<List<String>> exportAdjacencyList() {
        return nodes.values().stream()
                .map(node -> {
                    List<String> row = new java.util.ArrayList<>();
                    row.add(node.fqcn());
                    row.addAll(node.outgoingDependencies());
                    return List.copyOf(row);
                })
                .toList();
    }

    private static String normalizeFqcn(String fqcn) {
        String normalized = Objects.requireNonNull(fqcn, "fqcn must not be null").trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("fqcn must not be blank");
        }
        // Keep source-style canonical names for nested classes.
        return normalized.replace('$', '.');
    }
}
