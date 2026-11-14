package backend.infrastructure.mcp.tool;

import backend.application.port.in.DescribeNodesUseCase;
import backend.infrastructure.mcp.McpSchema;
import backend.infrastructure.mcp.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DescribeNodesTool implements McpTool {
    private static final int DEFAULT_LIMIT = 50;

    private final DescribeNodesUseCase describeNodesUseCase;

    public DescribeNodesTool(DescribeNodesUseCase describeNodesUseCase) {
        this.describeNodesUseCase = describeNodesUseCase;
    }

    @Override
    public String name() {
        return "describe_nodes";
    }

    @Override
    public String description() {
        return "Lists, for every node whose name starts with a prefix, its group, the nodes it uses and the"
                + " nodes that use it, each with its group and whether that relation is backward. Use it to"
                + " decide how to split a directory that causes backward relations.";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return McpSchema.object(
                Map.of(
                        "projectId", McpSchema.string("Project id from list_projects"),
                        "prefix", McpSchema.string("Node name prefix, for example a directory with its"
                                + " trailing slash, or a full node name"),
                        "limit", McpSchema.integer("Maximum nodes described. Default " + DEFAULT_LIMIT)),
                List.of("projectId", "prefix"));
    }

    @Override
    public Object call(JsonNode arguments) {
        return describeNodesUseCase.execute(
                arguments.path("projectId").asText(null),
                arguments.path("prefix").asText(null),
                arguments.path("limit").asInt(DEFAULT_LIMIT));
    }
}
