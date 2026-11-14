package backend.infrastructure.config;

import backend.application.port.out.ProjectCatalogPort;
import backend.domain.model.GraphModelGenerator;
import backend.domain.model.ProjectDescriptorModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Reads the four project catalogs of etc/projects, looked up from the repository root or from
 * backend/. Relative paths of a catalog are resolved against the repository root.
 */
@Component
public class ProjectCatalogJsonAdapter implements ProjectCatalogPort {
    private static final Path CATALOGS_RELATIVE_FOLDER = Path.of("etc", "projects");
    private static final Map<String, GraphModelGenerator> GENERATOR_BY_CATALOG = Map.of(
            "cachedProjects.json", GraphModelGenerator.CACHE_LOADER,
            "cppProjects.json", GraphModelGenerator.CPP_SOURCES,
            "javaProjects.json", GraphModelGenerator.JAVA_SOURCES,
            "typescriptProjects.json", GraphModelGenerator.TYPESCRIPT_SOURCES);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ProjectDescriptorModel> listAll() {
        Path catalogsFolder = resolveCatalogsFolder();
        Path repositoryRoot = catalogsFolder.toAbsolutePath().normalize().getParent().getParent();
        List<ProjectDescriptorModel> projects = new ArrayList<>();
        for (String catalog : GENERATOR_BY_CATALOG.keySet().stream().sorted().toList()) {
            Path catalogFile = catalogsFolder.resolve(catalog);
            if (!Files.isRegularFile(catalogFile)) {
                continue;
            }
            for (JsonNode entry : readCatalog(catalogFile)) {
                projects.add(mapProject(entry, GENERATOR_BY_CATALOG.get(catalog), repositoryRoot));
            }
        }
        return projects;
    }

    @Override
    public ProjectDescriptorModel requireById(String projectId) {
        if (projectId == null || projectId.isBlank()) {
            throw new IllegalArgumentException("projectId is required");
        }
        List<ProjectDescriptorModel> projects = listAll();
        return projects.stream()
                .filter(project -> projectId.trim().equals(project.id()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown project id: '" + projectId
                        + "'. known=" + projects.stream().map(ProjectDescriptorModel::id).toList()));
    }

    private JsonNode readCatalog(Path catalogFile) {
        try {
            return objectMapper.readTree(catalogFile.toFile());
        } catch (IOException e) {
            throw new IllegalStateException("Failed reading projects catalog: '" + catalogFile + "'", e);
        }
    }

    private ProjectDescriptorModel mapProject(JsonNode entry, GraphModelGenerator generator, Path repositoryRoot) {
        List<String> inputs = new ArrayList<>();
        if (generator == GraphModelGenerator.CACHE_LOADER) {
            for (String field : List.of("cacheFile", "extra")) {
                String value = entry.path(field).asText("");
                if (!value.isBlank()) {
                    inputs.add(resolveRootRelative(value, repositoryRoot).toString());
                }
            }
        } else {
            inputs.addAll(readStrings(entry.path("inputFolders")));
        }
        return new ProjectDescriptorModel(
                entry.path("id").asText(null),
                entry.path("name").asText(null),
                generator,
                resolveGroupsFolder(entry.path("groupsDefinitionFolder").asText(""), repositoryRoot),
                List.copyOf(inputs),
                List.copyOf(readStrings(entry.path("classpath"))));
    }

    private List<String> readStrings(JsonNode array) {
        List<String> values = new ArrayList<>();
        if (array.isArray()) {
            for (JsonNode value : array) {
                if (!value.asText("").isBlank()) {
                    values.add(value.asText().trim());
                }
            }
        }
        return values;
    }

    /** Catalogs written for a backend/ working directory use a leading "../"; it is dropped. */
    private Path resolveGroupsFolder(String configured, Path repositoryRoot) {
        return resolveRootRelative(configured.trim().replaceFirst("^\\.\\./", ""), repositoryRoot);
    }

    private Path resolveRootRelative(String configured, Path repositoryRoot) {
        Path path = Path.of(configured.trim());
        return path.isAbsolute() ? path.normalize() : repositoryRoot.resolve(path).normalize();
    }

    private Path resolveCatalogsFolder() {
        return Stream.of(CATALOGS_RELATIVE_FOLDER, Path.of("..").resolve(CATALOGS_RELATIVE_FOLDER))
                .filter(Files::isDirectory)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "projects catalog folder not found: " + CATALOGS_RELATIVE_FOLDER));
    }
}
