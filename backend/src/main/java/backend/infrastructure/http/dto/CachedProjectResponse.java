package backend.infrastructure.http.dto;

public record CachedProjectResponse(
        String id,
        String name,
        String groupsDefinitionFolder,
        String cacheFile) {
}
