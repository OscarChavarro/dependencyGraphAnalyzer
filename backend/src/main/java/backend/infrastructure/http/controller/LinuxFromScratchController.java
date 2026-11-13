package backend.infrastructure.http.controller;

import backend.application.port.in.DownloadLfsSourceUseCase;
import backend.application.port.in.PlanLfsSourceDownloadUseCase;
import backend.application.port.in.WriteLfsBuildGuideUseCase;
import backend.domain.model.LfsBuildGuideEntryModel;
import backend.domain.model.LfsBuildGuideResultModel;
import backend.domain.model.LfsDownloadPlanModel;
import backend.domain.model.LfsSourceDownloadResultModel;
import backend.domain.model.LfsSourceDownloadStatus;
import backend.infrastructure.http.dto.LfsBuildGuideRequest;
import backend.infrastructure.http.dto.LfsBuildGuideResponse;
import backend.infrastructure.http.dto.LfsDownloadSourceRequest;
import backend.infrastructure.http.dto.LfsDownloadSourceResponse;
import backend.infrastructure.http.dto.LfsPlanRequest;
import backend.infrastructure.http.dto.LfsPlanResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/lfs")
public class LinuxFromScratchController {
    private final PlanLfsSourceDownloadUseCase planLfsSourceDownloadUseCase;
    private final DownloadLfsSourceUseCase downloadLfsSourceUseCase;
    private final WriteLfsBuildGuideUseCase writeLfsBuildGuideUseCase;

    public LinuxFromScratchController(
            PlanLfsSourceDownloadUseCase planLfsSourceDownloadUseCase,
            DownloadLfsSourceUseCase downloadLfsSourceUseCase,
            WriteLfsBuildGuideUseCase writeLfsBuildGuideUseCase) {
        this.planLfsSourceDownloadUseCase = planLfsSourceDownloadUseCase;
        this.downloadLfsSourceUseCase = downloadLfsSourceUseCase;
        this.writeLfsBuildGuideUseCase = writeLfsBuildGuideUseCase;
    }

    @PostMapping({"/plan"})
    public LfsPlanResponse plan(@Valid @RequestBody LfsPlanRequest request) {
        LfsDownloadPlanModel plan = planLfsSourceDownloadUseCase.execute(request.projectId(), request.selectedNodes());
        return new LfsPlanResponse(
                plan.packages().stream()
                        .map(entry -> new LfsPlanResponse.LfsPlannedPackageResponse(entry.packageName(), entry.groupName()))
                        .toList(),
                plan.skippedNodes());
    }

    @PostMapping({"/downloadSource"})
    public LfsDownloadSourceResponse downloadSource(@Valid @RequestBody LfsDownloadSourceRequest request) {
        LfsSourceDownloadResultModel result = downloadLfsSourceUseCase.execute(
                request.projectId(), request.packageName(), request.positionOrZero(), request.totalOrZero());
        return new LfsDownloadSourceResponse(
                result.packageName(),
                result.success(),
                result.status().name(),
                result.exitCode(),
                result.message(),
                result.sourcePackage(),
                result.sourceVersion(),
                result.sourceDirectory());
    }

    @PostMapping({"/buildGuide"})
    public LfsBuildGuideResponse buildGuide(@Valid @RequestBody LfsBuildGuideRequest request) {
        List<LfsBuildGuideEntryModel> entries = request.entries().stream()
                .map(entry -> new LfsBuildGuideEntryModel(
                        entry.packageName().trim(),
                        blankToNull(entry.groupName()),
                        LfsSourceDownloadStatus.valueOf(entry.status()),
                        blankToNull(entry.sourcePackage()),
                        blankToNull(entry.sourceVersion()),
                        entry.message() == null ? "" : entry.message()))
                .toList();
        LfsBuildGuideResultModel result = writeLfsBuildGuideUseCase.execute(request.projectId(), entries);
        return new LfsBuildGuideResponse(result.guideFile().toString(), result.buildSteps(), result.failedPackages());
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
