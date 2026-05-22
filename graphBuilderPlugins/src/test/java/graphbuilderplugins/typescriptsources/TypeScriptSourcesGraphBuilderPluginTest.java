package graphbuilderplugins.typescriptsources;

import graphbuilderplugins.api.GraphBuildTarget;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeScriptSourcesGraphBuilderPluginTest {

    @Test
    void shouldBuildDependencyFromRelativeImport() throws IOException {
        Path root = Files.createTempDirectory("ts-plugin-rel-");
        Path src = root.resolve("src");
        Files.createDirectories(src);

        write(src.resolve("a.ts"), "import { b } from './b';\nexport const a = b;\n");
        write(src.resolve("b.ts"), "export const b = 1;\n");

        TestGraphBuildTarget target = runPlugin(List.of(src.toString()));

        assertTrue(target.goodNodes.contains("a.ts"));
        assertTrue(target.goodNodes.contains("b.ts"));
        assertTrue(target.edges.contains("a.ts->b.ts"));
    }

    @Test
    void shouldBuildDependencyFromExportFrom() throws IOException {
        Path root = Files.createTempDirectory("ts-plugin-export-");
        Path src = root.resolve("src");
        Files.createDirectories(src);

        write(src.resolve("index.ts"), "export { v } from './value';\n");
        write(src.resolve("value.ts"), "export const v = 42;\n");

        TestGraphBuildTarget target = runPlugin(List.of(src.toString()));

        assertTrue(target.goodNodes.contains("index.ts"));
        assertTrue(target.goodNodes.contains("value.ts"));
        assertTrue(target.edges.contains("index.ts->value.ts"));
    }

    @Test
    void shouldResolveAliasFromTsconfigPaths() throws IOException {
        Path root = Files.createTempDirectory("ts-plugin-alias-");
        Path src = root.resolve("src");
        Path utils = src.resolve("utils");
        Files.createDirectories(utils);

        write(root.resolve("tsconfig.json"), """
                {
                  "compilerOptions": {
                    "baseUrl": ".",
                    "paths": {
                      "@app/*": ["src/*"]
                    }
                  }
                }
                """);
        write(src.resolve("entry.ts"), "import { helper } from '@app/utils/helper';\nexport const e = helper;\n");
        write(utils.resolve("helper.ts"), "export const helper = 1;\n");

        TestGraphBuildTarget target = runPlugin(List.of(root.toString()));

        assertTrue(target.goodNodes.contains("src/entry.ts"));
        assertTrue(target.goodNodes.contains("src/utils/helper.ts"));
        assertTrue(target.edges.contains("src/entry.ts->src/utils/helper.ts"));
    }

    @Test
    void shouldCreateExternalPackageNodeWhenImportCannotResolveLocally() throws IOException {
        Path root = Files.createTempDirectory("ts-plugin-external-");
        Path src = root.resolve("src");
        Files.createDirectories(src);

        write(src.resolve("app.ts"), "import React from 'react';\nexport const x = React;\n");

        TestGraphBuildTarget target = runPlugin(List.of(src.toString()));

        assertTrue(target.goodNodes.contains("app.ts"));
        assertTrue(target.goodNodes.contains("react"));
        assertTrue(target.edges.contains("app.ts->react"));
    }

    @Test
    void shouldReportBadNodeForUnresolvedRelativeImportAndContinue() throws IOException {
        Path root = Files.createTempDirectory("ts-plugin-bad-");
        Path src = root.resolve("src");
        Files.createDirectories(src);

        write(src.resolve("a.ts"), "import { missing } from './missing';\nexport const a = missing;\n");

        TestGraphBuildTarget target = runPlugin(List.of(src.toString()));

        assertTrue(target.goodNodes.contains("a.ts"));
        assertTrue(target.badNodes.contains("./missing"));
        assertEquals(0, target.edges.size());
    }

    private TestGraphBuildTarget runPlugin(List<String> inputFolders) {
        TypeScriptSourcesGraphBuilderPlugin plugin = new TypeScriptSourcesGraphBuilderPlugin();
        TestGraphBuildTarget target = new TestGraphBuildTarget();
        plugin.build(target, inputFolders);
        return target;
    }

    private void write(Path file, String content) throws IOException {
        Files.writeString(file, content);
    }

    private static final class TestGraphBuildTarget implements GraphBuildTarget {
        private final Set<String> goodNodes = new LinkedHashSet<>();
        private final Set<String> badNodes = new LinkedHashSet<>();
        private final Set<String> edges = new LinkedHashSet<>();

        @Override
        public void addNode(String name) {
            goodNodes.add(name);
        }

        @Override
        public void addBadNode(String name) {
            badNodes.add(name);
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
