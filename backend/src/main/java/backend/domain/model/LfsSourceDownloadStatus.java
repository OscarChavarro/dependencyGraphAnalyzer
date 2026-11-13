package backend.domain.model;

/** Outcome of preparing the source of one package for the LFS build. */
public enum LfsSourceDownloadStatus {
    /** apt-get source ran and succeeded. */
    DOWNLOADED,
    /** Every source file and the unpacked directory already existed; apt-get source was not run. */
    ALREADY_PRESENT,
    /** apt-get source failed or could not run. */
    FAILED
}
