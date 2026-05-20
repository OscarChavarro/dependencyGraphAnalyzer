package backend.infrastructure.http.dto;

import backend.domain.model.GraphModelEdge;
import java.util.List;

public record EnrichedEdgesResponse(List<GraphModelEdge> enrichedEdges) {
}
