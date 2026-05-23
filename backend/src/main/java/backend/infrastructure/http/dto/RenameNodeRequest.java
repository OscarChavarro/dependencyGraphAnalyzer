package backend.infrastructure.http.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameNodeRequest(
        @NotBlank(message = "groupFolder is required")
        @Size(max = 1024, message = "groupFolder length must be <= 1024")
        String groupFolder,

        @NotBlank(message = "oldGroupName is required")
        @Size(max = 255, message = "oldGroupName length must be <= 255")
        String oldGroupName,

        @NotBlank(message = "newGroupName is required")
        @Size(max = 255, message = "newGroupName length must be <= 255")
        String newGroupName) {
}
