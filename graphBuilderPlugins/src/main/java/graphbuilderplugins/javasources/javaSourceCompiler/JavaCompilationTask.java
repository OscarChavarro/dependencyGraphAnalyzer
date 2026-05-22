package graphbuilderplugins.javasources.javaSourceCompiler;

import com.sun.source.util.JavacTask;
import java.util.List;
import java.util.Objects;
import javax.tools.JavaFileObject;

public final class JavaCompilationTask {
    private final JavacTask javacTask;
    private final List<JavaFileObject> compilationUnits;

    public JavaCompilationTask(JavacTask javacTask, List<JavaFileObject> compilationUnits) {
        this.javacTask = Objects.requireNonNull(javacTask, "javacTask must not be null");
        this.compilationUnits = List.copyOf(Objects.requireNonNull(compilationUnits, "compilationUnits must not be null"));
    }

    public JavacTask javacTask() {
        return javacTask;
    }

    public List<JavaFileObject> compilationUnits() {
        return compilationUnits;
    }
}
