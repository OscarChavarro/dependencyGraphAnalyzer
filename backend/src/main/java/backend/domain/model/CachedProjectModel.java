package backend.domain.model;

/** One entry of etc/projects/cachedProjects.json. Optional fields are null when absent. */
public record CachedProjectModel(
        String id,
        String name,
        String groupsDefinitionFolder,
        String cacheFile,
        String extra,
        String linuxFromScratch) {
}
