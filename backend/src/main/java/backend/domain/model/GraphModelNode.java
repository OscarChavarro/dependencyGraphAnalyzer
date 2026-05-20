package backend.domain.model;

public record GraphModelNode(
        String name,
        boolean isBad,
        boolean marked,
        boolean group,
        boolean sink,
        boolean source,
        boolean variant,
        boolean boldName) {
}
