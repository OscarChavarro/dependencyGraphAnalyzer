package backend.infrastructure.http.controller;

import backend.application.port.in.UpdateGraphModelUseCase;
import backend.application.port.in.BuildEnrichedEdgesUseCase;
import backend.infrastructure.http.dto.UpdateGraphModelRequest;
import backend.infrastructure.http.dto.EnrichedEdgesResponse;
import backend.infrastructure.http.dto.UpdateGraphModelResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(originPatterns = {
        "http://localhost",
        "http://localhost:*",
        "https://localhost",
        "https://localhost:*",
        "http://192.168.1.*",
        "http://192.168.1.*:*",
        "https://192.168.1.*",
        "https://192.168.1.*:*"
})
public class GraphModelController {
    private final UpdateGraphModelUseCase updateGraphModelUseCase;
    private final BuildEnrichedEdgesUseCase buildEnrichedEdgesUseCase;

    public GraphModelController(
            UpdateGraphModelUseCase updateGraphModelUseCase,
            BuildEnrichedEdgesUseCase buildEnrichedEdgesUseCase) {
        this.updateGraphModelUseCase = updateGraphModelUseCase;
        this.buildEnrichedEdgesUseCase = buildEnrichedEdgesUseCase;
    }

    @PostMapping({"/updateGraph", "/updateGraphModel"})
    public UpdateGraphModelResponse updateGraphModel(@RequestBody UpdateGraphModelRequest request) {
        if (request == null || request.generator() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "generator is required");
        }
        if (request.groupsDefinitionFolder() == null || request.groupsDefinitionFolder().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "groupsDefinitionFolder is required");
        }

        return new UpdateGraphModelResponse(
                updateGraphModelUseCase.execute(request.generator(), request.groupsDefinitionFolder()));
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
}
