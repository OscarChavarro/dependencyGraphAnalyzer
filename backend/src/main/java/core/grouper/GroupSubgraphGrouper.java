package core.grouper;

import core.graph.SoftwarePackageGraph;
import java.util.ArrayList;

public class GroupSubgraphGrouper {
    private final SoftwarePackageGraph graph;

    public GroupSubgraphGrouper(SoftwarePackageGraph graph) {
        this.graph = graph;
    }

    public void groupBySubgraphs(ArrayList<SoftwarePackageGroup> groups) {
        graph.reportNodesOutsideGroups();

        for (SoftwarePackageGroup group : groups) {
            graph.cleangroup(group.list, group.header);
        }

        graph.addGroupNodes(groups);
        graph.anotateGroups();
    }
}
