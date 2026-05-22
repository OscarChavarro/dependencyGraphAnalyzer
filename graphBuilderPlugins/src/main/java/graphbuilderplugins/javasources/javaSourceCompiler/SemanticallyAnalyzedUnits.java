package graphbuilderplugins.javasources.javaSourceCompiler;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;
import java.util.List;
import java.util.Objects;

public final class SemanticallyAnalyzedUnits {
    private final JavacTask javacTask;
    private final Trees trees;
    private final List<CompilationUnitTree> units;

    public SemanticallyAnalyzedUnits(JavacTask javacTask, Trees trees, List<CompilationUnitTree> units) {
        this.javacTask = Objects.requireNonNull(javacTask, "javacTask must not be null");
        this.trees = Objects.requireNonNull(trees, "trees must not be null");
        this.units = List.copyOf(Objects.requireNonNull(units, "units must not be null"));
    }

    public JavacTask javacTask() {
        return javacTask;
    }

    public Trees trees() {
        return trees;
    }

    public List<CompilationUnitTree> units() {
        return units;
    }
}
