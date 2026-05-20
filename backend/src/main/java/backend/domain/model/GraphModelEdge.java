package backend.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GraphModelEdge(@JsonProperty("s") String source, @JsonProperty("t") String target) {
}
