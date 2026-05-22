package graphbuilderplugins.javasources.javaSourceCompiler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public final class JavaCompilerSession implements AutoCloseable {
    private final JavaCompiler compiler;
    private final StandardJavaFileManager fileManager;
    private final DiagnosticCollector<JavaFileObject> diagnostics;

    JavaCompilerSession(
            JavaCompiler compiler,
            StandardJavaFileManager fileManager,
            DiagnosticCollector<JavaFileObject> diagnostics) {
        this.compiler = Objects.requireNonNull(compiler, "compiler must not be null");
        this.fileManager = Objects.requireNonNull(fileManager, "fileManager must not be null");
        this.diagnostics = Objects.requireNonNull(diagnostics, "diagnostics must not be null");
    }

    public JavaCompiler compiler() {
        return compiler;
    }

    public StandardJavaFileManager fileManager() {
        return fileManager;
    }

    public DiagnosticCollector<JavaFileObject> diagnostics() {
        return diagnostics;
    }

    @Override
    public void close() {
        try {
            fileManager.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to close Java compiler file manager", e);
        }
    }

    public static JavaCompilerSession createDefault() {
        return JavaCompilerSessionFactory.create(Locale.ROOT, StandardCharsets.UTF_8);
    }

    public static JavaCompilerSession create(Locale locale, Charset charset) {
        return JavaCompilerSessionFactory.create(locale, charset);
    }
}
