package graphbuilderplugins.javasources.analyzer;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class DependencyGraphAnalysisRequest {
    private final List<Path> sourceFolders;
    private final List<Path> classpathEntries;
    private final List<Path> sourcepathEntries;
    private final boolean includeModuleInfo;

    public DependencyGraphAnalysisRequest(
            List<Path> sourceFolders,
            List<Path> classpathEntries,
            List<Path> sourcepathEntries,
            boolean includeModuleInfo
    ) {
        this.sourceFolders = List.copyOf(Objects.requireNonNull(sourceFolders, "sourceFolders must not be null"));
        this.classpathEntries = List.copyOf(Objects.requireNonNull(classpathEntries, "classpathEntries must not be null"));
        this.sourcepathEntries = List.copyOf(Objects.requireNonNull(sourcepathEntries, "sourcepathEntries must not be null"));
        this.includeModuleInfo = includeModuleInfo;
    }

    public static DependencyGraphAnalysisRequest ofSourceFolders(List<Path> sourceFolders) {
        return new DependencyGraphAnalysisRequest(sourceFolders, List.of(), List.of(), false);
    }

    public static DependencyGraphAnalysisRequest of(List<Path> sourceFolders, List<Path> classpathEntries) {
        return new DependencyGraphAnalysisRequest(sourceFolders, classpathEntries, List.of(), false);
    }

    public List<Path> sourceFolders() {
        return sourceFolders;
    }

    public List<Path> classpathEntries() {
        return classpathEntries;
    }

    public List<Path> sourcepathEntries() {
        return sourcepathEntries;
    }

    public boolean includeModuleInfo() {
        return includeModuleInfo;
    }
}
