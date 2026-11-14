package backend.domain.model;

import java.util.List;
import java.util.Map;

/**
 * A raw graph together with the group definitions of its project.
 *
 * @param groups      every group file of the project folder, including the orphans file
 * @param groupByNode group name of each classified graph node; orphan nodes are absent
 */
public record GroupedGraphModel(
        ProjectDescriptorModel project,
        RawGraphModel graph,
        List<GroupFileModel> groups,
        Map<String, String> groupByNode) {

    /**
     * True when the structure graph draws the relation red: the group of {@code from} has a
     * lower layer number than the group of {@code to}.
     */
    public boolean isBackward(NodeRelationModel relation) {
        Integer fromLevel = GroupFileModel.levelOf(groupByNode.get(relation.from()));
        Integer toLevel = GroupFileModel.levelOf(groupByNode.get(relation.to()));
        return fromLevel != null && toLevel != null && fromLevel < toLevel;
    }
}
