package backend.application.service;

import backend.application.port.in.SummarizeDependenciesUseCase;
import backend.domain.model.DependencySummaryModel;
import backend.domain.model.DependencySummaryModel.Container;
import backend.domain.model.GroupFileModel;
import backend.domain.model.GroupedGraphModel;
import backend.domain.model.NodeRelationModel;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;

@Service
public class SummarizeDependenciesService implements SummarizeDependenciesUseCase {
    private final GroupedGraphLoader groupedGraphLoader;
    private final NodeContainerResolver nodeContainerResolver;

    public SummarizeDependenciesService(
            GroupedGraphLoader groupedGraphLoader,
            NodeContainerResolver nodeContainerResolver) {
        this.groupedGraphLoader = groupedGraphLoader;
        this.nodeContainerResolver = nodeContainerResolver;
    }

    @Override
    public DependencySummaryModel execute(String projectId, int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("depth must not be negative. received=" + depth);
        }
        GroupedGraphModel grouped = groupedGraphLoader.load(projectId);

        Map<String, Integer> nodeCounts = new TreeMap<>();
        Map<String, Map<String, Integer>> groups = new TreeMap<>();
        for (String node : grouped.graph().nodes()) {
            String container = nodeContainerResolver.resolve(node, depth);
            nodeCounts.merge(container, 1, Integer::sum);
            String group = grouped.groupByNode().getOrDefault(node, GroupFileModel.ORPHANS_GROUP_NAME);
            groups.computeIfAbsent(container, key -> new TreeMap<>()).merge(group, 1, Integer::sum);
        }

        Map<String, Map<String, Integer>> uses = new TreeMap<>();
        Map<String, Map<String, Integer>> usedBy = new TreeMap<>();
        for (NodeRelationModel relation : grouped.graph().relations()) {
            String from = nodeContainerResolver.resolve(relation.from(), depth);
            String to = nodeContainerResolver.resolve(relation.to(), depth);
            if (from.equals(to)) {
                continue;
            }
            uses.computeIfAbsent(from, key -> new TreeMap<>()).merge(to, 1, Integer::sum);
            usedBy.computeIfAbsent(to, key -> new TreeMap<>()).merge(from, 1, Integer::sum);
        }

        List<Container> containers = nodeCounts.entrySet().stream()
                .map(entry -> new Container(
                        entry.getKey(),
                        entry.getValue(),
                        groups.get(entry.getKey()),
                        uses.getOrDefault(entry.getKey(), Map.of()),
                        usedBy.getOrDefault(entry.getKey(), Map.of())))
                .toList();
        return new DependencySummaryModel(grouped.project().id(), containers);
    }
}
