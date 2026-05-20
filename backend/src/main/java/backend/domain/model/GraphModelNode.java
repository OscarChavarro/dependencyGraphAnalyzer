package backend.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record GraphModelNode(
        String name,
        boolean isBad,
        boolean marked,
        boolean group,
        boolean sink,
        @JsonProperty("s") boolean source,
        boolean variant,
        boolean boldName) {
}
