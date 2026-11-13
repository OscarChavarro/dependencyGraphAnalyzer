package backend.application.port.out;

import backend.domain.model.CachedProjectModel;
import java.nio.file.Path;

public interface CachedProjectsPort {
    /** @throws IllegalArgumentException when the project id is unknown */
    CachedProjectModel requireById(String projectId);

    /**
     * Resolves a path written in the projects config. Relative paths are relative to the
     * repository root; a leading "$HOME", "${HOME}" or "~" expands to the user home.
     */
    Path resolveConfiguredPath(String configuredPath);
}
