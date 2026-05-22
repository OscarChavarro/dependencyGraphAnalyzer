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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import core.graph.SoftwarePackageNode;

//===========================================================================

public class DebianAnalyzer {
    private static final String ORPHANS_GROUP_HEADER = "[999_orphans]";
    private static final String ORPHANS_GROUP_FILE = "999_orphans.txt";
    private final SoftwarePackageGraph graph;
    private final GraphBuilderPluginRegistry pluginRegistry;
    private ArrayList<SoftwarePackageGroup> groups;
    private String outputFile;

    public DebianAnalyzer() {
        graph = new SoftwarePackageGraph();
        pluginRegistry = new GraphBuilderPluginRegistry();
        outputFile = "./output/general.dot";
    }

    private void exec(
            String args[],
            OutputFormats outputFormat,
            GraphBuilderPluginId pluginId,
            String[] inputFolders,
            String[] classpathEntries) {
        GroupSubgraphGrouper groupSubgraphGrouper = new GroupSubgraphGrouper(graph);
        TransitiveRelationReducer transitiveRelationReducer = new TransitiveRelationReducer(graph);

        List<String> pluginInputFolders = inputFolders == null ? List.of() : Arrays.asList(inputFolders);
        List<String> pluginClasspathEntries = classpathEntries == null ? List.of() : Arrays.asList(classpathEntries);
        pluginRegistry.require(pluginId).build(
                new SoftwarePackageGraphBuildTarget(graph),
                pluginInputFolders,
                pluginClasspathEntries);

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
        captureOrphanedNodes(args);

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
        exec(args, outputFormat, GraphBuilderPluginId.DEBIAN_PACKAGE_GENERATOR, null, null);
    }

    public void runFromCache(String[] args, OutputFormats outputFormat, String[] inputFolders) {
        exec(args, outputFormat, GraphBuilderPluginId.CACHE_LOADER, inputFolders, null);
    }

    public void runFromDebian(String[] args, OutputFormats outputFormat) {
        exec(args, outputFormat, GraphBuilderPluginId.DEBIAN_PACKAGE_GENERATOR, null, null);
    }

    public void runFromCppSources(String[] args, OutputFormats outputFormat, String[] inputFolders) {
        exec(args, outputFormat, GraphBuilderPluginId.CPP_SOURCES, inputFolders, null);
    }

    public void runFromJavaSources(
            String[] args,
            OutputFormats outputFormat,
            String[] inputFolders,
            String[] classpathEntries) {
        exec(args, outputFormat, GraphBuilderPluginId.JAVA_SOURCES, inputFolders, classpathEntries);
    }

    public void runFromTypeScriptSources(String[] args, OutputFormats outputFormat, String[] inputFolders) {
        exec(args, outputFormat, GraphBuilderPluginId.TYPESCRIPT_SOURCES, inputFolders, null);
    }

    public SoftwarePackageGraph getGraph() {
        return graph;
    }

    private void captureOrphanedNodes(String[] groupDefinitionFiles) {
        ArrayList<SoftwarePackageNode> orphanNodes = new ArrayList<>();
        for (SoftwarePackageNode node : graph.getNodes()) {
            if (node.getParentGroupNode() == null && !node.getName().startsWith("[")) {
                orphanNodes.add(node);
            }
        }
        if (orphanNodes.isEmpty()) {
            return;
        }

        Path orphanFile = resolveOrphansGroupFile(groupDefinitionFiles);
        LinkedHashSet<String> orphanNames = readOrphanNames(orphanFile);
        for (SoftwarePackageNode orphanNode : orphanNodes) {
            orphanNames.add(orphanNode.getName());
        }
        writeOrphanGroupFile(orphanFile, orphanNames);

        SoftwarePackageGroup orphanGroup = findOrphanGroup();
        if (orphanGroup == null) {
            orphanGroup = new SoftwarePackageGroup(ORPHANS_GROUP_HEADER);
            orphanGroup.header = graph.addNode(ORPHANS_GROUP_HEADER);
            groups.add(orphanGroup);
        }
        for (String orphanName : orphanNames) {
            SoftwarePackageNode node = graph.searchNodeByName(orphanName);
            if (node == null || node.getParentGroupNode() != null) {
                continue;
            }
            node.setParentGroupNode(orphanGroup.header);
            graph.addDependency(orphanGroup.header, node);
            if (!orphanGroup.list.contains(node)) {
                orphanGroup.list.add(node);
            }
        }
    }

    private SoftwarePackageGroup findOrphanGroup() {
        for (SoftwarePackageGroup group : groups) {
            if (group.header != null && ORPHANS_GROUP_HEADER.equals(group.header.getName())) {
                return group;
            }
        }
        return null;
    }

    private Path resolveOrphansGroupFile(String[] groupDefinitionFiles) {
        if (groupDefinitionFiles != null && groupDefinitionFiles.length > 0) {
            Path firstGroupFile = Paths.get(groupDefinitionFiles[0]).toAbsolutePath().normalize();
            Path parent = firstGroupFile.getParent();
            if (parent != null) {
                return parent.resolve(ORPHANS_GROUP_FILE);
            }
        }
        return Paths.get(ORPHANS_GROUP_FILE).toAbsolutePath().normalize();
    }

    private LinkedHashSet<String> readOrphanNames(Path orphanFile) {
        LinkedHashSet<String> orphanNames = new LinkedHashSet<>();
        if (!Files.isRegularFile(orphanFile)) {
            return orphanNames;
        }
        try {
            List<String> lines = Files.readAllLines(orphanFile, StandardCharsets.UTF_8);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#") || line.equals(ORPHANS_GROUP_HEADER)) {
                    continue;
                }
                orphanNames.add(line);
            }
            return orphanNames;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read orphan group file: " + orphanFile, e);
        }
    }

    private void writeOrphanGroupFile(Path orphanFile, Set<String> orphanNames) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(ORPHANS_GROUP_HEADER);
        lines.addAll(orphanNames);
        try {
            Files.write(orphanFile, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write orphan group file: " + orphanFile, e);
        }
    }
}
