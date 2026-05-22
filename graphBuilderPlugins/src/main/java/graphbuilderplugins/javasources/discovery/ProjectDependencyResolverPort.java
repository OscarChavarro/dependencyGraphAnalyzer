package graphbuilderplugins.javasources.discovery;

import java.nio.file.Path;
import java.util.List;

public interface ProjectDependencyResolverPort {
    List<Path> resolveClasspathEntries(List<Path> sourceFolders);

    List<Path> resolveSourcepathEntries(List<Path> sourceFolders);
}
