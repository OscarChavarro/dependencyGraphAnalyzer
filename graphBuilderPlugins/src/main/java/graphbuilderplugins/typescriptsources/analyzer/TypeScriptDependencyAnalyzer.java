package graphbuilderplugins.typescriptsources.analyzer;

import graphbuilderplugins.typescriptsources.model.TypeScriptDependencyGraph;
import graphbuilderplugins.typescriptsources.parser.TypeScriptAnalyzerOutputParser;
import graphbuilderplugins.typescriptsources.runner.TypeScriptAnalyzerRunner;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

public final class TypeScriptDependencyAnalyzer {
    private static final List<String> DEFAULT_EXCLUDED_FOLDERS = List.of("node_modules", "dist", "build", ".git");
    private static final List<String> DEFAULT_EXCLUDED_SUFFIXES = List.of(".d.ts");
    private static final List<String> DEFAULT_INCLUDED_EXTENSIONS = List.of(".ts", ".tsx", ".mts", ".cts");

    private final TypeScriptAnalyzerRunner runner;
    private final TypeScriptAnalyzerOutputParser parser;

    public TypeScriptDependencyAnalyzer() {
        this(new TypeScriptAnalyzerRunner(), new TypeScriptAnalyzerOutputParser());
    }

    TypeScriptDependencyAnalyzer(TypeScriptAnalyzerRunner runner, TypeScriptAnalyzerOutputParser parser) {
        this.runner = runner;
        this.parser = parser;
    }

    public TypeScriptDependencyGraph analyze(List<String> inputFolders) {
        List<Path> normalizedFolders = inputFolders == null
                ? List.of()
                : inputFolders.stream()
                        .filter(value -> value != null && !value.isBlank())
                        .map(String::trim)
                        .map(Path::of)
                        .map(Path::normalize)
                        .toList();
        return analyzePaths(normalizedFolders);
    }

    public TypeScriptDependencyGraph analyzePaths(List<Path> inputFolders) {
        if (inputFolders == null || inputFolders.isEmpty()) {
            return TypeScriptDependencyGraph.empty();
        }

        String rawJson = runner.run(
                inputFolders,
                DEFAULT_EXCLUDED_FOLDERS,
                DEFAULT_EXCLUDED_SUFFIXES,
                DEFAULT_INCLUDED_EXTENSIONS);
        TypeScriptDependencyGraph parsedGraph = parser.parse(rawJson);
        return normalize(parsedGraph, inputFolders);
    }

    private TypeScriptDependencyGraph normalize(TypeScriptDependencyGraph graph, List<Path> inputFolders) {
        LinkedHashSet<String> normalizedNodes = new LinkedHashSet<>();
        LinkedHashSet<String> normalizedBadNodes = new LinkedHashSet<>();
        LinkedHashSet<TypeScriptDependencyGraph.DependencyEdge> normalizedEdges = new LinkedHashSet<>();

        for (var node : graph.nodes()) {
            String normalizedPath = normalizeModulePath(node.modulePath(), inputFolders);
            if (node.bad()) {
                normalizedBadNodes.add(normalizedPath);
            } else {
                normalizedNodes.add(normalizedPath);
            }
        }
        for (var edge : graph.edges()) {
            String source = normalizeModulePath(edge.source(), inputFolders);
            String target = normalizeModulePath(edge.target(), inputFolders);
            normalizedEdges.add(new TypeScriptDependencyGraph.DependencyEdge(source, target));
            normalizedNodes.add(source);
            normalizedNodes.add(target);
        }

        List<String> sortedNodes = normalizedNodes.stream().sorted().toList();
        List<String> sortedBadNodes = normalizedBadNodes.stream().sorted().toList();
        List<TypeScriptDependencyGraph.DependencyEdge> sortedEdges = new ArrayList<>(normalizedEdges);
        sortedEdges.sort(Comparator
                .comparing(TypeScriptDependencyGraph.DependencyEdge::source)
                .thenComparing(TypeScriptDependencyGraph.DependencyEdge::target));

        return TypeScriptDependencyGraph.fromLists(sortedNodes, sortedEdges, sortedBadNodes);
    }

    private String normalizeModulePath(String value, List<Path> inputFolders) {
        if (value == null || value.isBlank()) {
            return value;
        }

        String cleaned = value.trim().replace('\\', '/');
        Path asPath;
        try {
            asPath = Path.of(cleaned).normalize();
        } catch (Exception ex) {
            return cleaned;
        }

        Path bestRoot = null;
        int bestDepth = -1;
        for (Path root : inputFolders) {
            if (asPath.startsWith(root) && root.getNameCount() > bestDepth) {
                bestRoot = root;
                bestDepth = root.getNameCount();
            }
        }

        if (bestRoot != null) {
            return bestRoot.relativize(asPath).toString().replace('\\', '/');
        }

        return cleaned;
    }
}
