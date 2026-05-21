package graphbuilderplugins.api;

import graphbuilderplugins.impl.CacheLoaderGraphBuilderPlugin;
import graphbuilderplugins.impl.DebianPackageGraphBuilderPlugin;
import java.util.EnumMap;
import java.util.Map;

public class GraphBuilderPluginRegistry {
    private final Map<GraphBuilderPluginId, GraphBuilderPlugin> plugins;

    public GraphBuilderPluginRegistry() {
        plugins = new EnumMap<>(GraphBuilderPluginId.class);
        register(new CacheLoaderGraphBuilderPlugin());
        register(new DebianPackageGraphBuilderPlugin());
    }

    public GraphBuilderPlugin require(GraphBuilderPluginId id) {
        GraphBuilderPlugin plugin = plugins.get(id);
        if (plugin == null) {
            throw new IllegalArgumentException("No graph builder plugin registered for id: " + id);
        }
        return plugin;
    }

    public void register(GraphBuilderPlugin plugin) {
        plugins.put(plugin.id(), plugin);
    }
}
