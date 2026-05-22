package graphbuilderplugins.javasources.javaSourceCompiler;

import com.sun.source.util.Trees;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import javax.lang.model.element.Element;

public final class JavaSemanticAnalyzer {
    public SemanticallyAnalyzedUnits analyze(ParsedCompilationUnits parsedUnits) {
        Objects.requireNonNull(parsedUnits, "parsedUnits must not be null");

        try {
            Iterable<? extends Element> analyzedElements = parsedUnits.javacTask().analyze();
            for (Element ignored : analyzedElements) {
                // Force full semantic attribution for all compilation units.
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to perform semantic analysis", e);
        }

        Trees trees = Trees.instance(parsedUnits.javacTask());
        return new SemanticallyAnalyzedUnits(parsedUnits.javacTask(), trees, parsedUnits.units());
    }
}
