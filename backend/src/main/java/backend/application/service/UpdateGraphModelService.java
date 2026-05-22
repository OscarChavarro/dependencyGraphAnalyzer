package backend.application.service;

import backend.application.port.in.UpdateGraphModelUseCase;
import backend.domain.model.GraphModelEdge;
import backend.domain.model.GraphModelGenerator;
import backend.domain.model.GraphModelNode;
import backend.domain.model.GraphModelSnapshot;
import backend.domain.model.GraphModelStructure;
import core.DebianAnalyzer;
import core.OutputFormats;
import core.graph.PackageEdge;
import core.graph.SoftwarePackageGraph;
import core.graph.SoftwarePackageNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UpdateGraphModelService implements UpdateGraphModelUseCase {
    private static final Path CLEAN_RELATIONS_GRAPH_PATH = Path.of("output", "cleanRelationsGraph.txt");

    @Override
    public GraphModelSnapshot execute(
            GraphModelGenerator generator,
            String groupsDefinitionFolder,
            String[] inputFolders,
            String[] classpath) {
        resetCleanRelationsGraph();
        String[] groupsDefinitionFiles = resolveGroupDefinitionFiles(groupsDefinitionFolder);
        DebianAnalyzer analyzer = new DebianAnalyzer();

        switch (generator) {
            case CACHE_LOADER -> analyzer.runFromCache(groupsDefinitionFiles, OutputFormats.SVG, inputFolders);
            case DEBIAN_PACKAGE_GENERATOR -> analyzer.runFromDebian(groupsDefinitionFiles, OutputFormats.SVG);
            case CPP_SOURCES -> analyzer.runFromCppSources(groupsDefinitionFiles, OutputFormats.SVG, inputFolders);
            case JAVA_SOURCES -> analyzer.runFromJavaSources(groupsDefinitionFiles, OutputFormats.SVG, inputFolders, classpath);
        }

        SoftwarePackageGraph graph = analyzer.getGraph();
        List<GraphModelNode> nodes = graph.getNodes().stream()
                .map(this::mapNode)
                .toList();
        Set<PackageEdge> allEdges = graph.getEdges();
        List<GraphModelEdge> edges = allEdges.stream()
                .map(this::mapEdge)
                .toList();
        GraphModelStructure structure = buildStructureGraph(graph.getNodes(), allEdges);

        return new GraphModelSnapshot(nodes, edges, structure);
    }

    private void resetCleanRelationsGraph() {
        try {
            Path parent = CLEAN_RELATIONS_GRAPH_PATH.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(
                    CLEAN_RELATIONS_GRAPH_PATH,
                    "",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to reset clean relations graph output file", e);
        }
    }

    private GraphModelNode mapNode(SoftwarePackageNode node) {
        return new GraphModelNode(
                node.getName(),
                node.getIsBad(),
                node.getMark(),
                node.getGroup(),
                node.getSink(),
                node.getSource(),
                node.getVariant(),
                node.getBoldName());
    }

    private GraphModelEdge mapEdge(PackageEdge edge) {
        return new GraphModelEdge(edge.from().getName(), edge.to().getName());
    }

    private GraphModelStructure buildStructureGraph(List<SoftwarePackageNode> allNodes, Set<PackageEdge> allEdges) {
        Set<String> structureNodeNames = allNodes.stream()
                .map(SoftwarePackageNode::getName)
                .filter(this::isStructureNodeName)
                .collect(HashSet::new, Set::add, Set::addAll);

        List<GraphModelNode> structureNodes = allNodes.stream()
                .filter(node -> structureNodeNames.contains(node.getName()))
                .map(this::mapNode)
                .toList();

        List<GraphModelEdge> structureEdges = allEdges.stream()
                .filter(edge -> structureNodeNames.contains(edge.from().getName())
                        && structureNodeNames.contains(edge.to().getName()))
                .map(this::mapEdge)
                .toList();

        return new GraphModelStructure(structureNodes, structureEdges);
    }

    private boolean isStructureNodeName(String nodeName) {
        return nodeName.startsWith("_[");
    }

    private String[] resolveGroupDefinitionFiles(String groupsDefinitionFolder) {
        Path folder = resolveExistingFolder(groupsDefinitionFolder);
        try {
            List<String> files = Files.list(folder)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(Path::toString))
                    .map(Path::toString)
                    .toList();
            if (files.isEmpty()) {
                throw new IllegalArgumentException("No .txt files found in groupsDefinitionFolder: " + groupsDefinitionFolder);
            }
            return files.toArray(new String[0]);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to list groupsDefinitionFolder: " + groupsDefinitionFolder, e);
        }
    }

    private Path resolveExistingFolder(String groupsDefinitionFolder) {
        Path cwd = getCwd();
        Path firstTry = Paths.get(groupsDefinitionFolder);
        if (Files.isDirectory(firstTry)) {
            return firstTry.normalize();
        }
        String fallbackInput = groupsDefinitionFolder.replaceFirst("^\\.\\./", "");
        Path secondTry = Paths.get(fallbackInput);
        if (!fallbackInput.equals(groupsDefinitionFolder) && Files.isDirectory(secondTry)) {
            return secondTry.normalize();
        }
        Path firstTryNormalized = firstTry.normalize();
        Path secondTryNormalized = secondTry.normalize();
        throw new IllegalArgumentException("groupsDefinitionFolder does not exist or is not a directory. input='"
                + groupsDefinitionFolder + "', tried=[" + toAbsoluteFromCwd(firstTry, cwd) + ", "
                + toAbsoluteFromCwd(firstTryNormalized, cwd) + ", "
                + toAbsoluteFromCwd(secondTry, cwd) + ", "
                + toAbsoluteFromCwd(secondTryNormalized, cwd) + "]");
    }

    private Path getCwd() {
        return Paths.get("").toAbsolutePath().normalize();
    }

    private Path toAbsoluteFromCwd(Path path, Path cwd) {
        return cwd.resolve(path).normalize();
    }

}
