package backend.application.service;

import backend.application.port.in.MoveNodeUseCase;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MoveNodeService implements MoveNodeUseCase {
    @Override
    public String execute(String groupFolder, String originGroup, String originNode, String destinationGroup) {
        Path rootFolder = resolveExistingFolder(groupFolder);
        Path originFile = resolveExistingGroupFile(rootFolder, originGroup);
        Path destinationFile = resolveExistingDestinationGroupFile(rootFolder, destinationGroup);

        List<String> originLines = readLines(originFile);
        boolean removed = false;
        List<String> updatedOriginLines = new ArrayList<>(originLines.size());

        for (String line : originLines) {
            if (!removed && line.equals(originNode)) {
                removed = true;
                continue;
            }
            updatedOriginLines.add(line);
        }

        if (!removed) {
            throw new IllegalArgumentException(
                    "originNode not found. node='" + originNode + "', originFile='" + originFile + "'");
        }

        writeLines(originFile, updatedOriginLines);
        appendLine(destinationFile, originNode);
        return "moveNode completed. originFile='" + originFile + "', destinationFile='" + destinationFile
                + "', movedNode='" + originNode + "'";
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
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (AccessDeniedException e) {
            throw new IllegalArgumentException("Permission denied while writing file: '" + path + "'", e);
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("File not found while writing file: '" + path + "'", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("I/O error while writing file: '" + path + "'. cause=" + e.getMessage(), e);
        }
    }

    private void appendLine(Path path, String line) {
        try {
            String prefix = Files.size(path) > 0 ? System.lineSeparator() : "";
            Files.writeString(path, prefix + line, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        } catch (AccessDeniedException e) {
            throw new IllegalArgumentException("Permission denied while appending destination file: '" + path + "'", e);
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("Destination file not found while appending: '" + path + "'", e);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "I/O error while appending destination file: '" + path + "'. cause=" + e.getMessage(), e);
        }
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
        Path literalPath = Paths.get(groupFolder);
        Path normalizedPath = literalPath.normalize();
        if (Files.isDirectory(literalPath)) {
            return normalizedPath;
        }
        throw new IllegalArgumentException("groupFolder does not exist or is not a directory. input='"
                + groupFolder + "', literalPath='" + literalPath + "', normalizedPath='" + normalizedPath
                + "', tried=[" + toAbsoluteFromCwd(literalPath, cwd) + ", "
                + toAbsoluteFromCwd(normalizedPath, cwd) + "]");
    }

    private Path getCwd() {
        return Paths.get("").toAbsolutePath().normalize();
    }

    private Path toAbsoluteFromCwd(Path path, Path cwd) {
        return cwd.resolve(path).normalize();
    }
}
