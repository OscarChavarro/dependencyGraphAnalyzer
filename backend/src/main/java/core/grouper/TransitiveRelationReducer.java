package core.grouper;

import core.graph.SoftwarePackageGraph;

public class TransitiveRelationReducer {
    private final SoftwarePackageGraph graph;

    public TransitiveRelationReducer(SoftwarePackageGraph graph) {
        this.graph = graph;
    }

    public void reduce() {
        graph.removeTransitiveArcs();
    }
}
