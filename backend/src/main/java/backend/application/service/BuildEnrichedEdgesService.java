package backend.application.service;

import backend.application.port.in.BuildEnrichedEdgesUseCase;
import backend.domain.model.GraphModelEdge;
import backend.domain.model.GraphModelGenerator;
import graphbuilderplugins.cache.CacheFileContentModel;
import graphbuilderplugins.cache.CacheGraphComposer;
import graphbuilderplugins.cache.CacheRelationEntryModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BuildEnrichedEdgesService implements BuildEnrichedEdgesUseCase {
    @Override
    public List<GraphModelEdge> execute(GraphModelGenerator generator, String groupsDefinitionFolder, String[] inputFolders) {
        String[] groupsDefinitionFiles = resolveGroupDefinitionFiles(groupsDefinitionFolder);
        return buildEnrichedEdgesFromCache(resolveCachePaths(inputFolders), groupsDefinitionFiles);
    }

    private List<GraphModelEdge> buildEnrichedEdgesFromCache(List<Path> cachePaths, String[] groupsDefinitionFiles) {
        if (cachePaths.isEmpty()) {
            return List.of();
        }

        Map<String, Set<String>> packageGroups = readPackageGroups(groupsDefinitionFiles);
        if (packageGroups.isEmpty()) {
            return List.of();
        }

        CacheFileContentModel content;
        try {
            content = CacheGraphComposer.compose(cachePaths.get(0), cachePaths.subList(1, cachePaths.size()));
        } catch (IllegalStateException e) {
            return List.of();
        }

        Set<GraphModelEdge> enriched = new LinkedHashSet<>();
        for (CacheRelationEntryModel relation : content.relations()) {
            Set<String> sourceGroups = packageGroups.get(relation.source());
            Set<String> targetGroups = packageGroups.get(relation.destination());
            if (sourceGroups == null || targetGroups == null) {
                continue;
            }
            for (String sourceGroup : sourceGroups) {
                for (String targetGroup : targetGroups) {
                    enriched.add(new GraphModelEdge(
                            sourceGroup + "." + relation.source(),
                            targetGroup + "." + relation.destination()));
                }
            }
        }
        return new ArrayList<>(enriched);
    }

    /**
     * First path is the base cache; the remaining ones are extra caches.
     * Extra entries that cannot be resolved are skipped.
     */
    private List<Path> resolveCachePaths(String[] inputFolders) {
        Path base = null;
        if (inputFolders != null && inputFolders.length > 0) {
            base = resolveExistingFile(inputFolders[0]);
        }
        if (base == null) {
            base = resolveExistingFile("cache.txt", "../cache.txt");
        }
        if (base == null) {
            return List.of();
        }

        List<Path> paths = new ArrayList<>();
        paths.add(base);
        if (inputFolders != null) {
            for (int i = 1; i < inputFolders.length; i++) {
                if (inputFolders[i] == null || inputFolders[i].isBlank()) {
                    continue;
                }
                Path extra = resolveExistingFile(inputFolders[i]);
                if (extra != null) {
                    paths.add(extra);
                }
            }
        }
        return paths;
    }

    private Map<String, Set<String>> readPackageGroups(String[] groupsDefinitionFiles) {
        Map<String, Set<String>> packageGroups = new HashMap<>();

        for (String groupFile : groupsDefinitionFiles) {
            Path path = Paths.get(groupFile);
            List<String> lines;
            try {
                lines = Files.readAllLines(path);
            } catch (IOException e) {
                continue;
            }
            if (lines.isEmpty()) {
                continue;
            }

            String header = lines.get(0).trim();
            if (!header.startsWith("[") || !header.endsWith("]")) {
                continue;
            }

            for (int i = 1; i < lines.size(); i++) {
                String packageName = lines.get(i).trim();
                if (packageName.isEmpty() || packageName.startsWith("#")) {
                    continue;
                }
                packageGroups
                        .computeIfAbsent(packageName, ignored -> new LinkedHashSet<>())
                        .add(header);
            }
        }
        return packageGroups;
    }

    private String[] resolveGroupDefinitionFiles(String groupsDefinitionFolder) {
        Path folder = resolveExistingFolder(groupsDefinitionFolder);
        try {
            List<String> files = Files.list(folder)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(Path::toString))
                    .map(Path::toString)
                    .toList();
            if (files.isEmpty()) {
                throw new IllegalArgumentException("No .txt files found in groupsDefinitionFolder: " + groupsDefinitionFolder);
            }
            return files.toArray(new String[0]);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to list groupsDefinitionFolder: " + groupsDefinitionFolder, e);
        }
    }

    private Path resolveExistingFolder(String groupsDefinitionFolder) {
        Path cwd = getCwd();
        Path firstTry = Paths.get(groupsDefinitionFolder);
        if (Files.isDirectory(firstTry)) {
            return firstTry.normalize();
        }
        String fallbackInput = groupsDefinitionFolder.replaceFirst("^\\.\\./", "");
        Path secondTry = Paths.get(fallbackInput);
        if (!fallbackInput.equals(groupsDefinitionFolder) && Files.isDirectory(secondTry)) {
            return secondTry.normalize();
        }
        Path firstTryNormalized = firstTry.normalize();
        Path secondTryNormalized = secondTry.normalize();
        throw new IllegalArgumentException("groupsDefinitionFolder does not exist or is not a directory. input='"
                + groupsDefinitionFolder + "', tried=[" + toAbsoluteFromCwd(firstTry, cwd) + ", "
                + toAbsoluteFromCwd(firstTryNormalized, cwd) + ", "
                + toAbsoluteFromCwd(secondTry, cwd) + ", "
                + toAbsoluteFromCwd(secondTryNormalized, cwd) + "]");
    }

    private Path getCwd() {
        return Paths.get("").toAbsolutePath().normalize();
    }

    private Path toAbsoluteFromCwd(Path path, Path cwd) {
        return cwd.resolve(path).normalize();
    }

    private Path resolveExistingFile(String... candidates) {
        for (String candidate : candidates) {
            Path path = Paths.get(candidate).normalize();
            if (Files.isRegularFile(path)) {
                return path;
            }
            Path fromBackend = Paths.get("backend").resolve(candidate).normalize();
            if (Files.isRegularFile(fromBackend)) {
                return fromBackend;
            }
        }
        return null;
    }
}
