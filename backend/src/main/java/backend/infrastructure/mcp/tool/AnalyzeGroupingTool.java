package backend.infrastructure.mcp.tool;

import backend.application.port.in.AnalyzeGroupingUseCase;
import backend.infrastructure.mcp.McpSchema;
import backend.infrastructure.mcp.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AnalyzeGroupingTool implements McpTool {
    private static final int DEFAULT_LIMIT = 300;

    private final AnalyzeGroupingUseCase analyzeGroupingUseCase;

    public AnalyzeGroupingTool(AnalyzeGroupingUseCase analyzeGroupingUseCase) {
        this.analyzeGroupingUseCase = analyzeGroupingUseCase;
    }

    @Override
    public String name() {
        return "analyze_grouping";
    }

    @Override
    public String description() {
        return "Builds the graph of a project from its current sources (without writing cache.txt or output/)"
                + " and reports the state of its grouping: groups with their layer number and stale members,"
                + " orphan nodes, nodes listed in several groups, backward relations (a group that depends on a"
                + " group with a higher number, drawn red in the structure graph) with the node relations"
                + " that cause them, and groups that share a number and depend on each other.";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return McpSchema.object(
                Map.of(
                        "projectId", McpSchema.string("Project id from list_projects"),
                        "limit", McpSchema.integer("Maximum orphan nodes and backward relations listed."
                                + " Default " + DEFAULT_LIMIT)),
                List.of("projectId"));
    }

    @Override
    public Object call(JsonNode arguments) {
        return analyzeGroupingUseCase.execute(
                arguments.path("projectId").asText(null),
                arguments.path("limit").asInt(DEFAULT_LIMIT));
    }
}
