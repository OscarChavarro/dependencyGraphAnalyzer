package backend.infrastructure.http.controller;

import backend.application.port.in.UpdateGraphModelUseCase;
import backend.application.port.in.BuildEnrichedEdgesUseCase;
import backend.application.port.in.MoveNodeUseCase;
import backend.domain.model.GraphModelGenerator;
import backend.infrastructure.http.dto.CppProjectResponse;
import backend.infrastructure.http.dto.CachedProjectResponse;
import backend.infrastructure.http.dto.JavaProjectResponse;
import backend.infrastructure.http.dto.TypeScriptProjectResponse;
import backend.infrastructure.http.dto.UpdateGraphModelRequest;
import backend.infrastructure.http.dto.EnrichedEdgesResponse;
import backend.infrastructure.http.dto.JavaSourcesGraphRequest;
import backend.infrastructure.http.dto.TypeScriptSourcesGraphRequest;
import backend.infrastructure.http.dto.MoveNodeRequest;
import backend.infrastructure.http.dto.MoveNodeResponse;
import backend.infrastructure.http.dto.GroupRelationsRequest;
import backend.infrastructure.http.dto.GroupRelationsResponse;
import backend.infrastructure.http.dto.UpdateGraphModelResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1")
public class GraphModelController {
    private final UpdateGraphModelUseCase updateGraphModelUseCase;
    private final BuildEnrichedEdgesUseCase buildEnrichedEdgesUseCase;
    private final MoveNodeUseCase moveNodeUseCase;
    private final ObjectMapper objectMapper;

    public GraphModelController(
            UpdateGraphModelUseCase updateGraphModelUseCase,
            BuildEnrichedEdgesUseCase buildEnrichedEdgesUseCase,
            MoveNodeUseCase moveNodeUseCase,
            ObjectMapper objectMapper) {
        this.updateGraphModelUseCase = updateGraphModelUseCase;
        this.buildEnrichedEdgesUseCase = buildEnrichedEdgesUseCase;
        this.moveNodeUseCase = moveNodeUseCase;
        this.objectMapper = objectMapper;
    }

    @PostMapping({"/updateGraph", "/updateGraphModel"})
    public UpdateGraphModelResponse updateGraphModel(@RequestBody UpdateGraphModelRequest request) {
        if (request == null || request.generator() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "generator is required");
        }
        if (request.groupsDefinitionFolder() == null || request.groupsDefinitionFolder().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "groupsDefinitionFolder is required");
        }
        if (request.generator() == GraphModelGenerator.CPP_SOURCES
                || request.generator() == GraphModelGenerator.JAVA_SOURCES
                || request.generator() == GraphModelGenerator.TYPESCRIPT_SOURCES) {
            if (request.inputFolders() == null || request.inputFolders().length == 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "inputFolders is required for " + request.generator());
            }
        }

        return new UpdateGraphModelResponse(
                updateGraphModelUseCase.execute(
                        request.generator(),
                        request.groupsDefinitionFolder(),
                        request.inputFolders(),
                        request.classpath()));
    }

    @PostMapping({"/enrichedEdges"})
    public EnrichedEdgesResponse enrichedEdges(@RequestBody UpdateGraphModelRequest request) {
        if (request == null || request.generator() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "generator is required");
        }
        if (request.groupsDefinitionFolder() == null || request.groupsDefinitionFolder().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "groupsDefinitionFolder is required");
        }

        return new EnrichedEdgesResponse(
                buildEnrichedEdgesUseCase.execute(
                        request.generator(),
                        request.groupsDefinitionFolder(),
                        request.inputFolders()));
    }

    @PostMapping({"/updateGraphModel/javaSources"})
    public UpdateGraphModelResponse updateGraphModelFromJavaSources(@RequestBody JavaSourcesGraphRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request is required");
        }
        if (request.groupsDefinitionFolder() == null || request.groupsDefinitionFolder().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "groupsDefinitionFolder is required");
        }
        if (request.inputFolders() == null || request.inputFolders().length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "inputFolders is required");
        }

        return new UpdateGraphModelResponse(
                updateGraphModelUseCase.execute(
                        GraphModelGenerator.JAVA_SOURCES,
                        request.groupsDefinitionFolder(),
                        request.inputFolders(),
                        request.classpath()));
    }

    @PostMapping({"/updateGraphModel/typescriptSources"})
    public UpdateGraphModelResponse updateGraphModelFromTypeScriptSources(
            @RequestBody TypeScriptSourcesGraphRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request is required");
        }
        if (request.groupsDefinitionFolder() == null || request.groupsDefinitionFolder().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "groupsDefinitionFolder is required");
        }
        if (request.inputFolders() == null || request.inputFolders().length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "inputFolders is required");
        }

        return new UpdateGraphModelResponse(
                updateGraphModelUseCase.execute(
                        GraphModelGenerator.TYPESCRIPT_SOURCES,
                        request.groupsDefinitionFolder(),
                        request.inputFolders(),
                        null));
    }


    @PostMapping({"/groupRelations"})
    public GroupRelationsResponse groupRelations(@Valid @RequestBody GroupRelationsRequest request) {
        String groupA = normalizeGroupToken(request.groupA());
        String groupB = normalizeGroupToken(request.groupB());
        Path path = Path.of("output", "cleanRelationsGraph.txt");

        if (!Files.isRegularFile(path)) {
            return new GroupRelationsResponse(List.of());
        }

        try {
            List<String> relations = Files.readAllLines(path).stream()
                    .map(String::trim)
                    .filter(line -> !line.isBlank())
                    .filter(line -> {
                        String normalizedLine = line.toLowerCase();
                        return normalizedLine.contains("[" + groupA + "]") && normalizedLine.contains("[" + groupB + "]");
                    })
                    .distinct()
                    .sorted()
                    .toList();
            return new GroupRelationsResponse(relations);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed reading cleanRelationsGraph", e);
        }
    }

    private String normalizeGroupToken(String value) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.startsWith("_[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(2, trimmed.length() - 1);
        } else if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        if (trimmed.endsWith(".txt")) {
            trimmed = trimmed.substring(0, trimmed.length() - 4);
        }
        return trimmed.toLowerCase();
    }

    @PostMapping({"/moveNode"})
    public MoveNodeResponse moveNode(@Valid @RequestBody MoveNodeRequest request) {
        String message = moveNodeUseCase.execute(
                request.groupFolder(),
                request.originGroup(),
                new LinkedHashSet<>(Arrays.asList(request.originNodes())),
                request.destinationGroup());
        return new MoveNodeResponse(true, message);
    }

    @GetMapping({"/cppProjects"})
    public List<CppProjectResponse> cppProjects() {
        Path projectsPath = resolveCppProjectsPath();
        if (projectsPath == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "cpp projects config not found: etc/cppProjects/projects.json");
        }
        try {
            return objectMapper.readValue(
                    projectsPath.toFile(),
                    new TypeReference<List<CppProjectResponse>>() {
                    });
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "failed reading cpp projects config",
                    e);
        }
    }

    @GetMapping({"/cachedProjects"})
    public List<CachedProjectResponse> cachedProjects() {
        Path projectsPath = resolveCachedProjectsPath();
        if (projectsPath == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "cached projects config not found: etc/cachedProjects/projects.json");
        }
        try {
            return objectMapper.readValue(
                    projectsPath.toFile(),
                    new TypeReference<List<CachedProjectResponse>>() {
                    });
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "failed reading cached projects config",
                    e);
        }
    }

    @GetMapping({"/javaProjects"})
    public List<JavaProjectResponse> javaProjects() {
        Path projectsPath = resolveJavaProjectsPath();
        if (projectsPath == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "java projects config not found: etc/javaProjects/projects.json");
        }
        try {
            List<JavaProjectResponse> configuredProjects = objectMapper.readValue(
                    projectsPath.toFile(),
                    new TypeReference<List<JavaProjectResponse>>() {
                    });
            return configuredProjects.stream()
                    .map(this::resolveJavaProjectClasspath)
                    .toList();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "failed reading java projects config",
                    e);
        }
    }

    @GetMapping({"/typescriptProjects"})
    public List<TypeScriptProjectResponse> typeScriptProjects() {
        Path projectsPath = resolveTypeScriptProjectsPath();
        if (projectsPath == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "typescript projects config not found: etc/typescriptProjects/projects.json");
        }
        try {
            return objectMapper.readValue(
                    projectsPath.toFile(),
                    new TypeReference<List<TypeScriptProjectResponse>>() {
                    });
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "failed reading typescript projects config",
                    e);
        }
    }

    private Path resolveCppProjectsPath() {
        return Stream.of(
                        Path.of("etc", "cppProjects", "projects.json"),
                        Path.of("..", "etc", "cppProjects", "projects.json"))
                .filter(Files::isRegularFile)
                .findFirst()
                .orElse(null);
    }

    private Path resolveCachedProjectsPath() {
        return Stream.of(
                        Path.of("etc", "cachedProjects", "projects.json"),
                        Path.of("..", "etc", "cachedProjects", "projects.json"))
                .filter(Files::isRegularFile)
                .findFirst()
                .orElse(null);
    }

    private Path resolveJavaProjectsPath() {
        return Stream.of(
                        Path.of("etc", "javaProjects", "projects.json"),
                        Path.of("..", "etc", "javaProjects", "projects.json"))
                .filter(Files::isRegularFile)
                .findFirst()
                .orElse(null);
    }

    private Path resolveTypeScriptProjectsPath() {
        return Stream.of(
                        Path.of("etc", "typescriptProjects", "projects.json"),
                        Path.of("..", "etc", "typescriptProjects", "projects.json"))
                .filter(Files::isRegularFile)
                .findFirst()
                .orElse(null);
    }

    private JavaProjectResponse resolveJavaProjectClasspath(JavaProjectResponse project) {
        String[] fallbackClasspath = project.classpath() == null ? new String[0] : project.classpath();
        if (project.gradleProjectDir() == null || project.gradleProjectDir().isBlank()
                || project.gradleModule() == null || project.gradleModule().isBlank()) {
            return project;
        }

        try {
            String[] resolvedClasspath = resolveGradleCompileClasspath(project);
            if (resolvedClasspath.length == 0) {
                return project;
            }
            return new JavaProjectResponse(
                    project.id(),
                    project.name(),
                    project.groupsDefinitionFolder(),
                    project.inputFolders(),
                    resolvedClasspath,
                    project.gradleProjectDir(),
                    project.gradleModule(),
                    project.gradleUserHome());
        } catch (Exception e) {
            System.err.println("WARN java-projects: failed to resolve Gradle classpath for project "
                    + project.id() + ": " + e.getMessage());
            return new JavaProjectResponse(
                    project.id(),
                    project.name(),
                    project.groupsDefinitionFolder(),
                    project.inputFolders(),
                    fallbackClasspath,
                    project.gradleProjectDir(),
                    project.gradleModule(),
                    project.gradleUserHome());
        }
    }

    private String[] resolveGradleCompileClasspath(JavaProjectResponse project) throws IOException, InterruptedException {
        Path initScript = writeGradleClasspathInitScript();
        String taskName = ":" + project.gradleModule() + ":printCompileClasspathFiles";

        ProcessBuilder processBuilder = new ProcessBuilder("gradle", "-q", "-I", initScript.toString(), taskName);
        processBuilder.directory(Path.of(project.gradleProjectDir()).toFile());
        processBuilder.redirectErrorStream(true);
        if (project.gradleUserHome() != null && !project.gradleUserHome().isBlank()) {
            processBuilder.environment().put("GRADLE_USER_HOME", project.gradleUserHome());
        }

        Process process = processBuilder.start();
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isBlank()) {
                    lines.add(trimmed);
                }
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("gradle task failed with exit code " + exitCode);
        }

        return lines.stream()
                .filter(line -> line.endsWith(".jar") && Path.of(line).isAbsolute())
                .distinct()
                .toArray(String[]::new);
    }

    private Path writeGradleClasspathInitScript() throws IOException {
        String script = """
                allprojects {
                  tasks.register('printCompileClasspathFiles') {
                    doLast {
                      if (configurations.findByName('compileClasspath') != null) {
                        configurations.compileClasspath.resolve().each { println it.absolutePath }
                      }
                    }
                  }
                }
                """;
        Path scriptFile = Files.createTempFile("rpk-classpath-", ".gradle");
        Files.writeString(
                scriptFile,
                script,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING);
        scriptFile.toFile().deleteOnExit();
        return scriptFile;
    }
}
