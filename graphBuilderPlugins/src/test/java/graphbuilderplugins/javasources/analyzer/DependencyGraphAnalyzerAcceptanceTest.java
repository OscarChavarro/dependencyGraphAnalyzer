package graphbuilderplugins.javasources.analyzer;

import graphbuilderplugins.javasources.export.AdjacencyListDependencyGraphExporter;
import graphbuilderplugins.javasources.model.DependencyGraph;
import graphbuilderplugins.javasources.model.SourceClassNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DependencyGraphAnalyzerAcceptanceTest {
    @Test
    void shouldMeetFinalAcceptanceCriteria() throws IOException {
        Path root = Files.createTempDirectory("java-graph-acceptance-");
        Path pkg = root.resolve("acceptance");
        Files.createDirectories(pkg);

        write(pkg.resolve("Api.java"), """
                package acceptance;
                public interface Api {}
                """);

        write(pkg.resolve("Base.java"), """
                package acceptance;
                public class Base {}
                """);

        write(pkg.resolve("Model.java"), """
                package acceptance;
                public record Model(String id) {}
                """);

        write(pkg.resolve("MainService.java"), """
                package acceptance;

                import java.util.List;

                public class MainService extends Base implements Api {
                    private List<Model> models;

                    public void run() {
                        List<Model> a = models;
                        List<Model> b = models;
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                            }
                        };
                    }
                }
                """);

        // Unresolved symbol must not crash analysis.
        write(pkg.resolve("Broken.java"), """
                package acceptance;
                public class Broken {
                    private MissingType missing;
                }
                """);

        DependencyGraphAnalyzer analyzer = new DependencyGraphAnalyzer();
        DependencyGraph graph = analyzer.analyze(List.of(root));

        // All discovered source classes are present as nodes (excluding anonymous classes).
        assertTrue(graph.nodes().containsKey("acceptance.Api"));
        assertTrue(graph.nodes().containsKey("acceptance.Base"));
        assertTrue(graph.nodes().containsKey("acceptance.Model"));
        assertTrue(graph.nodes().containsKey("acceptance.MainService"));
        assertTrue(graph.nodes().containsKey("acceptance.Broken"));

        // Anonymous class is ignored as graph node.
        assertFalse(graph.nodes().keySet().stream().anyMatch(name -> name.contains("$")));

        // FQCN policy: every node uses canonical package-qualified name.
        assertTrue(
                graph.nodes().keySet().stream().allMatch(name -> name.contains(".")),
                "Non-FQCN nodes: " + graph.nodes().keySet()
        );

        SourceClassNode main = graph.nodes().get("acceptance.MainService");
        assertNotNull(main);

        // Outgoing dependencies are semantically resolved.
        assertTrue(main.outgoingDependencies().contains("acceptance.Base"));
        assertTrue(main.outgoingDependencies().contains("acceptance.Api"));
        assertTrue(main.outgoingDependencies().contains("java.util.List"));
        assertTrue(main.outgoingDependencies().contains("acceptance.Model"));

        // Duplicates are eliminated.
        Set<String> uniqueDependencies = main.outgoingDependencies().stream().collect(Collectors.toSet());
        assertTrue(uniqueDependencies.size() == main.outgoingDependencies().size());

        // Export works.
        String exported = new AdjacencyListDependencyGraphExporter().export(graph);
        assertFalse(exported.isBlank());
        assertTrue(exported.contains("acceptance.MainService"));

        // Unresolved symbols do not crash execution and analysis still returns data.
        assertFalse(graph.nodes().isEmpty());
    }

    private void write(Path file, String content) throws IOException {
        Files.writeString(file, content);
    }
}
