package backend.infrastructure.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

/** A tool exposed through the MCP endpoint. */
public interface McpTool {
    String name();

    String description();

    /** JSON Schema of the arguments object. */
    Map<String, Object> inputSchema();

    /**
     * @param arguments the "arguments" object of the tools/call request, never null
     * @return a value serializable to JSON
     * @throws IllegalArgumentException when the arguments are rejected
     */
    Object call(JsonNode arguments);
}
