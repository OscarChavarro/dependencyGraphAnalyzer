package co.cubestudio.graph;

import co.cubestudio.SoftwarePackageNode;
import java.util.Set;

public interface DirectedPackageGraphOperations {
    void addVertex(SoftwarePackageNode node);

    boolean addEdge(SoftwarePackageNode from, SoftwarePackageNode to);

    boolean removeEdge(SoftwarePackageNode from, SoftwarePackageNode to);

    boolean containsEdge(SoftwarePackageNode from, SoftwarePackageNode to);

    Set<SoftwarePackageNode> outgoingOf(SoftwarePackageNode node);

    Set<SoftwarePackageNode> incomingOf(SoftwarePackageNode node);

    int inDegreeOf(SoftwarePackageNode node);

    int outDegreeOf(SoftwarePackageNode node);

    Set<PackageEdge> edges();

    boolean hasPath(SoftwarePackageNode from, SoftwarePackageNode to);
}
