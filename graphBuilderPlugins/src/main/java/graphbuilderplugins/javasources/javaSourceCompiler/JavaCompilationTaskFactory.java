package graphbuilderplugins.javasources.javaSourceCompiler;

import com.sun.source.util.JavacTask;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.tools.JavaFileObject;

public final class JavaCompilationTaskFactory {
    private static final List<String> COMPILER_OPTIONS = List.of("-proc:none");

    public JavaCompilationTask create(JavaCompilerSession session, List<Path> sourceFiles) {
        Objects.requireNonNull(session, "session must not be null");
        Objects.requireNonNull(sourceFiles, "sourceFiles must not be null");

        List<Path> normalizedSourceFiles = sourceFiles.stream()
                .filter(Objects::nonNull)
                .map(Path::normalize)
                .toList();

        Iterable<? extends JavaFileObject> javaFileObjectsIterable =
                session.fileManager().getJavaFileObjectsFromPaths(normalizedSourceFiles);

        List<JavaFileObject> javaFileObjects = new ArrayList<>();
        for (JavaFileObject javaFileObject : javaFileObjectsIterable) {
            javaFileObjects.add(javaFileObject);
        }

        JavacTask javacTask = (JavacTask) session.compiler().getTask(
                null,
                session.fileManager(),
                null,
                COMPILER_OPTIONS,
                null,
                javaFileObjects
        );

        return new JavaCompilationTask(javacTask, javaFileObjects);
    }
}
