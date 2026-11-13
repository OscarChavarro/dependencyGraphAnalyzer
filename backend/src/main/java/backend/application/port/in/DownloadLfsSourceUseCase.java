package backend.application.port.in;

import backend.domain.model.LfsSourceDownloadResultModel;

public interface DownloadLfsSourceUseCase {
    /**
     * @param position 1-based index of this package in the client's plan, or 0 when unknown
     * @param total number of packages in the client's plan, or 0 when unknown
     */
    LfsSourceDownloadResultModel execute(String projectId, String packageName, int position, int total);
}
