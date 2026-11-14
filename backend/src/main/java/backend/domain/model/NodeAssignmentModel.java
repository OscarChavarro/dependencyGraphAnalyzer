package backend.domain.model;

import java.util.List;

/**
 * Request to place nodes in a group.
 *
 * @param group    group name as "number_name", e.g. "23_solid_operators"
 * @param nodes    exact node names
 * @param prefixes every graph node whose name starts with one of these prefixes
 */
public record NodeAssignmentModel(String group, List<String> nodes, List<String> prefixes) {
}
