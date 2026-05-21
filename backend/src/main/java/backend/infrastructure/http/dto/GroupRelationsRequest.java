package backend.infrastructure.http.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupRelationsRequest(
        @NotBlank(message = "groupA is required")
        @Size(max = 255, message = "groupA length must be <= 255")
        String groupA,

        @NotBlank(message = "groupB is required")
        @Size(max = 255, message = "groupB length must be <= 255")
        String groupB) {
}
