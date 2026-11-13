package backend.application.service;

import backend.application.port.out.CachedProjectsPort;
import backend.domain.model.CachedProjectModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

/** Resolves (and creates when missing) the linuxFromScratch folder of a cached project. */
@Component
public class LfsFolderResolver {
    private final CachedProjectsPort cachedProjectsPort;

    public LfsFolderResolver(CachedProjectsPort cachedProjectsPort) {
        this.cachedProjectsPort = cachedProjectsPort;
    }

    /** @throws IllegalArgumentException when the project is unknown, has no folder, or it cannot be created */
    public Path resolveAndCreate(String projectId) {
        CachedProjectModel project = cachedProjectsPort.requireById(projectId);
        if (project.linuxFromScratch() == null || project.linuxFromScratch().isBlank()) {
            throw new IllegalArgumentException("Project '" + project.id() + "' has no linuxFromScratch folder");
        }
        Path folder = cachedProjectsPort.resolveConfiguredPath(project.linuxFromScratch());
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot create linuxFromScratch folder: '" + folder + "'", e);
        }
        return folder;
    }
}
