package graphbuilderplugins.javasources.javaSourceCompiler;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Objects;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public final class JavaCompilerSessionFactory {
    private JavaCompilerSessionFactory() {
    }

    public static JavaCompilerSession create(Locale locale, Charset charset) {
        Locale normalizedLocale = Objects.requireNonNull(locale, "locale must not be null");
        Charset normalizedCharset = Objects.requireNonNull(charset, "charset must not be null");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException(
                    "System JavaCompiler is not available. Run with a JDK (not a JRE)."
            );
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, normalizedLocale, normalizedCharset);
        return new JavaCompilerSession(compiler, fileManager);
    }
}
