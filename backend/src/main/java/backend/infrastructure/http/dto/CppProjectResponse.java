package backend.infrastructure.http.dto;

public record CppProjectResponse(
        String id,
        String name,
        String groupsDefinitionFolder,
        String[] inputFolders) {
}
