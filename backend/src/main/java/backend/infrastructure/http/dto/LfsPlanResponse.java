package backend.infrastructure.http.dto;

import java.util.List;

public record LfsPlanResponse(List<LfsPlannedPackageResponse> packages, List<String> skippedNodes) {
    public record LfsPlannedPackageResponse(String packageName, String groupName) {
    }
}
