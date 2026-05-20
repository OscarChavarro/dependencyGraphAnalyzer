package backend.domain.model;

import java.util.List;

public record GraphModelStructure(List<GraphModelNode> nodes, List<GraphModelEdge> edges) {
}
