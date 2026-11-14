package backend.infrastructure.mcp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Small helpers to declare the JSON Schema of tool arguments. */
public final class McpSchema {
    private McpSchema() {
    }

    public static Map<String, Object> object(Map<String, Object> properties, List<String> required) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", required);
        return schema;
    }

    public static Map<String, Object> string(String description) {
        return typed("string", description);
    }

    public static Map<String, Object> integer(String description) {
        return typed("integer", description);
    }

    public static Map<String, Object> array(Map<String, Object> items, String description) {
        Map<String, Object> schema = typed("array", description);
        schema.put("items", items);
        return schema;
    }

    private static Map<String, Object> typed(String type, String description) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", type);
        schema.put("description", description);
        return schema;
    }
}
