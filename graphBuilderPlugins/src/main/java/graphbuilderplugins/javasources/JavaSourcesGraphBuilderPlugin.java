package graphbuilderplugins.javasources;

import graphbuilderplugins.javasources.analyzer.DependencyGraphAnalyzer;
import graphbuilderplugins.api.GraphBuildTarget;
import graphbuilderplugins.api.GraphBuilderPlugin;
import graphbuilderplugins.api.GraphBuilderPluginId;
import graphbuilderplugins.javasources.model.DependencyGraph;
import graphbuilderplugins.javasources.model.SourceClassNode;
import java.nio.file.Path;
import java.util.List;

public final class JavaSourcesGraphBuilderPlugin implements GraphBuilderPlugin {
    private final DependencyGraphAnalyzer analyzer;

    public JavaSourcesGraphBuilderPlugin() {
        this(new DependencyGraphAnalyzer());
    }

    JavaSourcesGraphBuilderPlugin(DependencyGraphAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public GraphBuilderPluginId id() {
        return GraphBuilderPluginId.JAVA_SOURCES;
    }

    @Override
    public void build(GraphBuildTarget target) {
        throw new IllegalArgumentException("JAVA_SOURCES requires non-empty inputFolders");
    }

    @Override
    public void build(GraphBuildTarget target, List<String> inputFolders) {
        if (inputFolders == null || inputFolders.isEmpty()) {
            throw new IllegalArgumentException("JAVA_SOURCES requires non-empty inputFolders");
        }

        List<Path> sourceFolders = inputFolders.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .map(Path::of)
                .map(Path::normalize)
                .toList();

        if (sourceFolders.isEmpty()) {
            throw new IllegalArgumentException("JAVA_SOURCES requires non-empty inputFolders");
        }

        DependencyGraph graph = analyzer.analyze(sourceFolders);
        for (SourceClassNode node : graph.nodes().values()) {
            target.addNode(node.fqcn());
        }
        for (SourceClassNode node : graph.nodes().values()) {
            for (String dependency : node.outgoingDependencies()) {
                target.addDependency(node.fqcn(), dependency);
            }
        }
    }
}
