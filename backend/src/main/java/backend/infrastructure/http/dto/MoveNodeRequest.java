package backend.infrastructure.http.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MoveNodeRequest(
        @NotBlank(message = "groupFolder is required")
        @Size(max = 1024, message = "groupFolder length must be <= 1024")
        String groupFolder,

        @NotBlank(message = "originGroup is required")
        @Size(max = 255, message = "originGroup length must be <= 255")
        String originGroup,

        @NotBlank(message = "originNode is required")
        @Size(max = 512, message = "originNode length must be <= 512")
        String originNode,

        @NotBlank(message = "destinationGroup is required")
        @Size(max = 255, message = "destinationGroup length must be <= 255")
        String destinationGroup) {
}
