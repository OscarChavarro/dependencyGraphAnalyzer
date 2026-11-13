package backend.infrastructure.http.dto;

public record LfsBuildGuideResponse(String guideFile, int buildSteps, int failedPackages) {
}
