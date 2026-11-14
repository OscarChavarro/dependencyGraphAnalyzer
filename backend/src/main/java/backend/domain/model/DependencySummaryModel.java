package backend.domain.model;

import java.util.List;
import java.util.Map;

/** Dependencies between containers (directories or packages) of a project graph. */
public record DependencySummaryModel(String projectId, List<Container> containers) {

    /**
     * @param groups node count per group; orphan nodes are counted under "999_orphans"
     * @param uses   relation count towards each other container
     * @param usedBy relation count from each other container
     */
    public record Container(
            String container,
            int nodeCount,
            Map<String, Integer> groups,
            Map<String, Integer> uses,
            Map<String, Integer> usedBy) {
    }
}
