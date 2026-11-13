package backend.domain.model;

import java.util.List;

/**
 * @param packages     packages to download, in order: sinks first, simplest group first
 * @param skippedNodes selected nodes that are not apt packages (e.g. "*local" nodes)
 */
public record LfsDownloadPlanModel(List<LfsPlannedPackageModel> packages, List<String> skippedNodes) {
}
