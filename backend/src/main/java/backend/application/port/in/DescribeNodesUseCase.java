package backend.application.port.in;

import backend.domain.model.NodeDescriptionModel;
import java.util.List;

public interface DescribeNodesUseCase {
    /** Describes the nodes whose name starts with {@code prefix}, up to {@code limit} nodes. */
    List<NodeDescriptionModel> execute(String projectId, String prefix, int limit);
}
