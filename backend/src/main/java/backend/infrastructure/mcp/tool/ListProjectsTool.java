package backend.infrastructure.mcp.tool;

import backend.application.port.in.ListProjectsUseCase;
import backend.infrastructure.mcp.McpSchema;
import backend.infrastructure.mcp.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ListProjectsTool implements McpTool {
    private final ListProjectsUseCase listProjectsUseCase;

    public ListProjectsTool(ListProjectsUseCase listProjectsUseCase) {
        this.listProjectsUseCase = listProjectsUseCase;
    }

    @Override
    public String name() {
        return "list_projects";
    }

    @Override
    public String description() {
        return "Lists the projects of every catalog in etc/projects with their id, generator, group"
                + " definition folder and inputs. The id is the projectId argument of the other tools.";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return McpSchema.object(Map.of(), List.of());
    }

    @Override
    public Object call(JsonNode arguments) {
        return listProjectsUseCase.execute().stream()
                .map(project -> Map.of(
                        "id", String.valueOf(project.id()),
                        "name", String.valueOf(project.name()),
                        "generator", project.generator().name(),
                        "groupsDefinitionFolder", project.groupsDefinitionFolder().toString(),
                        "inputs", project.inputs()))
                .toList();
    }
}
