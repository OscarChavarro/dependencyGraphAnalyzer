package backend.infrastructure.http.dto;

public record JavaProjectResponse(
        String id,
        String name,
        String groupsDefinitionFolder,
        String[] inputFolders,
        String[] classpath,
        String gradleProjectDir,
        String gradleModule,
        String gradleUserHome) {
}
