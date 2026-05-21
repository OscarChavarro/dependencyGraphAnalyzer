package graphbuilderplugins.api;

public interface GraphBuilderPlugin {
    GraphBuilderPluginId id();

    void build(GraphBuildTarget target);
}
