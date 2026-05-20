package backend.infrastructure.http.controller;

import backend.application.port.in.UpdateGraphModelUseCase;
import backend.infrastructure.http.dto.UpdateGraphModelRequest;
import backend.infrastructure.http.dto.UpdateGraphModelResponse;
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

    public GraphModelController(UpdateGraphModelUseCase updateGraphModelUseCase) {
        this.updateGraphModelUseCase = updateGraphModelUseCase;
    }

    @PostMapping("/updateGraphModel")
    public UpdateGraphModelResponse updateGraphModel(@RequestBody UpdateGraphModelRequest request) {
        if (request == null || request.generator() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "generator is required");
        }

        updateGraphModelUseCase.execute(request.generator());
        return new UpdateGraphModelResponse("ok");
    }
}
