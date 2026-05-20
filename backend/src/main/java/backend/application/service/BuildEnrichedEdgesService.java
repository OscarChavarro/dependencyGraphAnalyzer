package backend.application.service;

import backend.application.port.in.BuildEnrichedEdgesUseCase;
import backend.domain.model.GraphModelEdge;
import backend.domain.model.GraphModelGenerator;
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
    public List<GraphModelEdge> execute(GraphModelGenerator generator, String groupsDefinitionFolder) {
        String[] groupsDefinitionFiles = resolveGroupDefinitionFiles(groupsDefinitionFolder);
        return buildEnrichedEdgesFromCache(groupsDefinitionFiles);
    }

    private List<GraphModelEdge> buildEnrichedEdgesFromCache(String[] groupsDefinitionFiles) {
        Path cachePath = resolveExistingFile("cache.txt", "../cache.txt");
        if (cachePath == null) {
            return List.of();
        }

        Map<String, Set<String>> packageGroups = readPackageGroups(groupsDefinitionFiles);
        if (packageGroups.isEmpty()) {
            return List.of();
        }

        Set<GraphModelEdge> enriched = new LinkedHashSet<>();
        try {
            for (String line : Files.readAllLines(cachePath)) {
                String trimmed = line.trim();
                if (!trimmed.startsWith("r ")) {
                    continue;
                }
                String[] parts = trimmed.split("\\s+");
                if (parts.length < 3) {
                    continue;
                }
                String sourcePackage = parts[1];
                String targetPackage = parts[2];

                Set<String> sourceGroups = packageGroups.get(sourcePackage);
                Set<String> targetGroups = packageGroups.get(targetPackage);
                if (sourceGroups == null || targetGroups == null) {
                    continue;
                }

                for (String sourceGroup : sourceGroups) {
                    for (String targetGroup : targetGroups) {
                        enriched.add(new GraphModelEdge(
                                sourceGroup + "." + sourcePackage,
                                targetGroup + "." + targetPackage));
                    }
                }
            }
        } catch (IOException e) {
            return List.of();
        }
        return new ArrayList<>(enriched);
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
        List<Path> candidates = List.of(
                Paths.get(groupsDefinitionFolder).normalize(),
                Paths.get(".").resolve(groupsDefinitionFolder).normalize(),
                Paths.get("backend").resolve(groupsDefinitionFolder).normalize(),
                Paths.get(groupsDefinitionFolder.replaceFirst("^\\.\\./", "")).normalize());

        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("groupsDefinitionFolder does not exist or is not a directory: " + groupsDefinitionFolder);
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
