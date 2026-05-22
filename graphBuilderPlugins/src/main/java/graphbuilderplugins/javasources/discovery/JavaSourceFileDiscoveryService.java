package graphbuilderplugins.javasources.discovery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class JavaSourceFileDiscoveryService {
    private static final Set<String> IGNORED_DIRECTORY_NAMES = Set.of(
            "build",
            "target",
            "out",
            "bin"
    );

    public List<Path> discover(List<Path> sourceFolders) {
        if (sourceFolders == null || sourceFolders.isEmpty()) {
            return List.of();
        }

        LinkedHashSet<Path> result = new LinkedHashSet<>();
        for (Path folder : sourceFolders) {
            if (folder == null) {
                continue;
            }

            Path normalizedFolder = folder.normalize();
            if (!Files.isDirectory(normalizedFolder)) {
                throw new IllegalArgumentException("Source folder does not exist: " + normalizedFolder);
            }

            try (var stream = Files.walk(normalizedFolder)) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .filter(path -> !isInsideIgnoredDirectory(normalizedFolder.relativize(path)))
                        .map(Path::normalize)
                        .forEach(result::add);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to scan source folder: " + normalizedFolder, e);
            }
        }

        List<Path> sorted = new ArrayList<>(result);
        sorted.sort(Comparator.comparing(path -> path.toString().replace('\\', '/')));
        return List.copyOf(sorted);
    }

    private boolean isInsideIgnoredDirectory(Path relativePath) {
        for (Path segment : relativePath) {
            String name = Objects.toString(segment, "").trim();
            if (name.isEmpty()) {
                continue;
            }
            String lower = name.toLowerCase(Locale.ROOT);
            if (lower.startsWith(".")) {
                return true;
            }
            if (IGNORED_DIRECTORY_NAMES.contains(lower)) {
                return true;
            }
        }
        return false;
    }
}
