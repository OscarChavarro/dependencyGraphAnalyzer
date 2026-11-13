package graphbuilderplugins.impl;

import graphbuilderplugins.api.GraphBuildTarget;
import graphbuilderplugins.api.GraphBuilderPlugin;
import graphbuilderplugins.api.GraphBuilderPluginId;
import graphbuilderplugins.cache.CacheFileContentModel;
import graphbuilderplugins.cache.CacheGraphComposer;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Loads a graph from cache files. The first input is the base cache, loaded as is.
 * Any further inputs are extra caches: their nodes are appended only when all of
 * their dependencies already exist (see {@link graphbuilderplugins.cache.ExtraCacheSelector}).
 */
public class CacheLoaderGraphBuilderPlugin implements GraphBuilderPlugin {
    private static final String DEFAULT_BASE_CACHE = "cache.txt";
    private static final String DEFAULT_EXTRA_CACHE = "cache_extra.txt";

    @Override
    public GraphBuilderPluginId id() {
        return GraphBuilderPluginId.CACHE_LOADER;
    }

    @Override
    public void build(GraphBuildTarget target) {
        build(target, List.of(DEFAULT_BASE_CACHE, DEFAULT_EXTRA_CACHE));
    }

    @Override
    public void build(GraphBuildTarget target, List<String> inputFiles) {
        List<Path> files = inputFiles == null ? List.of() : inputFiles.stream()
                .filter(Objects::nonNull)
                .filter(file -> !file.isBlank())
                .map(Path::of)
                .toList();
        if (files.isEmpty()) {
            build(target);
            return;
        }

        System.out.print("Building graph from cache ...");
        CacheFileContentModel content = CacheGraphComposer.compose(files.get(0), files.subList(1, files.size()));
        content.nodes().forEach(node -> {
            if (node.good()) {
                target.addNode(node.name());
            } else {
                target.addBadNode(node.name());
            }
        });
        content.relations().forEach(relation -> target.addDependency(relation.source(), relation.destination()));
        System.out.println("Ok!");
    }
}
