package backend.application.service;

import backend.application.port.in.WriteLfsBuildGuideUseCase;
import backend.domain.model.DscControlFieldsModel;
import backend.domain.model.LfsBuildGuideEntryModel;
import backend.domain.model.LfsBuildGuideModel;
import backend.domain.model.LfsBuildGuideResultModel;
import backend.domain.model.LfsBuildStepModel;
import backend.domain.model.LfsSourceDownloadStatus;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Turns the ordered outcomes of an LFS run into build steps (one per source package, in the order
 * its first binary appeared) and writes them as AGENTS.md in the linuxFromScratch folder.
 */
@Service
public class WriteLfsBuildGuideService implements WriteLfsBuildGuideUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(WriteLfsBuildGuideService.class);
    static final String GUIDE_FILE_NAME = "AGENTS.md";
    private static final Pattern SOURCE_VERSION = Pattern.compile("^[A-Za-z0-9.+~\\-]+$");

    private final LfsFolderResolver lfsFolderResolver;
    private final DscFileReader dscFileReader;
    private final LfsBuildGuideRenderer renderer;

    public WriteLfsBuildGuideService(
            LfsFolderResolver lfsFolderResolver,
            DscFileReader dscFileReader,
            LfsBuildGuideRenderer renderer) {
        this.lfsFolderResolver = lfsFolderResolver;
        this.dscFileReader = dscFileReader;
        this.renderer = renderer;
    }

    @Override
    public LfsBuildGuideResultModel execute(String projectId, List<LfsBuildGuideEntryModel> entries) {
        Path folder = lfsFolderResolver.resolveAndCreate(projectId);
        List<LfsBuildGuideEntryModel> failed = new ArrayList<>();
        Map<String, StepAccumulator> stepsBySource = new LinkedHashMap<>();
        for (LfsBuildGuideEntryModel entry : entries) {
            validate(entry);
            if (entry.status() == LfsSourceDownloadStatus.FAILED || entry.sourcePackage() == null) {
                failed.add(entry);
                continue;
            }
            stepsBySource
                    .computeIfAbsent(entry.sourcePackage() + "_" + entry.sourceVersion(),
                            key -> new StepAccumulator(entry.sourcePackage(), entry.sourceVersion()))
                    .add(entry);
        }

        List<LfsBuildStepModel> steps = new ArrayList<>();
        for (StepAccumulator accumulator : stepsBySource.values()) {
            steps.add(accumulator.toStep(steps.size() + 1, folder, dscFileReader));
        }
        LfsBuildGuideModel guide = new LfsBuildGuideModel(projectId, LocalDateTime.now(), steps, failed);
        Path guideFile = folder.resolve(GUIDE_FILE_NAME);
        writeAtomically(guideFile, renderer.render(guide));
        LOG.info("LFS build guide written: file={}, buildSteps={}, failedPackages={}",
                guideFile, steps.size(), failed.size());
        return new LfsBuildGuideResultModel(guideFile, steps.size(), failed.size());
    }

    private static void validate(LfsBuildGuideEntryModel entry) {
        if (entry.packageName() == null || !DownloadLfsSourceService.APT_PACKAGE_NAME.matcher(entry.packageName()).matches()) {
            throw new IllegalArgumentException("Invalid apt package name: '" + entry.packageName() + "'");
        }
        if (entry.status() == null) {
            throw new IllegalArgumentException("Missing status for package '" + entry.packageName() + "'");
        }
        if (entry.sourcePackage() != null
                && (!DownloadLfsSourceService.APT_PACKAGE_NAME.matcher(entry.sourcePackage()).matches()
                || entry.sourceVersion() == null
                || !SOURCE_VERSION.matcher(entry.sourceVersion()).matches())) {
            throw new IllegalArgumentException("Invalid source package for '" + entry.packageName() + "': '"
                    + entry.sourcePackage() + "' version '" + entry.sourceVersion() + "'");
        }
    }

    private static void writeAtomically(Path target, String content) {
        try {
            Path temporary = Files.createTempFile(target.getParent(), GUIDE_FILE_NAME, ".tmp");
            Files.writeString(temporary, content, StandardCharsets.UTF_8);
            Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write " + target, e);
        }
    }

    private static final class StepAccumulator {
        private final String sourcePackage;
        private final String sourceVersion;
        private final Set<String> groups = new LinkedHashSet<>();
        private final Set<String> selectedPackages = new LinkedHashSet<>();

        private StepAccumulator(String sourcePackage, String sourceVersion) {
            this.sourcePackage = sourcePackage;
            this.sourceVersion = sourceVersion;
        }

        private void add(LfsBuildGuideEntryModel entry) {
            selectedPackages.add(entry.packageName());
            if (entry.groupName() != null && !entry.groupName().isBlank()) {
                groups.add(entry.groupName());
            }
        }

        private LfsBuildStepModel toStep(int stepNumber, Path folder, DscFileReader dscFileReader) {
            String directoryName = sourcePackage + "-" + AptSourceLocator.upstreamVersion(sourceVersion);
            DscControlFieldsModel dsc = dscFileReader.read(folder.resolve(sourcePackage + "_" + sourceVersion + ".dsc"));
            return new LfsBuildStepModel(
                    stepNumber,
                    sourcePackage,
                    sourceVersion,
                    directoryName,
                    Files.isDirectory(folder.resolve(directoryName)),
                    List.copyOf(groups),
                    List.copyOf(selectedPackages),
                    dsc.binaries(),
                    dsc.buildDepends());
        }
    }
}
