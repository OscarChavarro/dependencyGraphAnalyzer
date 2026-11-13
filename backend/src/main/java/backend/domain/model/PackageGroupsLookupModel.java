package backend.domain.model;

import java.util.List;

/**
 * Result of looking up a package in the group definition files.
 *
 * @param packageName package name as written in the group files (may differ in case from the query);
 *                    the query itself when no group contains it
 * @param groups      group names without brackets (e.g. "101_LOCAL"), in file order; empty when not found
 */
public record PackageGroupsLookupModel(String packageName, List<String> groups) {
}
