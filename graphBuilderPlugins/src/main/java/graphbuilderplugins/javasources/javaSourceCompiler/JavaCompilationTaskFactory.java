package graphbuilderplugins.javasources.javaSourceCompiler;

import com.sun.source.util.JavacTask;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public final class JavaCompilationTaskFactory {
    private static final List<String> COMPILER_OPTIONS = List.of(
            "-proc:none",
            "-implicit:none",
            "-Xlint:none",
            "-nowarn"
    );

    public JavaCompilationTask create(JavaCompilerSession session, List<Path> sourceFiles, List<Path> classpathEntries) {
        Objects.requireNonNull(session, "session must not be null");
        Objects.requireNonNull(sourceFiles, "sourceFiles must not be null");
        Objects.requireNonNull(classpathEntries, "classpathEntries must not be null");

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

        List<Path> normalizedClasspathEntries = classpathEntries.stream()
                .filter(Objects::nonNull)
                .map(Path::normalize)
                .toList();
        if (!normalizedClasspathEntries.isEmpty()) {
            try {
                session.fileManager().setLocationFromPaths(StandardLocation.CLASS_PATH, normalizedClasspathEntries);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to configure Java analysis classpath", e);
            }
        }

        JavacTask javacTask = (JavacTask) session.compiler().getTask(
                null,
                session.fileManager(),
                session.diagnostics(),
                COMPILER_OPTIONS,
                null,
                javaFileObjects
        );

        return new JavaCompilationTask(javacTask, javaFileObjects);
    }
}
