package backend.domain.model;

/**
 * Where "apt-get source PACKAGE" puts (or already put) the source of a binary package.
 *
 * @param sourcePackage           Debian source package name, e.g. "glibc" for libc6
 * @param sourceVersion           version without epoch, as in the .dsc file name
 * @param directoryName           folder dpkg-source unpacks to: SOURCE-UPSTREAMVERSION
 * @param allFilesDownloaded      true when apt-get reports no source file left to download
 */
public record AptSourceLocationModel(
        String sourcePackage,
        String sourceVersion,
        String directoryName,
        boolean allFilesDownloaded) {

    public String dscFileName() {
        return sourcePackage + "_" + sourceVersion + ".dsc";
    }
}
