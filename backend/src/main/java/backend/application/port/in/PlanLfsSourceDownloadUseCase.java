package backend.application.port.in;

import backend.domain.model.LfsDownloadPlanModel;
import java.util.List;

public interface PlanLfsSourceDownloadUseCase {
    LfsDownloadPlanModel execute(String projectId, List<String> selectedNodes);
}
