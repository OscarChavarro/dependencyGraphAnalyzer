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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaDependencyGraphValidationTest {
    @Test
    void shouldBuildExpectedGraphForSyntheticProject() throws IOException {
        Path root = Files.createTempDirectory("java-graph-test-");
        Path pkg = root.resolve("sample");
        Files.createDirectories(pkg);

        write(pkg.resolve("Marker.java"), """
                package sample;
                public @interface Marker {}
                """);
        write(pkg.resolve("Repo.java"), """
                package sample;
                public interface Repo {}
                """);
        write(pkg.resolve("Base.java"), """
                package sample;
                public class Base {}
                """);
        write(pkg.resolve("User.java"), """
                package sample;
                public record User(String name) {}
                """);
        write(pkg.resolve("Role.java"), """
                package sample;
                public enum Role { ADMIN }
                """);
        write(pkg.resolve("Outer.java"), """
                package sample;
                public class Outer {
                    public static class Inner {}
                }
                """);
        write(pkg.resolve("Service.java"), """
                package sample;

                import java.io.IOException;
                import java.util.ArrayList;
                import java.util.HashMap;
                import java.util.List;
                import java.util.Map;
                import java.util.Set;

                public class Service<T extends Repo> extends Base implements Repo {
                    @Marker
                    private List<User> users;
                    private Outer.Inner innerField;

                    public Map<String, List<User>> make(Set<Role> roles) throws IOException {
                        List<User> local = new ArrayList<>();
                        Outer.Inner created = new Outer.Inner();
                        return new HashMap<>();
                    }
                }
                """);

        JavaSourceFileDiscoveryService discoveryService = new JavaSourceFileDiscoveryService();
        List<Path> sourceFiles = discoveryService.discover(List.of(root));

        DependencyGraph graph = DependencyGraph.empty();
        try (JavaCompilerSession session = JavaCompilerSession.createDefault()) {
            JavaCompilationTask task = new JavaCompilationTaskFactory().create(session, sourceFiles, List.of());
            ParsedCompilationUnits parsed = new JavaAstParser().parse(task);
            SemanticallyAnalyzedUnits analyzed = new JavaSemanticAnalyzer().analyze(parsed);

            ScanContext context = new ScanContext(analyzed.trees(), graph);
            ClassDependencyScanner scanner = new ClassDependencyScanner();
            for (CompilationUnitTree unit : analyzed.units()) {
                scanner.scanCompilationUnit(unit, context);
            }
            graph = context.currentGraph();
        }

        Map<String, Set<String>> expectedGraph = Map.ofEntries(
                Map.entry("sample.Service", new LinkedHashSet<>(List.of(
                        "sample.Base",
                        "sample.Repo",
                        "java.util.List",
                        "sample.User",
                        "sample.Marker",
                        "sample.Outer.Inner",
                        "sample.Outer",
                        "java.util.Map",
                        "java.lang.String",
                        "java.util.Set",
                        "sample.Role",
                        "java.io.IOException",
                        "java.util.ArrayList",
                        "java.util.HashMap"
                ))),
                Map.entry("sample.User", Set.of("java.lang.String")),
                Map.entry("sample.Base", Set.of()),
                Map.entry("sample.Repo", Set.of()),
                Map.entry("sample.Outer", Set.of()),
                Map.entry("sample.Outer.Inner", Set.of()),
                Map.entry("sample.Marker", Set.of()),
                Map.entry("sample.Role", Set.of()),
                Map.entry("java.util.List", Set.of()),
                Map.entry("java.util.Map", Set.of()),
                Map.entry("java.util.Set", Set.of()),
                Map.entry("java.util.ArrayList", Set.of()),
                Map.entry("java.util.HashMap", Set.of()),
                Map.entry("java.io.IOException", Set.of()),
                Map.entry("java.lang.String", Set.of())
        );

        Map<String, Set<String>> actualGraph = graph.nodes().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new LinkedHashSet<>(e.getValue().outgoingDependencies())
                ));

        assertEquals(expectedGraph, actualGraph);
    }

    private void write(Path file, String content) throws IOException {
        Files.writeString(file, content);
    }
}
