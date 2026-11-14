package backend.infrastructure.mcp.tool;

import backend.application.port.in.AssignNodesUseCase;
import backend.domain.model.NodeAssignmentModel;
import backend.infrastructure.mcp.McpSchema;
import backend.infrastructure.mcp.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AssignNodesTool implements McpTool {
    private final AssignNodesUseCase assignNodesUseCase;

    public AssignNodesTool(AssignNodesUseCase assignNodesUseCase) {
        this.assignNodesUseCase = assignNodesUseCase;
    }

    @Override
    public String name() {
        return "assign_nodes";
    }

    @Override
    public String description() {
        return "Writes group definition files: places nodes in groups, removing them from any other group"
                + " and from 999_orphans, and creating group files that do not exist. Assignments apply in"
                + " order and a later one overrides an earlier one for the same node, so list general"
                + " prefixes first and exceptions after. The request is rejected as a whole, writing nothing,"
                + " when a node is not in the graph or a prefix matches no node.";
    }

    @Override
    public Map<String, Object> inputSchema() {
        Map<String, Object> assignment = McpSchema.object(
                Map.of(
                        "group", McpSchema.string("Destination group as number_name, e.g. 23_solid_operators"),
                        "nodes", McpSchema.array(McpSchema.string("Node name"), "Exact node names"),
                        "prefixes", McpSchema.array(
                                McpSchema.string("Node name prefix"),
                                "Every graph node whose name starts with one of these prefixes")),
                List.of("group"));
        return McpSchema.object(
                Map.of(
                        "projectId", McpSchema.string("Project id from list_projects"),
                        "assignments", McpSchema.array(assignment, "Assignments, applied in order")),
                List.of("projectId", "assignments"));
    }

    @Override
    public Object call(JsonNode arguments) {
        List<NodeAssignmentModel> assignments = new ArrayList<>();
        for (JsonNode assignment : arguments.path("assignments")) {
            assignments.add(new NodeAssignmentModel(
                    assignment.path("group").asText(null),
                    readStrings(assignment.path("nodes")),
                    readStrings(assignment.path("prefixes"))));
        }
        return assignNodesUseCase.execute(arguments.path("projectId").asText(null), assignments);
    }

    private List<String> readStrings(JsonNode array) {
        List<String> values = new ArrayList<>();
        for (JsonNode value : array) {
            values.add(value.asText());
        }
        return values;
    }
}
