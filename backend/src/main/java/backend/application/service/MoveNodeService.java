package backend.application.service;

import backend.application.port.in.MoveNodeUseCase;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class MoveNodeService implements MoveNodeUseCase {
    @Override
    public String execute(String groupFolder, String originGroup, Set<String> originNodes, String destinationGroup) {
        if (originNodes == null || originNodes.isEmpty()) {
            throw new IllegalArgumentException("originNodes is required and must contain at least one node");
        }
        Set<String> sanitizedNodes = sanitizeNodes(originNodes);
        if (sanitizedNodes.isEmpty()) {
            throw new IllegalArgumentException("originNodes is required and must contain at least one non-blank node");
        }

        Path rootFolder = resolveExistingFolder(groupFolder);
        Path originFile = resolveExistingGroupFile(rootFolder, originGroup);
        Path destinationFile = resolveExistingDestinationGroupFile(rootFolder, destinationGroup);

        List<String> originLines = normalizeNonBlankLines(readLines(originFile));
        Set<String> missingNodes = new LinkedHashSet<>(sanitizedNodes);
        List<String> updatedOriginLines = new ArrayList<>(originLines.size());

        for (String line : originLines) {
            if (missingNodes.contains(line)) {
                missingNodes.remove(line);
                continue;
            }
            updatedOriginLines.add(line);
        }

        if (!missingNodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "originNodes not found. nodes='" + missingNodes + "', originFile='" + originFile + "'");
        }

        writeLines(originFile, updatedOriginLines);
        for (String node : sanitizedNodes) {
            appendLine(destinationFile, node);
        }
        return "moveNode completed. originFile='" + originFile + "', destinationFile='" + destinationFile
                + "', movedNodes='" + sanitizedNodes + "'";
    }

    private Set<String> sanitizeNodes(Set<String> originNodes) {
        LinkedHashSet<String> nodes = new LinkedHashSet<>();
        for (String node : originNodes) {
            if (node == null) {
                continue;
            }
            String trimmed = node.trim();
            if (!trimmed.isEmpty()) {
                nodes.add(trimmed);
            }
        }
        return nodes;
    }

    private List<String> readLines(Path path) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (AccessDeniedException e) {
            throw new IllegalArgumentException("Permission denied while reading file: '" + path + "'", e);
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("File not found while reading file: '" + path + "'", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("I/O error while reading file: '" + path + "'. cause=" + e.getMessage(), e);
        }
    }

    private void writeLines(Path path, List<String> lines) {
        try {
            List<String> sanitizedLines = normalizeNonBlankLines(lines);
            String lineSeparator = System.lineSeparator();
            String content = sanitizedLines.isEmpty() ? "" : String.join(lineSeparator, sanitizedLines) + lineSeparator;
            Files.writeString(
                    path,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (AccessDeniedException e) {
            throw new IllegalArgumentException("Permission denied while writing file: '" + path + "'", e);
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("File not found while writing file: '" + path + "'", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("I/O error while writing file: '" + path + "'. cause=" + e.getMessage(), e);
        }
    }

    private void appendLine(Path path, String line) {
        List<String> lines = normalizeNonBlankLines(readLines(path));
        if (!line.isBlank()) {
            lines.add(line);
        }
        writeLines(path, lines);
    }

    private List<String> normalizeNonBlankLines(List<String> lines) {
        List<String> normalized = new ArrayList<>(lines.size());
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            normalized.add(line);
        }
        return normalized;
    }

    private Path resolveExistingGroupFile(Path folder, String groupName) {
        Path cwd = getCwd();
        List<Path> candidates = List.of(
                folder.resolve(groupName).normalize(),
                folder.resolve(groupName + ".txt").normalize());

        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Origin group file not found for group='" + groupName
                + "'. tried=[" + toAbsoluteFromCwd(candidates.get(0), cwd) + ", "
                + toAbsoluteFromCwd(candidates.get(1), cwd) + "]");
    }

    private Path resolveExistingDestinationGroupFile(Path folder, String destinationGroup) {
        Path cwd = getCwd();
        Path candidate = folder.resolve(destinationGroup + ".txt").normalize();
        if (Files.isRegularFile(candidate)) {
            return candidate;
        }
        throw new IllegalArgumentException("Destination group file not found for group='" + destinationGroup
                + "'. tried=[" + toAbsoluteFromCwd(candidate, cwd) + "]");
    }

    private Path resolveExistingFolder(String groupFolder) {
        Path cwd = getCwd();
        Path firstTry = Paths.get(groupFolder);
        if (Files.isDirectory(firstTry)) {
            return firstTry.normalize();
        }
        String fallbackInput = groupFolder.replaceFirst("^\\.\\./", "");
        Path secondTry = Paths.get(fallbackInput);
        if (!fallbackInput.equals(groupFolder) && Files.isDirectory(secondTry)) {
            return secondTry.normalize();
        }
        Path firstTryNormalized = firstTry.normalize();
        Path secondTryNormalized = secondTry.normalize();
        throw new IllegalArgumentException("groupFolder does not exist or is not a directory. input='"
                + groupFolder + "', tried=[" + toAbsoluteFromCwd(firstTry, cwd) + ", "
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
}
