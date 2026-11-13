package graphbuilderplugins.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Decides which nodes of an extra cache file can be appended to an already loaded graph.
 * An extra node is accepted only when every one of its dependencies already exists in the
 * graph or is another accepted extra node. Rejections propagate: if an extra node is
 * rejected, extra nodes depending on it are rejected too (greatest fixpoint, so mutually
 * dependent extras whose external dependencies exist are accepted).
 */
public final class ExtraCacheSelector {
    private ExtraCacheSelector() {
    }

    public static ExtraCacheSelectionResultModel select(CacheFileContentModel extra, Set<String> existingNodeNames) {
        Map<String, CacheNodeEntryModel> candidates = new LinkedHashMap<>();
        extra.nodes().forEach(node -> candidates.putIfAbsent(node.name(), node));

        Map<String, Set<String>> dependenciesBySource = new LinkedHashMap<>();
        Set<String> undeclaredSources = new LinkedHashSet<>();
        for (CacheRelationEntryModel relation : extra.relations()) {
            if (!candidates.containsKey(relation.source())) {
                undeclaredSources.add(relation.source());
                continue;
            }
            dependenciesBySource
                    .computeIfAbsent(relation.source(), ignored -> new LinkedHashSet<>())
                    .add(relation.destination());
        }

        Set<String> remaining = new LinkedHashSet<>(candidates.keySet());
        Map<String, Set<String>> rejected = new LinkedHashMap<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String candidate : List.copyOf(remaining)) {
                Set<String> missing = new LinkedHashSet<>();
                for (String dependency : dependenciesBySource.getOrDefault(candidate, Set.of())) {
                    if (!existingNodeNames.contains(dependency) && !remaining.contains(dependency)) {
                        missing.add(dependency);
                    }
                }
                if (!missing.isEmpty()) {
                    remaining.remove(candidate);
                    rejected.put(candidate, missing);
                    changed = true;
                }
            }
        }

        List<CacheNodeEntryModel> acceptedNodes = new ArrayList<>();
        remaining.forEach(name -> acceptedNodes.add(candidates.get(name)));
        List<CacheRelationEntryModel> acceptedRelations = extra.relations().stream()
                .filter(relation -> remaining.contains(relation.source()))
                .toList();

        return new ExtraCacheSelectionResultModel(
                new CacheFileContentModel(List.copyOf(acceptedNodes), acceptedRelations),
                rejected,
                List.copyOf(undeclaredSources));
    }
}
