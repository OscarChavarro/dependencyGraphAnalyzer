package backend.domain.model;

/**
 * @param message         empty on success; tail of the apt-get output on failure
 * @param sourcePackage   source package name, or null when it could not be resolved
 * @param sourceVersion   source version without epoch, or null when unknown
 * @param sourceDirectory unpacked source folder name, or null when unknown
 */
public record LfsSourceDownloadResultModel(
        String packageName,
        LfsSourceDownloadStatus status,
        int exitCode,
        String message,
        String sourcePackage,
        String sourceVersion,
        String sourceDirectory) {

    public boolean success() {
        return status != LfsSourceDownloadStatus.FAILED;
    }

    public static LfsSourceDownloadResultModel failed(String packageName, int exitCode, String message) {
        return new LfsSourceDownloadResultModel(
                packageName, LfsSourceDownloadStatus.FAILED, exitCode, message, null, null, null);
    }

    public static LfsSourceDownloadResultModel succeeded(
            String packageName, LfsSourceDownloadStatus status, AptSourceLocationModel location) {
        return new LfsSourceDownloadResultModel(
                packageName,
                status,
                0,
                "",
                location == null ? null : location.sourcePackage(),
                location == null ? null : location.sourceVersion(),
                location == null ? null : location.directoryName());
    }
}
