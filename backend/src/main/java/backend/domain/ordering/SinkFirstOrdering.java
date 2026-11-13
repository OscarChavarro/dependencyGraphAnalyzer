package backend.domain.ordering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Orders nodes from sinks to sources: a node is emitted only after every node it depends on.
 * Nodes are emitted by layers (all current sinks, sorted with the tie breaker). When the
 * remaining nodes form a cycle, the node with fewest pending dependencies (then tie breaker)
 * is emitted to break it. Dependencies outside {@code nodes} and self-dependencies are ignored.
 */
public final class SinkFirstOrdering {
    private SinkFirstOrdering() {
    }

    public static <T> List<T> order(
            Collection<T> nodes,
            Map<T, ? extends Collection<T>> dependencies,
            Comparator<T> tieBreaker) {
        Set<T> remaining = new LinkedHashSet<>(nodes);
        List<T> ordered = new ArrayList<>(remaining.size());

        while (!remaining.isEmpty()) {
            List<T> layer = new ArrayList<>();
            T leastBlocked = null;
            int leastBlockedCount = Integer.MAX_VALUE;
            for (T node : remaining) {
                int pending = countPendingDependencies(node, dependencies, remaining);
                if (pending == 0) {
                    layer.add(node);
                } else if (pending < leastBlockedCount
                        || (pending == leastBlockedCount && tieBreaker.compare(node, leastBlocked) < 0)) {
                    leastBlocked = node;
                    leastBlockedCount = pending;
                }
            }
            if (layer.isEmpty()) {
                layer.add(leastBlocked);
            }
            layer.sort(tieBreaker);
            ordered.addAll(layer);
            layer.forEach(remaining::remove);
        }
        return ordered;
    }

    private static <T> int countPendingDependencies(
            T node,
            Map<T, ? extends Collection<T>> dependencies,
            Set<T> remaining) {
        Collection<T> nodeDependencies = dependencies.get(node);
        if (nodeDependencies == null) {
            return 0;
        }
        int count = 0;
        for (T dependency : nodeDependencies) {
            if (!dependency.equals(node) && remaining.contains(dependency)) {
                count++;
            }
        }
        return count;
    }
}
