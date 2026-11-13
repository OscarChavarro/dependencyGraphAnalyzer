package backend.application.port.in;

import backend.domain.model.LfsBuildGuideEntryModel;
import backend.domain.model.LfsBuildGuideResultModel;
import java.util.List;

public interface WriteLfsBuildGuideUseCase {
    /** Writes AGENTS.md in the project's linuxFromScratch folder; entries are in build order. */
    LfsBuildGuideResultModel execute(String projectId, List<LfsBuildGuideEntryModel> entries);
}
