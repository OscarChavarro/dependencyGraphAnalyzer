package backend.domain.model;

/**
 * @param packageName name passed to "apt-get source" (architecture suffix removed)
 * @param groupName   group the package belongs to, or null when it is in no group
 */
public record LfsPlannedPackageModel(String packageName, String groupName) {
}
