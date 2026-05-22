package backend.infrastructure.http.controller;

import backend.application.port.in.UpdateGraphModelUseCase;
import backend.application.port.in.BuildEnrichedEdgesUseCase;
import backend.application.port.in.MoveNodeUseCase;
import backend.domain.model.GraphModelGenerator;
import backend.infrastructure.http.dto.CppProjectResponse;
import backend.infrastructure.http.dto.UpdateGraphModelRequest;
import backend.infrastructure.http.dto.EnrichedEdgesResponse;
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
import java.nio.file.Files;
import java.nio.file.Path;
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
        if (request.generator() == GraphModelGenerator.CPP_SOURCES) {
            if (request.inputFolders() == null || request.inputFolders().length == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "inputFolders is required for CPP_SOURCES");
            }
        }

        return new UpdateGraphModelResponse(
                updateGraphModelUseCase.execute(
                        request.generator(),
                        request.groupsDefinitionFolder(),
                        request.inputFolders()));
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
                buildEnrichedEdgesUseCase.execute(request.generator(), request.groupsDefinitionFolder()));
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

    private Path resolveCppProjectsPath() {
        return Stream.of(
                        Path.of("etc", "cppProjects", "projects.json"),
                        Path.of("..", "etc", "cppProjects", "projects.json"))
                .filter(Files::isRegularFile)
                .findFirst()
                .orElse(null);
    }
}
