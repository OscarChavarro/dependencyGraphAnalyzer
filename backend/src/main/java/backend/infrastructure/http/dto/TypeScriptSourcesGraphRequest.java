package backend.infrastructure.http.dto;

public record TypeScriptSourcesGraphRequest(
        String groupsDefinitionFolder,
        String[] inputFolders) {
}
