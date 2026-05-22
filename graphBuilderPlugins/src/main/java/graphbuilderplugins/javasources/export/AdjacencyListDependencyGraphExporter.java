package graphbuilderplugins.javasources.export;

import graphbuilderplugins.javasources.model.DependencyGraph;
import graphbuilderplugins.javasources.model.SourceClassNode;
import java.util.Objects;

public final class AdjacencyListDependencyGraphExporter implements DependencyGraphExporter {
    @Override
    public String export(DependencyGraph graph) {
        Objects.requireNonNull(graph, "graph must not be null");

        StringBuilder output = new StringBuilder();
        for (SourceClassNode node : graph.nodes().values()) {
            output.append(node.fqcn()).append('\n');
            for (String dependency : node.outgoingDependencies()) {
                output.append("  ").append(dependency).append('\n');
            }
        }
        return output.toString();
    }
}
