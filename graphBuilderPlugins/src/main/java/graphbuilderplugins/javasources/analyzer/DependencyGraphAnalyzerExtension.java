package graphbuilderplugins.javasources.analyzer;

import graphbuilderplugins.javasources.model.DependencyGraph;

public interface DependencyGraphAnalyzerExtension {
    default void beforeAnalysis(DependencyGraphAnalysisRequest request) {
    }

    default void afterAnalysis(DependencyGraphAnalysisRequest request, DependencyGraph graph) {
    }
}
