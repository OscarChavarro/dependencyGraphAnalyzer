package backend.domain.model;

/**
 * One processed package of an LFS run, in download (= build) order.
 *
 * @param groupName     group name without brackets, or null
 * @param sourcePackage null when the source could not be resolved (e.g. FAILED)
 * @param sourceVersion null when the source could not be resolved
 * @param message       failure detail; empty otherwise
 */
public record LfsBuildGuideEntryModel(
        String packageName,
        String groupName,
        LfsSourceDownloadStatus status,
        String sourcePackage,
        String sourceVersion,
        String message) {
}
