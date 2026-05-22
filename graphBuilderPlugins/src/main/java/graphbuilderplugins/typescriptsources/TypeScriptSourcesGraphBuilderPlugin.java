package graphbuilderplugins.typescriptsources;

import graphbuilderplugins.api.GraphBuildTarget;
import graphbuilderplugins.api.GraphBuilderPlugin;
import graphbuilderplugins.api.GraphBuilderPluginId;
import graphbuilderplugins.typescriptsources.analyzer.TypeScriptDependencyAnalyzer;
import graphbuilderplugins.typescriptsources.model.TypeScriptDependencyGraph;
import graphbuilderplugins.typescriptsources.model.TypeScriptSourceModuleNode;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class TypeScriptSourcesGraphBuilderPlugin implements GraphBuilderPlugin {
    private final TypeScriptDependencyAnalyzer analyzer;

    public TypeScriptSourcesGraphBuilderPlugin() {
        this(new TypeScriptDependencyAnalyzer());
    }

    TypeScriptSourcesGraphBuilderPlugin(TypeScriptDependencyAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public GraphBuilderPluginId id() {
        return GraphBuilderPluginId.TYPESCRIPT_SOURCES;
    }

    @Override
    public void build(GraphBuildTarget target) {
        throw new IllegalArgumentException("TYPESCRIPT_SOURCES requires non-empty inputFolders");
    }

    @Override
    public void build(GraphBuildTarget target, List<String> inputFolders) {
        if (inputFolders == null || inputFolders.isEmpty()) {
            throw new IllegalArgumentException("TYPESCRIPT_SOURCES requires non-empty inputFolders");
        }

        TypeScriptDependencyGraph graph = analyzer.analyze(inputFolders);
        List<TypeScriptSourceModuleNode> sortedNodes = new ArrayList<>(graph.nodes());
        sortedNodes.sort(Comparator.comparing(TypeScriptSourceModuleNode::modulePath));

        Set<String> goodNodes = new LinkedHashSet<>();
        Set<String> badNodes = new LinkedHashSet<>();
        for (TypeScriptSourceModuleNode node : sortedNodes) {
            if (node.modulePath() == null || node.modulePath().isBlank()) {
                continue;
            }
            if (node.bad()) {
                badNodes.add(node.modulePath());
            } else {
                goodNodes.add(node.modulePath());
            }
        }

        List<TypeScriptDependencyGraph.DependencyEdge> sortedEdges = new ArrayList<>(graph.edges());
        sortedEdges.sort(Comparator
                .comparing(TypeScriptDependencyGraph.DependencyEdge::source)
                .thenComparing(TypeScriptDependencyGraph.DependencyEdge::target));

        for (String node : goodNodes) {
            target.addNode(node);
        }
        for (String node : badNodes) {
            target.addBadNode(node);
        }
        for (TypeScriptDependencyGraph.DependencyEdge edge : sortedEdges) {
            if (edge.source() == null || edge.source().isBlank() || edge.target() == null || edge.target().isBlank()) {
                continue;
            }
            target.addDependency(edge.source(), edge.target());
        }

        target.saveCache("cache.txt");
        System.out.println("Cache graph written to cache.txt");
    }

    public TypeScriptDependencyGraph analyzePaths(List<Path> inputFolders) {
        return analyzer.analyzePaths(inputFolders);
    }
}
