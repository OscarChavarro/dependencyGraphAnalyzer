package backend.domain.model;

import java.util.List;

/**
 * One source package to build, with everything an agent needs for that step.
 *
 * @param producedBinaries every binary package the source builds (Binary field of the .dsc)
 * @param buildDepends     Build-Depends, Build-Depends-Arch and Build-Depends-Indep of the .dsc
 */
public record LfsBuildStepModel(
        int step,
        String sourcePackage,
        String sourceVersion,
        String directoryName,
        boolean directoryPresent,
        List<String> groups,
        List<String> selectedPackages,
        List<String> producedBinaries,
        String buildDepends) {
}
