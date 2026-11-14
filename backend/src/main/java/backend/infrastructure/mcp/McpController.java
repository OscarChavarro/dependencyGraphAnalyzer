package backend.infrastructure.mcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Model Context Protocol endpoint (Streamable HTTP transport, JSON responses only, stateless).
 * It serves tools; it does not open server-to-client streams.
 */
@RestController
@RequestMapping("/mcp")
public class McpController {
    private static final String DEFAULT_PROTOCOL_VERSION = "2025-03-26";
    private static final List<String> SUPPORTED_PROTOCOL_VERSIONS =
            List.of("2025-06-18", DEFAULT_PROTOCOL_VERSION, "2024-11-05");
    private static final int METHOD_NOT_FOUND = -32601;
    private static final int INVALID_REQUEST = -32600;
    private static final int INVALID_PARAMS = -32602;
    private static final String INSTRUCTIONS = "Tools to classify the nodes of a dependency graph project into"
            + " layered groups. Start with list_projects, then analyze_grouping. The criteria are in the"
            + " 'Group Definition Maintenance' section of the repository AGENTS.md.";

    private final Map<String, McpTool> toolsByName = new LinkedHashMap<>();
    private final ObjectMapper objectMapper;

    public McpController(List<McpTool> tools, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        tools.stream()
                .sorted((a, b) -> a.name().compareTo(b.name()))
                .forEach(tool -> toolsByName.put(tool.name(), tool));
    }

    @GetMapping
    public ResponseEntity<Void> rejectStream() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> handle(@RequestBody JsonNode message) {
        if (!message.isObject() || !message.path("method").isTextual()) {
            // batches, responses and malformed messages are not supported
            if (message.isObject() && !message.has("method")) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.badRequest()
                    .body(error(null, INVALID_REQUEST, "Expected a single JSON-RPC request object"));
        }
        JsonNode id = message.get("id");
        if (id == null || id.isNull()) {
            return ResponseEntity.accepted().build();
        }

        String method = message.get("method").asText();
        JsonNode params = message.path("params");
        return ResponseEntity.ok(switch (method) {
            case "initialize" -> result(id, initialize(params));
            case "ping" -> result(id, Map.of());
            case "tools/list" -> result(id, Map.of("tools", listTools()));
            case "tools/call" -> callTool(id, params);
            default -> error(id, METHOD_NOT_FOUND, "Method not found: " + method);
        });
    }

    private Map<String, Object> initialize(JsonNode params) {
        String requested = params.path("protocolVersion").asText("");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion",
                SUPPORTED_PROTOCOL_VERSIONS.contains(requested) ? requested : DEFAULT_PROTOCOL_VERSION);
        result.put("capabilities", Map.of("tools", Map.of()));
        result.put("serverInfo", Map.of("name", "dependency-graph-analyzer", "version", "0.666"));
        result.put("instructions", INSTRUCTIONS);
        return result;
    }

    private List<Map<String, Object>> listTools() {
        return toolsByName.values().stream()
                .map(tool -> {
                    Map<String, Object> descriptor = new LinkedHashMap<>();
                    descriptor.put("name", tool.name());
                    descriptor.put("description", tool.description());
                    descriptor.put("inputSchema", tool.inputSchema());
                    return descriptor;
                })
                .toList();
    }

    private Map<String, Object> callTool(JsonNode id, JsonNode params) {
        McpTool tool = toolsByName.get(params.path("name").asText(""));
        if (tool == null) {
            return error(id, INVALID_PARAMS, "Unknown tool: " + params.path("name").asText(""));
        }
        JsonNode arguments = params.path("arguments").isObject()
                ? params.get("arguments")
                : JsonNodeFactory.instance.objectNode();
        try {
            return result(id, toolResult(objectMapper.writeValueAsString(tool.call(arguments)), false));
        } catch (IllegalArgumentException e) {
            return result(id, toolResult(e.getMessage(), true));
        } catch (JsonProcessingException | RuntimeException e) {
            System.err.println("ERROR mcp: tool '" + tool.name() + "' failed: " + e);
            return result(id, toolResult("Tool '" + tool.name() + "' failed: " + e.getMessage(), true));
        }
    }

    private Map<String, Object> toolResult(String text, boolean isError) {
        return Map.of("content", List.of(Map.of("type", "text", "text", String.valueOf(text))), "isError", isError);
    }

    private Map<String, Object> result(JsonNode id, Object result) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("result", result);
        return response;
    }

    private Map<String, Object> error(JsonNode id, int code, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("error", Map.of("code", code, "message", message));
        return response;
    }
}
