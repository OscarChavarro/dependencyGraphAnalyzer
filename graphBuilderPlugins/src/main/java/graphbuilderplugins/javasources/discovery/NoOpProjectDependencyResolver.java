package graphbuilderplugins.javasources.discovery;

import java.nio.file.Path;
import java.util.List;

public final class NoOpProjectDependencyResolver implements ProjectDependencyResolverPort {
    @Override
    public List<Path> resolveClasspathEntries(List<Path> sourceFolders) {
        return List.of();
    }

    @Override
    public List<Path> resolveSourcepathEntries(List<Path> sourceFolders) {
        return List.of();
    }
}
