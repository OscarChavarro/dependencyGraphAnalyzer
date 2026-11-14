package backend.application.service;

import backend.application.port.in.AnalyzeGroupingUseCase;
import backend.domain.model.GroupFileModel;
import backend.domain.model.GroupedGraphModel;
import backend.domain.model.GroupingAnalysisModel;
import backend.domain.model.GroupingAnalysisModel.DuplicatedNode;
import backend.domain.model.GroupingAnalysisModel.GroupRelations;
import backend.domain.model.GroupingAnalysisModel.GroupSummary;
import backend.domain.model.GroupingAnalysisModel.MutualGroups;
import backend.domain.model.NodeRelationModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeGroupingService implements AnalyzeGroupingUseCase {
    private final GroupedGraphLoader groupedGraphLoader;

    public AnalyzeGroupingService(GroupedGraphLoader groupedGraphLoader) {
        this.groupedGraphLoader = groupedGraphLoader;
    }

    @Override
    public GroupingAnalysisModel execute(String projectId, int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit must not be negative. received=" + limit);
        }
        GroupedGraphModel grouped = groupedGraphLoader.load(projectId);
        Set<String> nodes = Set.copyOf(grouped.graph().nodes());
        Map<String, String> groupByNode = grouped.groupByNode();

        List<String> orphans = grouped.graph().nodes().stream()
                .filter(node -> !groupByNode.containsKey(node))
                .sorted()
                .toList();
        Map<String, List<NodeRelationModel>> backward = collectBackwardRelations(grouped);
        int backwardCount = backward.values().stream().mapToInt(List::size).sum();

        List<GroupRelations> backwardGroups = new ArrayList<>();
        int remaining = limit;
        for (Map.Entry<String, List<NodeRelationModel>> entry : backward.entrySet()) {
            List<NodeRelationModel> relations = entry.getValue();
            NodeRelationModel first = relations.get(0);
            List<NodeRelationModel> listed = relations.subList(0, Math.min(remaining, relations.size()));
            remaining -= listed.size();
            backwardGroups.add(new GroupRelations(
                    groupByNode.get(first.from()),
                    groupByNode.get(first.to()),
                    relations.size(),
                    List.copyOf(listed)));
        }

        return new GroupingAnalysisModel(
                grouped.project().id(),
                nodes.size(),
                grouped.graph().relations().size(),
                summarizeGroups(grouped, nodes),
                orphans.size(),
                orphans.subList(0, Math.min(limit, orphans.size())),
                findDuplicatedNodes(grouped, nodes),
                backwardCount,
                backwardGroups,
                findSameLevelMutualGroups(grouped),
                orphans.size() > limit || backwardCount > limit);
    }

    private List<GroupSummary> summarizeGroups(GroupedGraphModel grouped, Set<String> nodes) {
        List<GroupSummary> summaries = new ArrayList<>();
        for (GroupFileModel group : grouped.groups()) {
            List<String> stale = group.members().stream().filter(member -> !nodes.contains(member)).toList();
            summaries.add(new GroupSummary(
                    group.groupName(),
                    group.level(),
                    group.file().getFileName().toString(),
                    group.members().size() - stale.size(),
                    stale));
        }
        return summaries;
    }

    private List<DuplicatedNode> findDuplicatedNodes(GroupedGraphModel grouped, Set<String> nodes) {
        Map<String, List<String>> groupsByNode = new TreeMap<>();
        for (GroupFileModel group : grouped.groups()) {
            if (group.isOrphans()) {
                continue;
            }
            for (String member : group.members()) {
                if (nodes.contains(member)) {
                    List<String> groups = groupsByNode.computeIfAbsent(member, key -> new ArrayList<>());
                    if (!groups.contains(group.groupName())) {
                        groups.add(group.groupName());
                    }
                }
            }
        }
        return groupsByNode.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> new DuplicatedNode(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Map<String, List<NodeRelationModel>> collectBackwardRelations(GroupedGraphModel grouped) {
        Map<String, List<NodeRelationModel>> backward = new TreeMap<>();
        for (NodeRelationModel relation : grouped.graph().relations()) {
            if (grouped.isBackward(relation)) {
                String key = grouped.groupByNode().get(relation.from()) + " -> "
                        + grouped.groupByNode().get(relation.to());
                backward.computeIfAbsent(key, ignored -> new ArrayList<>()).add(relation);
            }
        }
        return backward;
    }

    private List<MutualGroups> findSameLevelMutualGroups(GroupedGraphModel grouped) {
        Map<String, Map<String, Integer>> counts = new TreeMap<>();
        for (NodeRelationModel relation : grouped.graph().relations()) {
            String from = grouped.groupByNode().get(relation.from());
            String to = grouped.groupByNode().get(relation.to());
            if (from == null || to == null || from.equals(to)) {
                continue;
            }
            Integer fromLevel = GroupFileModel.levelOf(from);
            if (fromLevel != null && Objects.equals(fromLevel, GroupFileModel.levelOf(to))) {
                counts.computeIfAbsent(from, key -> new TreeMap<>()).merge(to, 1, Integer::sum);
            }
        }
        List<MutualGroups> mutual = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> entry : counts.entrySet()) {
            String groupA = entry.getKey();
            for (Map.Entry<String, Integer> target : entry.getValue().entrySet()) {
                String groupB = target.getKey();
                Integer reverse = counts.getOrDefault(groupB, Map.of()).get(groupA);
                if (reverse != null && groupA.compareTo(groupB) < 0) {
                    mutual.add(new MutualGroups(groupA, groupB, target.getValue(), reverse));
                }
            }
        }
        return mutual;
    }
}
