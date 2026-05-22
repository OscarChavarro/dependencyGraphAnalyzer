package backend.infrastructure.http.dto;

public record TypeScriptProjectResponse(
        String id,
        String name,
        String groupsDefinitionFolder,
        String[] inputFolders) {
}
