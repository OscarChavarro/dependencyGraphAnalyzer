package backend.domain.model;

import java.util.List;

public record GraphModelSnapshot(List<GraphModelNode> nodes, List<GraphModelEdge> edges) {
}
