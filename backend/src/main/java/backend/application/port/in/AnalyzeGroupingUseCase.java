package backend.application.port.in;

import backend.domain.model.GroupingAnalysisModel;

public interface AnalyzeGroupingUseCase {
    /** @param limit maximum number of orphan nodes and of backward relations listed */
    GroupingAnalysisModel execute(String projectId, int limit);
}
