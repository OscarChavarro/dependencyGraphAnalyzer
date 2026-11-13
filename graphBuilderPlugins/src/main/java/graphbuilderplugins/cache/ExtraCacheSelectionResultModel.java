package graphbuilderplugins.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param accepted              extra nodes (and their relations) that can be appended to the graph
 * @param rejectedMissingDeps   rejected extra node name -> dependencies not available in the graph
 * @param undeclaredSources     relation sources in the extra file without a matching "n" line
 */
public record ExtraCacheSelectionResultModel(
        CacheFileContentModel accepted,
        Map<String, Set<String>> rejectedMissingDeps,
        List<String> undeclaredSources) {
}
