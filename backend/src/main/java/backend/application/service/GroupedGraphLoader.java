package backend.application.service;

import backend.application.port.out.GroupFilesPort;
import backend.application.port.out.ProjectCatalogPort;
import backend.domain.model.GroupFileModel;
import backend.domain.model.GroupedGraphModel;
import backend.domain.model.NodeRelationModel;
import backend.domain.model.ProjectDescriptorModel;
import backend.domain.model.RawGraphModel;
import graphbuilderplugins.api.GraphBuildTarget;
import graphbuilderplugins.api.GraphBuilderPluginId;
import graphbuilderplugins.api.GraphBuilderPluginRegistry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Builds the raw graph of a catalog project in memory and pairs it with its group definitions.
 * Nothing is written: cache.txt and output/ stay untouched.
 */
@Component
public class GroupedGraphLoader {
    private final ProjectCatalogPort projectCatalogPort;
    private final GroupFilesPort groupFilesPort;
    private final GraphBuilderPluginRegistry pluginRegistry = new GraphBuilderPluginRegistry();

    public GroupedGraphLoader(ProjectCatalogPort projectCatalogPort, GroupFilesPort groupFilesPort) {
        this.projectCatalogPort = projectCatalogPort;
        this.groupFilesPort = groupFilesPort;
    }

    public GroupedGraphModel load(String projectId) {
        ProjectDescriptorModel project = projectCatalogPort.requireById(projectId);
        RawGraphModel graph = buildGraph(project);
        List<GroupFileModel> groups = groupFilesPort.readAll(project.groupsDefinitionFolder());

        Set<String> nodes = Set.copyOf(graph.nodes());
        Map<String, String> groupByNode = new LinkedHashMap<>();
        for (GroupFileModel group : groups) {
            if (group.isOrphans()) {
                continue;
            }
            for (String member : group.members()) {
                if (nodes.contains(member)) {
                    // a node listed twice ends in the last group, as in the analyzer core
                    groupByNode.put(member, group.groupName());
                }
            }
        }
        return new GroupedGraphModel(project, graph, groups, groupByNode);
    }

    private RawGraphModel buildGraph(ProjectDescriptorModel project) {
        CollectingTarget target = new CollectingTarget();
        pluginRegistry.require(GraphBuilderPluginId.valueOf(project.generator().name()))
                .build(target, project.inputs(), project.classpath());
        return new RawGraphModel(List.copyOf(target.nodes), List.copyOf(target.relations));
    }

    private static final class CollectingTarget implements GraphBuildTarget {
        private final LinkedHashSet<String> nodes = new LinkedHashSet<>();
        private final LinkedHashSet<NodeRelationModel> relations = new LinkedHashSet<>();

        @Override
        public void addNode(String name) {
            nodes.add(name);
        }

        @Override
        public void addBadNode(String name) {
            nodes.add(name);
        }

        @Override
        public void addDependency(String fromNodeName, String toNodeName) {
            nodes.add(fromNodeName);
            nodes.add(toNodeName);
            if (!fromNodeName.equals(toNodeName)) {
                relations.add(new NodeRelationModel(fromNodeName, toNodeName));
            }
        }

        @Override
        public List<String> listNodeNames() {
            return new ArrayList<>(nodes);
        }
    }
}
