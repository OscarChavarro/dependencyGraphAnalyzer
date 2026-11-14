package backend.application.port.in;

import backend.domain.model.NodeAssignmentModel;
import backend.domain.model.NodeAssignmentResultModel;
import java.util.List;

public interface AssignNodesUseCase {
    /** Applies the assignments in order; a later assignment overrides an earlier one for the same node. */
    NodeAssignmentResultModel execute(String projectId, List<NodeAssignmentModel> assignments);
}
