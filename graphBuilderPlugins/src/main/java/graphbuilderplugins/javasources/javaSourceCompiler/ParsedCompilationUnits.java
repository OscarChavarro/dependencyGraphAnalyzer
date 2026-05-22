package graphbuilderplugins.javasources.javaSourceCompiler;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import java.util.List;
import java.util.Objects;

public final class ParsedCompilationUnits {
    private final JavacTask javacTask;
    private final List<CompilationUnitTree> units;

    public ParsedCompilationUnits(JavacTask javacTask, List<CompilationUnitTree> units) {
        this.javacTask = Objects.requireNonNull(javacTask, "javacTask must not be null");
        this.units = List.copyOf(Objects.requireNonNull(units, "units must not be null"));
    }

    public JavacTask javacTask() {
        return javacTask;
    }

    public List<CompilationUnitTree> units() {
        return units;
    }
}
