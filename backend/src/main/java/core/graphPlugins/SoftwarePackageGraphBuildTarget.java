package core.graphPlugins;

import core.graph.SoftwarePackageGraph;
import core.graph.SoftwarePackageNode;
import graphbuilderplugins.api.GraphBuildTarget;
import java.util.List;

public class SoftwarePackageGraphBuildTarget implements GraphBuildTarget {
    private final SoftwarePackageGraph graph;

    public SoftwarePackageGraphBuildTarget(SoftwarePackageGraph graph) {
        this.graph = graph;
    }

    @Override
    public void addNode(String name) {
        graph.addNode(name);
    }

    @Override
    public void addBadNode(String name) {
        graph.addBadNode(name);
    }

    @Override
    public void addDependency(String fromNodeName, String toNodeName) {
        SoftwarePackageNode from = graph.searchNodeByName(fromNodeName);
        SoftwarePackageNode to = graph.searchNodeByName(toNodeName);

        if (from == null) {
            graph.addBadNode(fromNodeName);
            from = graph.searchNodeByName(fromNodeName);
        }
        if (to == null) {
            graph.addBadNode(toNodeName);
            to = graph.searchNodeByName(toNodeName);
        }
        graph.addDependency(from, to);
    }

    @Override
    public List<String> listNodeNames() {
        return graph.getNodes().stream().map(SoftwarePackageNode::getName).toList();
    }
}
