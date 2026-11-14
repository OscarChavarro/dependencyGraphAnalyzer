package backend.domain.model;

import java.util.List;

/**
 * State of the grouping of a project.
 *
 * @param orphanNodes            graph nodes that belong to no group
 * @param duplicatedNodes        graph nodes listed in more than one group
 * @param backwardGroupRelations group pairs drawn red in the structure graph
 * @param sameLevelMutualGroups  group pairs that share a layer number and depend on each other
 * @param truncated              true when a list was cut to the requested limit
 */
public record GroupingAnalysisModel(
        String projectId,
        int nodeCount,
        int relationCount,
        List<GroupSummary> groups,
        int orphanCount,
        List<String> orphanNodes,
        List<DuplicatedNode> duplicatedNodes,
        int backwardRelationCount,
        List<GroupRelations> backwardGroupRelations,
        List<MutualGroups> sameLevelMutualGroups,
        boolean truncated) {

    /** @param staleMembers members listed in the file that are not nodes of the graph */
    public record GroupSummary(String group, Integer level, String file, int nodeCount, List<String> staleMembers) {
    }

    public record DuplicatedNode(String node, List<String> groups) {
    }

    public record GroupRelations(String fromGroup, String toGroup, int relationCount, List<NodeRelationModel> relations) {
    }

    public record MutualGroups(String groupA, String groupB, int relationsFromA, int relationsFromB) {
    }
}
