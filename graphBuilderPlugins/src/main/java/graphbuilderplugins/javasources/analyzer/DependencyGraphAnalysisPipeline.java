package graphbuilderplugins.javasources.analyzer;

import com.sun.source.tree.CompilationUnitTree;
import graphbuilderplugins.javasources.javaSourceCompiler.JavaAstParser;
import graphbuilderplugins.javasources.javaSourceCompiler.JavaCompilationTask;
import graphbuilderplugins.javasources.javaSourceCompiler.JavaCompilationTaskFactory;
import graphbuilderplugins.javasources.javaSourceCompiler.JavaCompilerSession;
import graphbuilderplugins.javasources.javaSourceCompiler.JavaSemanticAnalyzer;
import graphbuilderplugins.javasources.javaSourceCompiler.ParsedCompilationUnits;
import graphbuilderplugins.javasources.javaSourceCompiler.SemanticallyAnalyzedUnits;
import graphbuilderplugins.javasources.discovery.JavaSourceFileDiscoveryService;
import graphbuilderplugins.javasources.model.DependencyGraph;
import graphbuilderplugins.javasources.scanner.ClassDependencyScanner;
import graphbuilderplugins.javasources.scanner.ScanContext;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class DependencyGraphAnalysisPipeline {
    private final JavaSourceFileDiscoveryService discoveryService;
    private final JavaCompilationTaskFactory compilationTaskFactory;
    private final JavaAstParser astParser;
    private final JavaSemanticAnalyzer semanticAnalyzer;

    public DependencyGraphAnalysisPipeline(
            JavaSourceFileDiscoveryService discoveryService,
            JavaCompilationTaskFactory compilationTaskFactory,
            JavaAstParser astParser,
            JavaSemanticAnalyzer semanticAnalyzer
    ) {
        this.discoveryService = Objects.requireNonNull(discoveryService, "discoveryService must not be null");
        this.compilationTaskFactory = Objects.requireNonNull(compilationTaskFactory, "compilationTaskFactory must not be null");
        this.astParser = Objects.requireNonNull(astParser, "astParser must not be null");
        this.semanticAnalyzer = Objects.requireNonNull(semanticAnalyzer, "semanticAnalyzer must not be null");
    }

    public DependencyGraph run(DependencyGraphAnalysisRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        List<Path> sourceFolders = request.sourceFolders();

        // 1) Discover source files
        List<Path> sourceFiles = discoveryService.discover(sourceFolders);
        if (sourceFiles.isEmpty()) {
            return DependencyGraph.empty();
        }

        // 2) Initialize compiler infrastructure
        try (JavaCompilerSession session = JavaCompilerSession.createDefault()) {
            // 3) Build compilation task
            JavaCompilationTask task = compilationTaskFactory.create(session, sourceFiles, request.classpathEntries());
            // 4) Parse AST
            ParsedCompilationUnits parsed = astParser.parse(task);
            // 5) Run semantic analysis
            SemanticallyAnalyzedUnits analyzed = semanticAnalyzer.analyze(parsed);
            // 6) Scan compilation units and build graph incrementally
            DependencyGraph graph = DependencyGraph.empty();
            ScanContext context = new ScanContext(analyzed.trees(), graph);
            ClassDependencyScanner scanner = new ClassDependencyScanner();
            for (CompilationUnitTree unit : analyzed.units()) {
                scanner.scanCompilationUnit(unit, context);
            }
            // 7) Export-ready graph result
            return context.currentGraph();
        }
    }
}
