package backend.application.service;

import backend.application.port.in.AssignNodesUseCase;
import backend.application.port.out.GroupFilesPort;
import backend.domain.model.GroupFileModel;
import backend.domain.model.GroupedGraphModel;
import backend.domain.model.NodeAssignmentModel;
import backend.domain.model.NodeAssignmentResultModel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/**
 * Places graph nodes in groups. Each assigned node is removed from every other group file
 * (including the orphans file) and appended to its destination, which is created when missing.
 * The whole request is validated before any file is written.
 */
@Service
public class AssignNodesService implements AssignNodesUseCase {
    private static final Pattern GROUP_NAME_PATTERN = Pattern.compile("^\\d+_[A-Za-z0-9_]+$");

    private final GroupedGraphLoader groupedGraphLoader;
    private final GroupFilesPort groupFilesPort;

    public AssignNodesService(GroupedGraphLoader groupedGraphLoader, GroupFilesPort groupFilesPort) {
        this.groupedGraphLoader = groupedGraphLoader;
        this.groupFilesPort = groupFilesPort;
    }

    @Override
    public NodeAssignmentResultModel execute(String projectId, List<NodeAssignmentModel> assignments) {
        if (assignments == null || assignments.isEmpty()) {
            throw new IllegalArgumentException("assignments is required and must not be empty");
        }
        GroupedGraphModel grouped = groupedGraphLoader.load(projectId);
        Map<String, String> destinationByNode = resolveDestinations(grouped, assignments);

        Path folder = grouped.project().groupsDefinitionFolder();
        Map<String, GroupFileModel> groupsByKey = new LinkedHashMap<>();
        for (GroupFileModel group : grouped.groups()) {
            groupsByKey.put(key(group.groupName()), group);
        }

        List<String> createdGroups = new ArrayList<>();
        for (String destination : destinationByNode.values()) {
            if (!groupsByKey.containsKey(destination)) {
                GroupFileModel created = groupFilesPort.create(folder, destination);
                groupsByKey.put(destination, created);
                createdGroups.add(created.groupName());
            }
        }

        int unchanged = 0;
        Map<String, Integer> assignedCountByGroup = new TreeMap<>();
        for (Map.Entry<String, GroupFileModel> entry : groupsByKey.entrySet()) {
            GroupFileModel group = entry.getValue();
            List<String> members = new ArrayList<>();
            for (String member : group.members()) {
                String destination = destinationByNode.get(member);
                if (destination == null || (destination.equals(entry.getKey()) && !members.contains(member))) {
                    members.add(member);
                }
            }
            int assigned = 0;
            for (Map.Entry<String, String> assignment : destinationByNode.entrySet()) {
                if (!assignment.getValue().equals(entry.getKey())) {
                    continue;
                }
                assigned++;
                if (members.contains(assignment.getKey())) {
                    unchanged++;
                } else {
                    members.add(assignment.getKey());
                }
            }
            if (assigned > 0) {
                assignedCountByGroup.put(group.groupName(), assigned);
            }
            if (!members.equals(group.members())) {
                groupFilesPort.writeMembers(group, members);
            }
        }
        return new NodeAssignmentResultModel(assignedCountByGroup, createdGroups, unchanged);
    }

    /** Node name to destination group key, with later assignments overriding earlier ones. */
    private Map<String, String> resolveDestinations(GroupedGraphModel grouped, List<NodeAssignmentModel> assignments) {
        Set<String> nodes = Set.copyOf(grouped.graph().nodes());
        Map<String, String> destinationByNode = new LinkedHashMap<>();
        List<String> unknownNodes = new ArrayList<>();
        List<String> emptyPrefixes = new ArrayList<>();

        for (NodeAssignmentModel assignment : assignments) {
            String destination = validateGroupName(assignment == null ? null : assignment.group());
            for (String prefix : orEmpty(assignment.prefixes())) {
                List<String> matches = prefix == null || prefix.isBlank()
                        ? List.of()
                        : grouped.graph().nodes().stream().filter(node -> node.startsWith(prefix.trim())).toList();
                if (matches.isEmpty()) {
                    emptyPrefixes.add(String.valueOf(prefix));
                }
                matches.forEach(node -> destinationByNode.put(node, destination));
            }
            for (String node : orEmpty(assignment.nodes())) {
                String trimmed = node == null ? "" : node.trim();
                if (nodes.contains(trimmed)) {
                    destinationByNode.put(trimmed, destination);
                } else {
                    unknownNodes.add(String.valueOf(node));
                }
            }
        }
        if (!unknownNodes.isEmpty() || !emptyPrefixes.isEmpty()) {
            throw new IllegalArgumentException("Nothing was written. nodes not in the graph=" + unknownNodes
                    + ", prefixes matching no node=" + emptyPrefixes);
        }
        if (destinationByNode.isEmpty()) {
            throw new IllegalArgumentException("assignments select no node");
        }
        return destinationByNode;
    }

    private String validateGroupName(String group) {
        String trimmed = group == null ? "" : group.trim();
        if (!GROUP_NAME_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("group must match 'number_name', for example '23_solid_operators'."
                    + " received='" + group + "'");
        }
        if (GroupFileModel.ORPHANS_GROUP_NAME.equalsIgnoreCase(trimmed)) {
            throw new IllegalArgumentException("nodes cannot be assigned to the orphans group");
        }
        return key(trimmed);
    }

    private String key(String groupName) {
        return groupName.toLowerCase(Locale.ROOT);
    }

    private List<String> orEmpty(List<String> values) {
        return values == null ? List.of() : values;
    }
}
