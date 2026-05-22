package graphbuilderplugins.javasources.javaSourceCompiler;

import com.sun.source.tree.CompilationUnitTree;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JavaAstParser {
    public ParsedCompilationUnits parse(JavaCompilationTask task) {
        Objects.requireNonNull(task, "task must not be null");

        List<CompilationUnitTree> units = new ArrayList<>();
        try {
            Iterable<? extends CompilationUnitTree> parsed = task.javacTask().parse();
            for (CompilationUnitTree unit : parsed) {
                units.add(unit);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse Java compilation units", e);
        }

        return new ParsedCompilationUnits(task.javacTask(), units);
    }
}
