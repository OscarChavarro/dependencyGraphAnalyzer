package backend.application.service;

import backend.domain.model.GroupDefinitionModel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Reads the ".txt" group definition files of a group folder, sorted by file name. First line is
 * the "[GROUP_NAME]" header, following lines are package names ('#' lines are comments).
 * Files without a valid header are ignored.
 */
@Component
public class GroupDefinitionsReader {
    public List<GroupDefinitionModel> read(String groupFolder) {
        return read(resolveExistingFolder(groupFolder));
    }

    public List<GroupDefinitionModel> read(Path folder) {
        List<Path> files;
        try (Stream<Path> listing = Files.list(folder)) {
            files = listing
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to list groupFolder: '" + folder + "'", e);
        }

        List<GroupDefinitionModel> definitions = new ArrayList<>();
        for (Path file : files) {
            List<String> lines;
            try {
                lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to read group file: '" + file + "'", e);
            }
            if (lines.isEmpty()) {
                continue;
            }
            String header = lines.get(0).trim();
            if (!header.startsWith("[") || !header.endsWith("]") || header.length() < 3) {
                continue;
            }
            List<String> members = lines.subList(1, lines.size()).stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .toList();
            definitions.add(new GroupDefinitionModel(header.substring(1, header.length() - 1), members));
        }
        return definitions;
    }

    private Path resolveExistingFolder(String groupFolder) {
        if (groupFolder == null || groupFolder.isBlank()) {
            throw new IllegalArgumentException("groupFolder is required");
        }
        Path firstTry = Paths.get(groupFolder);
        if (Files.isDirectory(firstTry)) {
            return firstTry.normalize();
        }
        String fallbackInput = groupFolder.replaceFirst("^\\.\\./", "");
        Path secondTry = Paths.get(fallbackInput);
        if (Files.isDirectory(secondTry)) {
            return secondTry.normalize();
        }
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        throw new IllegalArgumentException("groupFolder does not exist or is not a directory. input='"
                + groupFolder + "', tried=[" + cwd.resolve(firstTry).normalize() + ", "
                + cwd.resolve(secondTry).normalize() + "]");
    }
}
