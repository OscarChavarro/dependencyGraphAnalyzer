package backend.domain.model;

import java.nio.file.Path;

public record LfsBuildGuideResultModel(Path guideFile, int buildSteps, int failedPackages) {
}
