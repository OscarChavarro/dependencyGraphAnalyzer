package backend.infrastructure.mcp.tool;

import backend.application.port.in.SummarizeDependenciesUseCase;
import backend.infrastructure.mcp.McpSchema;
import backend.infrastructure.mcp.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SummarizeDependenciesTool implements McpTool {
    private final SummarizeDependenciesUseCase summarizeDependenciesUseCase;

    public SummarizeDependenciesTool(SummarizeDependenciesUseCase summarizeDependenciesUseCase) {
        this.summarizeDependenciesUseCase = summarizeDependenciesUseCase;
    }

    @Override
    public String name() {
        return "summarize_dependencies";
    }

    @Override
    public String description() {
        return "Summarizes the dependencies of a project by container (directory of a path-like node name,"
                + " or package of a dotted name): node count, current groups, which containers it uses and"
                + " which use it, with relation counts. Use it to decide the layers before assigning nodes.";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return McpSchema.object(
                Map.of(
                        "projectId", McpSchema.string("Project id from list_projects"),
                        "depth", McpSchema.integer("Maximum name segments of a container; 0 (default) keeps"
                                + " the full parent path")),
                List.of("projectId"));
    }

    @Override
    public Object call(JsonNode arguments) {
        return summarizeDependenciesUseCase.execute(
                arguments.path("projectId").asText(null),
                arguments.path("depth").asInt(0));
    }
}
