package backend.domain.model;

import java.nio.file.Path;
import java.util.List;

/**
 * A project of any catalog in etc/projects, normalized so a graph can be built from it.
 *
 * @param groupsDefinitionFolder absolute folder holding the group ".txt" files
 * @param inputs                 source folders, or cache files for {@code CACHE_LOADER}
 * @param classpath              classpath entries, only used by {@code JAVA_SOURCES}
 */
public record ProjectDescriptorModel(
        String id,
        String name,
        GraphModelGenerator generator,
        Path groupsDefinitionFolder,
        List<String> inputs,
        List<String> classpath) {
}
