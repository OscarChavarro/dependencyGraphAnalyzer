package backend.application.service;

import backend.application.port.in.CreateNewGroupUseCase;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;

@Service
public class CreateNewGroupService implements CreateNewGroupUseCase {
    @Override
    public String execute(String groupFolder, String newGroupName) {
        String sanitizedGroup = sanitizeNewGroupName(newGroupName);
        Path rootFolder = resolveExistingFolder(groupFolder);
        Path groupFile = rootFolder.resolve(sanitizedGroup.toLowerCase() + ".txt").normalize();

        String firstLine = "[" + sanitizedGroup.toUpperCase() + "]" + System.lineSeparator();
        try {
            Files.writeString(groupFile, firstLine, StandardCharsets.UTF_8);
        } catch (FileAlreadyExistsException e) {
            throw new IllegalArgumentException(
                    "Group file was not created because it already exists: '" + groupFile + "'", e);
        } catch (AccessDeniedException e) {
            throw new RuntimeException("Permission denied while creating group file: '" + groupFile + "'", e);
        } catch (IOException e) {
            throw new RuntimeException(
                    "I/O error while creating group file: '" + groupFile + "'. cause=" + e.getMessage(), e);
        }

        return "createNewGroup completed. groupFile='" + groupFile + "'";
    }

    private String sanitizeNewGroupName(String newGroupName) {
        String trimmed = newGroupName == null ? "" : newGroupName.trim();
        if (!trimmed.matches("^\\d+_[A-Za-z0-9_]+$")) {
            throw new IllegalArgumentException(
                    "newGroupName must match pattern 'number_name', for example '95_new_things'");
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
}
