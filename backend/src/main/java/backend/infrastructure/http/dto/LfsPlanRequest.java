package backend.infrastructure.http.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LfsPlanRequest(
        @NotBlank(message = "projectId is required")
        String projectId,

        @NotEmpty(message = "selectedNodes is required")
        List<String> selectedNodes) {
}
