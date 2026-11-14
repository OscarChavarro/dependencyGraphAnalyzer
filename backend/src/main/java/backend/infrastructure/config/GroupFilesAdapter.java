package backend.infrastructure.config;

import backend.application.port.out.GroupFilesPort;
import backend.domain.model.GroupFileModel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * File-system access to group definition files. A member is the first whitespace-separated token
 * of a line, as the analyzer core reads it; '#' lines and blank lines are ignored.
 */
@Component
public class GroupFilesAdapter implements GroupFilesPort {
    @Override
    public List<GroupFileModel> readAll(Path groupFolder) {
        if (!Files.isDirectory(groupFolder)) {
            throw new IllegalArgumentException("Group folder does not exist: '" + groupFolder + "'");
        }
        List<Path> files;
        try (Stream<Path> listing = Files.list(groupFolder)) {
            files = listing
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to list group folder: '" + groupFolder + "'", e);
        }

        List<GroupFileModel> groups = new ArrayList<>();
        for (Path file : files) {
            List<String> tokens = readTokens(file);
            if (tokens.isEmpty() || !isHeader(tokens.get(0))) {
                continue;
            }
            String header = tokens.get(0);
            groups.add(new GroupFileModel(
                    header.substring(1, header.length() - 1),
                    file,
                    List.copyOf(tokens.subList(1, tokens.size()))));
        }
        return groups;
    }

    @Override
    public void writeMembers(GroupFileModel group, List<String> members) {
        List<String> lines = new ArrayList<>();
        lines.add("[" + group.groupName() + "]");
        lines.addAll(members);
        try {
            Files.write(group.file(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write group file: '" + group.file() + "'", e);
        }
    }

    @Override
    public GroupFileModel create(Path groupFolder, String groupName) {
        Path file = groupFolder.resolve(groupName.toLowerCase(Locale.ROOT) + ".txt");
        String header = groupName.toUpperCase(Locale.ROOT);
        try {
            Files.writeString(
                    file,
                    "[" + header + "]" + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create group file: '" + file + "'", e);
        }
        return new GroupFileModel(header, file, List.of());
    }

    private List<String> readTokens(Path file) {
        List<String> tokens = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                tokens.add(trimmed.split("\\s+")[0]);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read group file: '" + file + "'", e);
        }
        return tokens;
    }

    private boolean isHeader(String token) {
        return token.length() >= 3 && token.startsWith("[") && token.endsWith("]");
    }
}
