package backend.domain.model;

import java.time.LocalDateTime;
import java.util.List;

/** Content of the AGENTS.md build guide written in the linuxFromScratch folder. */
public record LfsBuildGuideModel(
        String projectId,
        LocalDateTime generatedAt,
        List<LfsBuildStepModel> steps,
        List<LfsBuildGuideEntryModel> failedPackages) {
}
