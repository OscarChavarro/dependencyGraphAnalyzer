package backend.application.service;

import backend.application.port.in.DescribeNodesUseCase;
import backend.domain.model.GroupedGraphModel;
import backend.domain.model.NodeDescriptionModel;
import backend.domain.model.NodeDescriptionModel.Neighbor;
import backend.domain.model.NodeRelationModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DescribeNodesService implements DescribeNodesUseCase {
    private final GroupedGraphLoader groupedGraphLoader;

    public DescribeNodesService(GroupedGraphLoader groupedGraphLoader) {
        this.groupedGraphLoader = groupedGraphLoader;
    }

    @Override
    public List<NodeDescriptionModel> execute(String projectId, String prefix, int limit) {
        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("prefix is required");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be positive. received=" + limit);
        }
        GroupedGraphModel grouped = groupedGraphLoader.load(projectId);
        List<String> selected = grouped.graph().nodes().stream()
                .filter(node -> node.startsWith(prefix.trim()))
                .sorted()
                .limit(limit)
                .toList();

        List<NodeDescriptionModel> descriptions = new ArrayList<>();
        for (String node : selected) {
            List<Neighbor> uses = new ArrayList<>();
            List<Neighbor> usedBy = new ArrayList<>();
            for (NodeRelationModel relation : grouped.graph().relations()) {
                if (relation.from().equals(node)) {
                    uses.add(neighbor(grouped, relation.to(), relation));
                } else if (relation.to().equals(node)) {
                    usedBy.add(neighbor(grouped, relation.from(), relation));
                }
            }
            uses.sort(Comparator.comparing(Neighbor::node));
            usedBy.sort(Comparator.comparing(Neighbor::node));
            descriptions.add(new NodeDescriptionModel(node, grouped.groupByNode().get(node), uses, usedBy));
        }
        return descriptions;
    }

    private Neighbor neighbor(GroupedGraphModel grouped, String node, NodeRelationModel relation) {
        return new Neighbor(node, grouped.groupByNode().get(node), grouped.isBackward(relation));
    }
}
