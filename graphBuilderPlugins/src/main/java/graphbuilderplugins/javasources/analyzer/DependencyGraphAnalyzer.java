package graphbuilderplugins.javasources.analyzer;

import graphbuilderplugins.javasources.javaSourceCompiler.JavaAstParser;
import graphbuilderplugins.javasources.javaSourceCompiler.JavaCompilationTaskFactory;
import graphbuilderplugins.javasources.javaSourceCompiler.JavaSemanticAnalyzer;
import graphbuilderplugins.javasources.discovery.JavaSourceFileDiscoveryService;
import graphbuilderplugins.javasources.model.DependencyGraph;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class DependencyGraphAnalyzer {
    private final DependencyGraphAnalysisPipeline pipeline;

    public DependencyGraphAnalyzer() {
        this(new DependencyGraphAnalysisPipeline(
                new JavaSourceFileDiscoveryService(),
                new JavaCompilationTaskFactory(),
                new JavaAstParser(),
                new JavaSemanticAnalyzer()
        ));
    }

    public DependencyGraphAnalyzer(DependencyGraphAnalysisPipeline pipeline) {
        this.pipeline = Objects.requireNonNull(pipeline, "pipeline must not be null");
    }

    public DependencyGraph analyze(List<Path> sourceFolders) {
        return analyze(sourceFolders, List.of());
    }

    public DependencyGraph analyze(List<Path> sourceFolders, List<Path> classpathEntries) {
        return pipeline.run(DependencyGraphAnalysisRequest.of(sourceFolders, classpathEntries));
    }
}
