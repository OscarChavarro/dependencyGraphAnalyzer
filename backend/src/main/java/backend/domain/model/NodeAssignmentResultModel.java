package backend.domain.model;

import java.util.List;
import java.util.Map;

/**
 * @param assignedCountByGroup nodes now placed in each destination group
 * @param createdGroups        groups whose file did not exist before
 * @param unchangedCount       requested nodes that were already in their destination group
 */
public record NodeAssignmentResultModel(
        Map<String, Integer> assignedCountByGroup,
        List<String> createdGroups,
        int unchangedCount) {
}
