package backend.infrastructure.http.dto;

public record JavaSourcesGraphRequest(
        String groupsDefinitionFolder,
        String[] inputFolders) {
}
