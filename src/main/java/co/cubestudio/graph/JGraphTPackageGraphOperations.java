package co.cubestudio.graph;

import co.cubestudio.SoftwarePackageNode;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class JGraphTPackageGraphOperations implements DirectedPackageGraphOperations {
    private final Graph<SoftwarePackageNode, DefaultEdge> graph;

    public JGraphTPackageGraphOperations() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    @Override
    public void addVertex(SoftwarePackageNode node) {
        graph.addVertex(node);
    }

    @Override
    public boolean addEdge(SoftwarePackageNode from, SoftwarePackageNode to) {
        if (graph.containsEdge(from, to)) {
            return false;
        }
        return graph.addEdge(from, to) != null;
    }

    @Override
    public boolean removeEdge(SoftwarePackageNode from, SoftwarePackageNode to) {
        DefaultEdge edge = graph.getEdge(from, to);
        if (edge == null) {
            return false;
        }
        graph.removeEdge(edge);
        return true;
    }

    @Override
    public boolean containsEdge(SoftwarePackageNode from, SoftwarePackageNode to) {
        return graph.containsEdge(from, to);
    }

    @Override
    public Set<SoftwarePackageNode> outgoingOf(SoftwarePackageNode node) {
        Set<SoftwarePackageNode> result = new LinkedHashSet<>();
        for (DefaultEdge edge : graph.outgoingEdgesOf(node)) {
            result.add(graph.getEdgeTarget(edge));
        }
        return result;
    }

    @Override
    public Set<SoftwarePackageNode> incomingOf(SoftwarePackageNode node) {
        Set<SoftwarePackageNode> result = new LinkedHashSet<>();
        for (DefaultEdge edge : graph.incomingEdgesOf(node)) {
            result.add(graph.getEdgeSource(edge));
        }
        return result;
    }

    @Override
    public int inDegreeOf(SoftwarePackageNode node) {
        return graph.inDegreeOf(node);
    }

    @Override
    public int outDegreeOf(SoftwarePackageNode node) {
        return graph.outDegreeOf(node);
    }

    @Override
    public Set<PackageEdge> edges() {
        Set<PackageEdge> result = new LinkedHashSet<>();
        for (DefaultEdge edge : graph.edgeSet()) {
            result.add(new PackageEdge(graph.getEdgeSource(edge), graph.getEdgeTarget(edge)));
        }
        return result;
    }

    @Override
    public boolean hasPath(SoftwarePackageNode from, SoftwarePackageNode to) {
        return BFSShortestPath.findPathBetween(graph, from, to) != null;
    }
}
