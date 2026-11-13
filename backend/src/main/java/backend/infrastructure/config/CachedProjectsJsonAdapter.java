package backend.infrastructure.config;

import backend.application.port.out.CachedProjectsPort;
import backend.domain.model.CachedProjectModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/** Reads etc/projects/cachedProjects.json, looked up from the repository root or from backend/. */
@Component
public class CachedProjectsJsonAdapter implements CachedProjectsPort {
    private static final Path CONFIG_RELATIVE_PATH = Path.of("etc", "projects", "cachedProjects.json");

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public CachedProjectModel requireById(String projectId) {
        return readAll().stream()
                .filter(project -> project.id() != null && project.id().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown cached project id: '" + projectId + "'"));
    }

    @Override
    public Path resolveConfiguredPath(String configuredPath) {
        if (configuredPath == null || configuredPath.isBlank()) {
            throw new IllegalArgumentException("Configured path is empty");
        }
        String trimmed = configuredPath.trim();
        String home = System.getProperty("user.home");
        for (String homePrefix : List.of("${HOME}", "$HOME", "~")) {
            if (trimmed.equals(homePrefix) || trimmed.startsWith(homePrefix + "/")) {
                return Path.of(home + trimmed.substring(homePrefix.length())).normalize();
            }
        }
        Path path = Path.of(trimmed);
        return path.isAbsolute() ? path.normalize() : resolveRepositoryRoot().resolve(path).normalize();
    }

    private List<CachedProjectModel> readAll() {
        Path configPath = resolveConfigPath();
        try {
            return objectMapper.readValue(configPath.toFile(), new TypeReference<List<CachedProjectModel>>() {
            });
        } catch (IOException e) {
            throw new IllegalStateException("Failed reading cached projects config: '" + configPath + "'", e);
        }
    }

    private Path resolveRepositoryRoot() {
        // config is <root>/etc/projects/cachedProjects.json
        return resolveConfigPath().toAbsolutePath().getParent().getParent().getParent();
    }

    private Path resolveConfigPath() {
        return Stream.of(CONFIG_RELATIVE_PATH, Path.of("..").resolve(CONFIG_RELATIVE_PATH))
                .filter(Files::isRegularFile)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "cached projects config not found: " + CONFIG_RELATIVE_PATH));
    }
}
