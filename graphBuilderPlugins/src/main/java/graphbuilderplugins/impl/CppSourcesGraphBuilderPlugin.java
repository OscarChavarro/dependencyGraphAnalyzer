package graphbuilderplugins.impl;

import graphbuilderplugins.api.GraphBuildTarget;
import graphbuilderplugins.api.GraphBuilderPlugin;
import graphbuilderplugins.api.GraphBuilderPluginId;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CppSourcesGraphBuilderPlugin implements GraphBuilderPlugin {
    private static final String SRC_PREFIX_A = "./src/";
    private static final String SRC_PREFIX_B = "src/";

    @Override
    public GraphBuilderPluginId id() {
        return GraphBuilderPluginId.CPP_SOURCES;
    }

    @Override
    public void build(GraphBuildTarget target) {
        throw new IllegalArgumentException("CPP_SOURCES requires non-empty inputFolders");
    }

    @Override
    public void build(GraphBuildTarget target, List<String> inputFolders) {
        if (inputFolders == null || inputFolders.isEmpty()) {
            throw new IllegalArgumentException("CPP_SOURCES requires non-empty inputFolders");
        }

        List<Path> files = collectSourceFiles(inputFolders);
        List<String> sourceNodes = mapSourceNodes(files, inputFolders);
        Set<String> sourceNodeSet = new LinkedHashSet<>(sourceNodes);
        LinkedHashSet<String> allNodes = new LinkedHashSet<>(sourceNodes);
        List<Relation> relations = new ArrayList<>();

        for (int index = 0; index < files.size(); index++) {
            Path file = files.get(index);
            String nodeName = sourceNodes.get(index);
            parseIncludes(file, nodeName, sourceNodeSet, allNodes, relations);
        }

        for (String node : allNodes) {
            target.addNode(node);
        }
        for (Relation relation : relations) {
            target.addDependency(relation.source(), relation.target());
        }

        target.saveCache("cache.txt");
        System.out.println("Cache graph written to cache.txt");
    }

    private List<Path> collectSourceFiles(List<String> inputFolders) {
        List<Path> files = new ArrayList<>();
        for (String rawFolder : inputFolders) {
            if (rawFolder == null || rawFolder.isBlank()) {
                continue;
            }
            Path folder = Path.of(rawFolder.trim()).normalize();
            if (!Files.isDirectory(folder)) {
                throw new IllegalArgumentException("CPP_SOURCES input folder does not exist: " + folder);
            }
            try (var stream = Files.walk(folder)) {
                stream.filter(Files::isRegularFile)
                        .filter(this::isSourceFile)
                        .forEach(files::add);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to walk folder: " + folder, e);
            }
        }
        files.sort(Comparator.comparing(path -> path.toString().replace('\\', '/')));
        return files;
    }

    private List<String> mapSourceNodes(List<Path> files, List<String> inputFolders) {
        List<Path> roots = inputFolders.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(value -> Path.of(value.trim()).normalize())
                .toList();

        List<String> nodes = new ArrayList<>(files.size());
        for (Path file : files) {
            Path relative = relativizeByBestRoot(file, roots);
            nodes.add(normalizePath(relative.toString().replace('\\', '/')));
        }
        return nodes;
    }

    private Path relativizeByBestRoot(Path file, List<Path> roots) {
        Path bestRoot = null;
        int bestNameCount = -1;
        for (Path root : roots) {
            if (!file.startsWith(root)) {
                continue;
            }
            int currentNameCount = root.getNameCount();
            if (currentNameCount > bestNameCount) {
                bestNameCount = currentNameCount;
                bestRoot = root;
            }
        }
        if (bestRoot == null) {
            return file;
        }
        return bestRoot.relativize(file);
    }

    private void parseIncludes(
            Path file,
            String nodeName,
            Set<String> sourceNodes,
            Set<String> allNodes,
            List<Relation> relations) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String includeTarget = parseIncludeTarget(line);
                if (includeTarget == null) {
                    continue;
                }
                relations.add(new Relation(nodeName, includeTarget));
                if (!sourceNodes.contains(includeTarget)) {
                    allNodes.add(includeTarget);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read source file: " + file, e);
        }
    }

    private String parseIncludeTarget(String line) {
        int includePos = line.indexOf("#include");
        if (includePos < 0) {
            return null;
        }

        int index = includePos + 8;
        while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= line.length()) {
            return null;
        }

        char opener = line.charAt(index);
        if (opener != '<' && opener != '"') {
            return null;
        }

        char closer = opener == '<' ? '>' : '"';
        int end = line.indexOf(closer, index + 1);
        if (end <= index + 1) {
            return null;
        }

        String target = line.substring(index + 1, end).trim();
        return target.isEmpty() ? null : target;
    }

    private boolean isSourceFile(Path path) {
        String normalized = path.toString().replace('\\', '/');
        return normalized.endsWith(".h") || normalized.endsWith(".cpp");
    }

    private String normalizePath(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith(SRC_PREFIX_A)) {
            return trimmed.substring(SRC_PREFIX_A.length());
        }
        if (trimmed.startsWith(SRC_PREFIX_B)) {
            return trimmed.substring(SRC_PREFIX_B.length());
        }
        return trimmed;
    }

    private record Relation(String source, String target) {
    }
}
