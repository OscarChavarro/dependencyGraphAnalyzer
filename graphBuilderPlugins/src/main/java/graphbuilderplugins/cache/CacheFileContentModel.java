package graphbuilderplugins.cache;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Parsed content of a cache file: "n <name> <good|bad>" node lines and
 * "r <source> <destination>" relation lines, in file order.
 */
public record CacheFileContentModel(List<CacheNodeEntryModel> nodes, List<CacheRelationEntryModel> relations) {
    public static CacheFileContentModel empty() {
        return new CacheFileContentModel(List.of(), List.of());
    }

    /**
     * Names of every node the content produces once loaded into a graph: declared
     * nodes plus relation endpoints (missing endpoints are created as bad nodes).
     */
    public Set<String> resultingNodeNames() {
        Set<String> names = new LinkedHashSet<>();
        nodes.forEach(node -> names.add(node.name()));
        relations.forEach(relation -> {
            names.add(relation.source());
            names.add(relation.destination());
        });
        return names;
    }
}
