package graphbuilderplugins.javasources.export;

import graphbuilderplugins.javasources.model.DependencyGraph;

public interface DependencyGraphExporter {
    String export(DependencyGraph graph);
}
