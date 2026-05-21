package graphbuilderplugins.api;

import java.util.List;

public interface GraphBuilderPlugin {
    GraphBuilderPluginId id();

    void build(GraphBuildTarget target);

    default void build(GraphBuildTarget target, List<String> inputFolders) {
        build(target);
    }
}
