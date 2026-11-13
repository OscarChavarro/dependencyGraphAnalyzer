package backend.infrastructure.http.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LfsDownloadSourceRequest(
        @NotBlank(message = "projectId is required")
        String projectId,

        @NotBlank(message = "packageName is required")
        @Size(max = 255, message = "packageName length must be <= 255")
        String packageName,

        /** 1-based position in the client's plan; optional, used only for progress logs. */
        @Min(value = 0, message = "position must be >= 0")
        Integer position,

        /** Size of the client's plan; optional, used only for progress logs. */
        @Min(value = 0, message = "total must be >= 0")
        Integer total) {

    public int positionOrZero() {
        return position == null ? 0 : position;
    }

    public int totalOrZero() {
        return total == null ? 0 : total;
    }
}
