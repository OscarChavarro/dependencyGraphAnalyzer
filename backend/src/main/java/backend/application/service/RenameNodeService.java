package backend.application.service;

import backend.application.port.in.RenameNodeUseCase;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RenameNodeService implements RenameNodeUseCase {
    @Override
    public String execute(String groupFolder, String oldGroupName, String newGroupName) {
        String sanitizedOldGroup = sanitizeGroupName(oldGroupName, "oldGroupName");
        String sanitizedNewGroup = sanitizeGroupName(newGroupName, "newGroupName");
        if (sanitizedOldGroup.equals(sanitizedNewGroup)) {
            throw new IllegalArgumentException("oldGroupName and newGroupName must be different");
        }

        Path rootFolder = resolveExistingFolder(groupFolder);
        Path oldGroupFile = resolveExistingGroupFile(rootFolder, sanitizedOldGroup);
        Path newGroupFile = rootFolder.resolve(sanitizedNewGroup + ".txt").normalize();

        if (Files.exists(newGroupFile)) {
            throw new IllegalArgumentException("Cannot rename group because destination file already exists. oldGroupFile='"
                    + oldGroupFile + "', destinationFile='" + newGroupFile + "'");
        }

        try {
            Files.move(oldGroupFile, newGroupFile, StandardCopyOption.ATOMIC_MOVE);
        } catch (UnsupportedOperationException e) {
            // Fallback for filesystems without atomic move support.
            moveWithoutAtomic(oldGroupFile, newGroupFile);
        } catch (AccessDeniedException e) {
            throw new IllegalArgumentException("Permission denied while renaming group file. oldGroupFile='"
                    + oldGroupFile + "', destinationFile='" + newGroupFile + "'", e);
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("Group file not found while renaming. oldGroupFile='" + oldGroupFile + "'", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("I/O error while renaming group file. oldGroupFile='" + oldGroupFile
                    + "', destinationFile='" + newGroupFile + "'. cause=" + e.getMessage(), e);
        }

        List<String> lines = readLines(newGroupFile);
        List<String> updatedLines = updateHeaderLine(lines, sanitizedNewGroup);
        writeLines(newGroupFile, updatedLines);

        return "renameNode completed. oldGroupFile='" + oldGroupFile + "', newGroupFile='" + newGroupFile
                + "', header='[" + sanitizedNewGroup.toUpperCase() + "]'";
    }

    private String sanitizeGroupName(String rawName, String fieldName) {
        String trimmed = rawName == null ? "" : rawName.trim().toLowerCase();
        if (!trimmed.matches("^\\d+_[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException(fieldName
                    + " must match pattern 'number_name', for example '95_new_things'");
        }
        return trimmed;
    }

    private Path resolveExistingFolder(String groupFolder) {
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        Path firstTry = Paths.get(groupFolder);
        if (Files.isDirectory(firstTry)) {
            return firstTry.normalize();
        }
        String fallbackInput = groupFolder.replaceFirst("^\\.\\./", "");
        Path secondTry = Paths.get(fallbackInput);
        if (!fallbackInput.equals(groupFolder) && Files.isDirectory(secondTry)) {
            return secondTry.normalize();
        }
        throw new IllegalArgumentException("groupFolder does not exist or is not a directory. input='"
                + groupFolder + "', tried=[" + cwd.resolve(firstTry).normalize() + ", "
                + cwd.resolve(firstTry.normalize()).normalize() + ", "
                + cwd.resolve(secondTry).normalize() + ", "
                + cwd.resolve(secondTry.normalize()).normalize() + "]");
    }

    private Path resolveExistingGroupFile(Path folder, String groupName) {
        Path candidate = folder.resolve(groupName + ".txt").normalize();
        if (Files.isRegularFile(candidate)) {
            return candidate;
        }
        throw new IllegalArgumentException("Group file not found for oldGroupName='" + groupName
                + "'. tried=['" + candidate + "']");
    }

    private void moveWithoutAtomic(Path oldGroupFile, Path newGroupFile) {
        try {
            Files.move(oldGroupFile, newGroupFile);
        } catch (AccessDeniedException e) {
            throw new IllegalArgumentException("Permission denied while renaming group file. oldGroupFile='"
                    + oldGroupFile + "', destinationFile='" + newGroupFile + "'", e);
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("Group file not found while renaming. oldGroupFile='" + oldGroupFile + "'", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("I/O error while renaming group file. oldGroupFile='" + oldGroupFile
                    + "', destinationFile='" + newGroupFile + "'. cause=" + e.getMessage(), e);
        }
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

    private List<String> updateHeaderLine(List<String> lines, String newGroupName) {
        ArrayList<String> updated = new ArrayList<>();
        if (lines.isEmpty()) {
            updated.add("[" + newGroupName.toUpperCase() + "]");
            return updated;
        }

        updated.add("[" + newGroupName.toUpperCase() + "]");
        if (lines.size() > 1) {
            updated.addAll(lines.subList(1, lines.size()));
        }
        return updated;
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
}
