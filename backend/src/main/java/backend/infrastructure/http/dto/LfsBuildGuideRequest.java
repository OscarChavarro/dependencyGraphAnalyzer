package backend.infrastructure.http.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Ordered outcomes of an LFS run, used to write AGENTS.md. */
public record LfsBuildGuideRequest(
        @NotBlank(message = "projectId is required")
        String projectId,

        @NotNull(message = "entries is required")
        @Size(max = 100000, message = "too many entries")
        List<@Valid @NotNull LfsBuildGuideEntryRequest> entries) {

    public record LfsBuildGuideEntryRequest(
            @NotBlank(message = "packageName is required")
            @Size(max = 255, message = "packageName length must be <= 255")
            String packageName,

            @Size(max = 255, message = "groupName length must be <= 255")
            String groupName,

            @NotBlank(message = "status is required")
            @Pattern(regexp = "DOWNLOADED|ALREADY_PRESENT|FAILED", message = "unknown status")
            String status,

            @Size(max = 255, message = "sourcePackage length must be <= 255")
            String sourcePackage,

            @Size(max = 255, message = "sourceVersion length must be <= 255")
            String sourceVersion,

            @Size(max = 4000, message = "message length must be <= 4000")
            String message) {
    }
}
