package backend.infrastructure.http.dto;

/**
 * @param status          DOWNLOADED, ALREADY_PRESENT or FAILED
 * @param sourcePackage   null when the source could not be resolved
 * @param sourceVersion   null when the source could not be resolved
 * @param sourceDirectory null when the source could not be resolved
 */
public record LfsDownloadSourceResponse(
        String packageName,
        boolean success,
        String status,
        int exitCode,
        String message,
        String sourcePackage,
        String sourceVersion,
        String sourceDirectory) {
}
