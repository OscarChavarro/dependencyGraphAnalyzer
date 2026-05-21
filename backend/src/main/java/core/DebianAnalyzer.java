package core;

//===========================================================================

import common.ProcessControl;
import core.graph.SoftwarePackageGraph;
import core.graphPlugins.SoftwarePackageGraphBuildTarget;
import core.grouper.GroupSubgraphGrouper;
import core.grouper.SoftwarePackageGroup;
import core.grouper.TransitiveRelationReducer;
import core.highlight.MarkerJedilink;
import core.highlight.Markers;
import graphbuilderplugins.api.GraphBuilderPluginId;
import graphbuilderplugins.api.GraphBuilderPluginRegistry;
import java.util.ArrayList;

import java.io.File;

//===========================================================================

public class DebianAnalyzer {
    private final SoftwarePackageGraph graph;
    private final GraphBuilderPluginRegistry pluginRegistry;
    private ArrayList<SoftwarePackageGroup> groups;
    private String outputFile;

    public DebianAnalyzer() {
        graph = new SoftwarePackageGraph();
        pluginRegistry = new GraphBuilderPluginRegistry();
        outputFile = "./output/general.dot";
    }

    private void exec(String args[], OutputFormats outputFormat, GraphBuilderPluginId pluginId) {
        GroupSubgraphGrouper groupSubgraphGrouper = new GroupSubgraphGrouper(graph);
        TransitiveRelationReducer transitiveRelationReducer = new TransitiveRelationReducer(graph);

        pluginRegistry.require(pluginId).build(new SoftwarePackageGraphBuildTarget(graph));

        //----------------------------------------------------------------
        int i;
        SoftwarePackageGroup g;

        groups = new ArrayList<SoftwarePackageGroup>();
        for (i = 0; i < args.length; i++) {
            g = new SoftwarePackageGroup(args[i], graph);
            if (g.header != null) {
                groups.add(g);
            }
        }

        //----------------------------------------------------------------
        graph.initNodesAnotation();
        new Markers(graph);

        MarkerJedilink mj = new MarkerJedilink(graph);
        mj.markCustomJedilink();

        groupSubgraphGrouper.groupBySubgraphs(groups);
        transitiveRelationReducer.reduce();

        //----------------------------------------------------------------
        graph.exportDot(outputFile, groups);
        System.out.println("Graphviz graph has been generated on " + outputFile + "!");
        String eargs[] = new String[6];

        eargs[0] = "/usr/bin/dot";
        eargs[1] = "-Gnodesep=1";
        eargs[2] = "-Tdia";
        eargs[3] = outputFile;
        eargs[4] = "-o";
        eargs[5] = "./output/general.dia";

        Process proceso = ProcessControl.system(eargs);
        ProcessControl.finalizeProcess(proceso);

        //----------------------------------------------------------------
        System.out.print("Exporting individual groups: ");
        if (!groups.isEmpty()) {
            graph.labelLastGroup(groups);
            graph.exportDotByGroups(groups, outputFormat);
            if (outputFormat == OutputFormats.SVG) {
                graph.exportDotByGroups(groups, OutputFormats.PNG);
            } else if (outputFormat == OutputFormats.PNG) {
                graph.exportDotByGroups(groups, OutputFormats.SVG);
            }
            graph.exportGroups(groups);
            graph.exportCleanScripts(groups);
            graph.exportInstallScripts(groups);
            graph.exportTopNodesPerGroups(groups);
        }
        System.out.println(" Ok!");
    }

    public void run(String[] args) {
        run(args, OutputFormats.PNG);
    }

    public void run(String[] args, OutputFormats outputFormat) {
        exec(args, outputFormat, GraphBuilderPluginId.DEBIAN_PACKAGE_GENERATOR);
    }

    public void runFromCache(String[] args, OutputFormats outputFormat) {
        exec(args, outputFormat, GraphBuilderPluginId.CACHE_LOADER);
    }

    public void runFromDebian(String[] args, OutputFormats outputFormat) {
        exec(args, outputFormat, GraphBuilderPluginId.DEBIAN_PACKAGE_GENERATOR);
    }

    public SoftwarePackageGraph getGraph() {
        return graph;
    }
}
