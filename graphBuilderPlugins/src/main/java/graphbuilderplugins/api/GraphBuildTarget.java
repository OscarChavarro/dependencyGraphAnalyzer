package graphbuilderplugins.api;

import java.util.List;

public interface GraphBuildTarget {
    void addNode(String name);

    void addBadNode(String name);

    void addDependency(String fromNodeName, String toNodeName);

    List<String> listNodeNames();

    default void saveCache(String filename) {
        // Optional capability: targets that support persistence can override.
    }
}
