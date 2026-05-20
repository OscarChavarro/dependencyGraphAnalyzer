package backend.infrastructure.http.dto;

import backend.domain.model.GraphModelSnapshot;

public record UpdateGraphModelResponse(GraphModelSnapshot graphModel) {
}
