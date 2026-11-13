package backend.domain.model;

import java.util.List;

/**
 * A group definition file: header "[GROUP_NAME]" followed by member package names.
 *
 * @param groupName group name without brackets, e.g. "101_LOCAL"
 * @param members   package names in file order, without comments or blank lines
 */
public record GroupDefinitionModel(String groupName, List<String> members) {
}
