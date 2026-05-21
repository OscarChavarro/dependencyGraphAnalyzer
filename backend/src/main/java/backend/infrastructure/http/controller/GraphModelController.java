package backend.infrastructure.http.controller;

import backend.application.port.in.UpdateGraphModelUseCase;
import backend.application.port.in.BuildEnrichedEdgesUseCase;
import backend.application.port.in.MoveNodeUseCase;
import backend.domain.model.GraphModelGenerator;
import backend.infrastructure.http.dto.UpdateGraphModelRequest;
import backend.infrastructure.http.dto.EnrichedEdgesResponse;
import backend.infrastructure.http.dto.MoveNodeRequest;
import backend.infrastructure.http.dto.MoveNodeResponse;
import backend.infrastructure.http.dto.UpdateGraphModelResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.LinkedHashSet;
import org.springframework.http.HttpStatus;
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

    public GraphModelController(
            UpdateGraphModelUseCase updateGraphModelUseCase,
            BuildEnrichedEdgesUseCase buildEnrichedEdgesUseCase,
            MoveNodeUseCase moveNodeUseCase) {
        this.updateGraphModelUseCase = updateGraphModelUseCase;
        this.buildEnrichedEdgesUseCase = buildEnrichedEdgesUseCase;
        this.moveNodeUseCase = moveNodeUseCase;
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

    @PostMapping({"/moveNode"})
    public MoveNodeResponse moveNode(@Valid @RequestBody MoveNodeRequest request) {
        String message = moveNodeUseCase.execute(
                request.groupFolder(),
                request.originGroup(),
                new LinkedHashSet<>(Arrays.asList(request.originNodes())),
                request.destinationGroup());
        return new MoveNodeResponse(true, message);
    }
}
