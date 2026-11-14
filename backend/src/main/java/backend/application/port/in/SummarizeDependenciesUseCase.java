package backend.application.port.in;

import backend.domain.model.DependencySummaryModel;

public interface SummarizeDependenciesUseCase {
    /** @param depth maximum number of name segments of a container; 0 means the full parent path */
    DependencySummaryModel execute(String projectId, int depth);
}
