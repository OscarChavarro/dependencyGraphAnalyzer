package graphbuilderplugins.typescriptsources.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TypeScriptDependencyGraph {
    private final List<TypeScriptSourceModuleNode> nodes;
    private final List<DependencyEdge> edges;

    public TypeScriptDependencyGraph(List<TypeScriptSourceModuleNode> nodes, List<DependencyEdge> edges) {
        this.nodes = List.copyOf(nodes);
        this.edges = List.copyOf(edges);
    }

    public static TypeScriptDependencyGraph empty() {
        return new TypeScriptDependencyGraph(List.of(), List.of());
    }

    public static TypeScriptDependencyGraph fromLists(
            List<String> goodNodes,
            List<DependencyEdge> edges,
            List<String> badNodes) {
        List<TypeScriptSourceModuleNode> allNodes = new ArrayList<>();
        for (String node : goodNodes) {
            allNodes.add(new TypeScriptSourceModuleNode(node, false));
        }
        for (String node : badNodes) {
            allNodes.add(new TypeScriptSourceModuleNode(node, true));
        }
        return new TypeScriptDependencyGraph(allNodes, edges);
    }

    public List<TypeScriptSourceModuleNode> nodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<DependencyEdge> edges() {
        return Collections.unmodifiableList(edges);
    }

    public List<String> badNodes() {
        return nodes.stream().filter(TypeScriptSourceModuleNode::bad).map(TypeScriptSourceModuleNode::modulePath).toList();
    }

    public record DependencyEdge(String source, String target) {
    }
}
