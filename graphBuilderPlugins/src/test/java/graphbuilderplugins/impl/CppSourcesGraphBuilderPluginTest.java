package graphbuilderplugins.impl;

import graphbuilderplugins.api.GraphBuildTarget;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CppSourcesGraphBuilderPluginTest {

    @Test
    void shouldParseIncludesFromTxxFiles() throws IOException {
        Path root = Files.createTempDirectory("cpp-plugin-txx-");
        Path src = root.resolve("src");
        Files.createDirectories(src);

        write(src.resolve("Main.cpp"), "#include \"math/Add.txx\"\n");
        write(src.resolve("math/Add.txx"), "#include \"Core.h\"\n");
        write(src.resolve("Core.h"), "// header\n");

        TestGraphBuildTarget target = runPlugin(List.of(src.toString()));

        assertTrue(target.goodNodes.contains("Main.cpp"));
        assertTrue(target.goodNodes.contains("math/Add.txx"));
        assertTrue(target.goodNodes.contains("Core.h"));
        assertTrue(target.edges.contains("Main.cpp->math/Add.txx"));
        assertTrue(target.edges.contains("math/Add.txx->Core.h"));
    }

    private TestGraphBuildTarget runPlugin(List<String> inputFolders) {
        CppSourcesGraphBuilderPlugin plugin = new CppSourcesGraphBuilderPlugin();
        TestGraphBuildTarget target = new TestGraphBuildTarget();
        plugin.build(target, inputFolders);
        return target;
    }

    private void write(Path file, String content) throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
    }

    private static final class TestGraphBuildTarget implements GraphBuildTarget {
        private final Set<String> goodNodes = new LinkedHashSet<>();
        private final Set<String> edges = new LinkedHashSet<>();

        @Override
        public void addNode(String name) {
            goodNodes.add(name);
        }

        @Override
        public void addBadNode(String name) {
        }

        @Override
        public void addDependency(String fromNodeName, String toNodeName) {
            edges.add(fromNodeName + "->" + toNodeName);
        }

        @Override
        public List<String> listNodeNames() {
            return new ArrayList<>(goodNodes);
        }
    }
}
