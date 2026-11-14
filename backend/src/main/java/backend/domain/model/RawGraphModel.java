package backend.domain.model;

import java.util.List;

/** Nodes and dependencies exactly as produced by a graph builder plugin, before any grouping. */
public record RawGraphModel(List<String> nodes, List<NodeRelationModel> relations) {
}
