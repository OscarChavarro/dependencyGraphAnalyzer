package backend.domain.model;

/** A dependency between two nodes: {@code from} depends on {@code to}. */
public record NodeRelationModel(String from, String to) {
}
