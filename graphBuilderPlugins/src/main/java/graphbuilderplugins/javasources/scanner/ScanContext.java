package graphbuilderplugins.javasources.scanner;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import graphbuilderplugins.javasources.model.DependencyGraph;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class ScanContext {
    private String currentClassFqcn;
    private CompilationUnitTree currentCompilationUnit;
    private DependencyGraph currentGraph;
    private final Trees trees;
    private final Map<Tree, Set<String>> resolvedDependencyCache = new IdentityHashMap<>();

    public ScanContext(Trees trees, DependencyGraph currentGraph) {
        this.trees = Objects.requireNonNull(trees, "trees must not be null");
        this.currentGraph = Objects.requireNonNull(currentGraph, "currentGraph must not be null");
    }

    public Optional<String> currentClassFqcn() {
        return Optional.ofNullable(currentClassFqcn);
    }

    public void setCurrentClassFqcn(String currentClassFqcn) {
        if (currentClassFqcn == null || currentClassFqcn.isBlank()) {
            this.currentClassFqcn = null;
            return;
        }
        this.currentClassFqcn = currentClassFqcn.trim();
    }

    public Optional<CompilationUnitTree> currentCompilationUnit() {
        return Optional.ofNullable(currentCompilationUnit);
    }

    public void setCurrentCompilationUnit(CompilationUnitTree currentCompilationUnit) {
        this.currentCompilationUnit = currentCompilationUnit;
    }

    public DependencyGraph currentGraph() {
        return currentGraph;
    }

    public void setCurrentGraph(DependencyGraph currentGraph) {
        this.currentGraph = Objects.requireNonNull(currentGraph, "currentGraph must not be null");
    }

    public Trees trees() {
        return trees;
    }

    public void addDependencyEdge(String sourceFqcn, String targetFqcn) {
        currentGraph = currentGraph.registerEdge(sourceFqcn, targetFqcn);
    }

    public Optional<Set<String>> cachedDependencies(Tree tree) {
        Set<String> cached = resolvedDependencyCache.get(tree);
        if (cached == null) {
            return Optional.empty();
        }
        return Optional.of(Set.copyOf(cached));
    }

    public void cacheDependencies(Tree tree, Set<String> dependencies) {
        if (tree == null || dependencies == null) {
            return;
        }
        resolvedDependencyCache.put(tree, new LinkedHashSet<>(dependencies));
    }
}
