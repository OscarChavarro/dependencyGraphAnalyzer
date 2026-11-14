package backend.domain.model;

import java.util.List;

/** Direct dependencies and clients of one node, each with its group. */
public record NodeDescriptionModel(String node, String group, List<Neighbor> uses, List<Neighbor> usedBy) {

    /** @param backward true when the relation with this neighbor is drawn red in the structure graph */
    public record Neighbor(String node, String group, boolean backward) {
    }
}
