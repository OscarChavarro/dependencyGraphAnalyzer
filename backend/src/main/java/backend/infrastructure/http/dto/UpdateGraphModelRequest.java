package backend.infrastructure.http.dto;

import backend.domain.model.GraphModelGenerator;

public record UpdateGraphModelRequest(
        GraphModelGenerator generator,
        String groupsDefinitionFolder,
        String[] inputFolders) {
}
