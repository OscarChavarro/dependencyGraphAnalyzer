package backend.application.service;

import backend.application.port.in.PlanLfsSourceDownloadUseCase;
import backend.application.port.out.CachedProjectsPort;
import backend.domain.model.CachedProjectModel;
import backend.domain.model.GroupDefinitionModel;
import backend.domain.model.LfsDownloadPlanModel;
import backend.domain.model.LfsPlannedPackageModel;
import backend.domain.ordering.SinkFirstOrdering;
import graphbuilderplugins.cache.CacheFileContentModel;
import graphbuilderplugins.cache.CacheGraphComposer;
import graphbuilderplugins.cache.CacheRelationEntryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/**
 * Builds the "apt-get source" download order for the selected nodes of a cached project:
 * groups from the simplest to the most composite (sink groups first), and inside each group
 * packages from sinks to sources. Dependencies come from the project cache (base + extra).
 * Selected group nodes ("_[G]" or "[G]") expand to all the packages of that group.
 */
@Service
public class PlanLfsSourceDownloadService implements PlanLfsSourceDownloadUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(PlanLfsSourceDownloadService.class);
    private static final Pattern APT_PACKAGE_NAME = Pattern.compile("^[a-z0-9][a-z0-9+.\\-]*$");
    private static final Pattern GROUP_NODE = Pattern.compile("^_?\\[(.+)]$");
    private static final Pattern LEADING_NUMBER = Pattern.compile("^(\\d+)");
    private static final Comparator<String> GROUP_ORDER = Comparator
            .comparingLong(PlanLfsSourceDownloadService::leadingNumber)
            .thenComparing(String::compareTo);

    private final CachedProjectsPort cachedProjectsPort;
    private final GroupDefinitionsReader groupDefinitionsReader;

    public PlanLfsSourceDownloadService(
            CachedProjectsPort cachedProjectsPort,
            GroupDefinitionsReader groupDefinitionsReader) {
        this.cachedProjectsPort = cachedProjectsPort;
        this.groupDefinitionsReader = groupDefinitionsReader;
    }

    @Override
    public LfsDownloadPlanModel execute(String projectId, List<String> selectedNodes) {
        CachedProjectModel project = cachedProjectsPort.requireById(projectId);
        List<GroupDefinitionModel> groups = groupDefinitionsReader.read(
                cachedProjectsPort.resolveConfiguredPath(project.groupsDefinitionFolder()));
        Map<String, Set<String>> dependencies = readDependencies(project);

        Map<String, String> groupByPackage = new HashMap<>();
        Map<String, List<String>> membersByGroup = new LinkedHashMap<>();
        for (GroupDefinitionModel group : groups) {
            membersByGroup.put(group.groupName(), group.members());
            group.members().forEach(member -> groupByPackage.putIfAbsent(member, group.groupName()));
        }

        Set<String> selectedPackages = expandSelection(selectedNodes, membersByGroup);

        List<LfsPlannedPackageModel> planned = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        Set<String> plannedAptNames = new LinkedHashSet<>();
        for (String groupName : orderGroups(membersByGroup.keySet(), groupByPackage, dependencies)) {
            List<String> members = membersByGroup.get(groupName).stream()
                    .filter(member -> groupName.equals(groupByPackage.get(member)))
                    .toList();
            addInOrder(planned, skipped, plannedAptNames, members, selectedPackages, dependencies, groupName);
        }
        List<String> withoutGroup = selectedPackages.stream()
                .filter(name -> !groupByPackage.containsKey(name))
                .toList();
        addInOrder(planned, skipped, plannedAptNames, withoutGroup, selectedPackages, dependencies, null);

        LOG.info("LFS plan for project={}: selectedNodes={}, packagesToDownload={}, skippedNodes={}",
                projectId, selectedNodes.size(), planned.size(), skipped.size());
        return new LfsDownloadPlanModel(List.copyOf(planned), List.copyOf(skipped));
    }

    private Map<String, Set<String>> readDependencies(CachedProjectModel project) {
        Path cacheFile = cachedProjectsPort.resolveConfiguredPath(project.cacheFile());
        List<Path> extras = project.extra() == null || project.extra().isBlank()
                ? List.of()
                : List.of(cachedProjectsPort.resolveConfiguredPath(project.extra()));
        CacheFileContentModel content = CacheGraphComposer.compose(cacheFile, extras);

        Map<String, Set<String>> dependencies = new HashMap<>();
        for (CacheRelationEntryModel relation : content.relations()) {
            dependencies.computeIfAbsent(relation.source(), ignored -> new LinkedHashSet<>())
                    .add(relation.destination());
        }
        return dependencies;
    }

    private Set<String> expandSelection(List<String> selectedNodes, Map<String, List<String>> membersByGroup) {
        Set<String> selected = new LinkedHashSet<>();
        for (String node : selectedNodes == null ? List.<String>of() : selectedNodes) {
            if (node == null || node.isBlank()) {
                continue;
            }
            String trimmed = node.trim();
            Matcher groupMatcher = GROUP_NODE.matcher(trimmed);
            if (groupMatcher.matches()) {
                String groupName = groupMatcher.group(1);
                membersByGroup.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(groupName))
                        .findFirst()
                        .ifPresent(entry -> selected.addAll(entry.getValue()));
            } else {
                selected.add(trimmed);
            }
        }
        return selected;
    }

    /** Groups sink-first, by the package dependencies that cross group boundaries. */
    private List<String> orderGroups(
            Set<String> groupNames,
            Map<String, String> groupByPackage,
            Map<String, Set<String>> dependencies) {
        Map<String, Set<String>> groupDependencies = new HashMap<>();
        dependencies.forEach((source, targets) -> {
            String sourceGroup = groupByPackage.get(source);
            if (sourceGroup == null) {
                return;
            }
            for (String target : targets) {
                String targetGroup = groupByPackage.get(target);
                if (targetGroup != null && !targetGroup.equals(sourceGroup)) {
                    groupDependencies.computeIfAbsent(sourceGroup, ignored -> new LinkedHashSet<>()).add(targetGroup);
                }
            }
        });
        return SinkFirstOrdering.order(groupNames, groupDependencies, GROUP_ORDER);
    }

    private void addInOrder(
            List<LfsPlannedPackageModel> planned,
            List<String> skipped,
            Set<String> plannedAptNames,
            List<String> members,
            Set<String> selectedPackages,
            Map<String, Set<String>> dependencies,
            String groupName) {
        // Order the whole group, so transitive order through unselected members is preserved.
        Set<String> memberSet = new LinkedHashSet<>(members);
        Map<String, Set<String>> internalDependencies = new HashMap<>();
        for (String member : memberSet) {
            Set<String> internal = new LinkedHashSet<>();
            for (String dependency : dependencies.getOrDefault(member, Set.of())) {
                if (memberSet.contains(dependency)) {
                    internal.add(dependency);
                }
            }
            internalDependencies.put(member, internal);
        }

        for (String member : SinkFirstOrdering.order(memberSet, internalDependencies, Comparator.naturalOrder())) {
            if (!selectedPackages.contains(member)) {
                continue;
            }
            String aptName = toAptName(member);
            if (!APT_PACKAGE_NAME.matcher(aptName).matches()) {
                skipped.add(member);
                continue;
            }
            if (plannedAptNames.add(aptName)) {
                planned.add(new LfsPlannedPackageModel(aptName, groupName));
            }
        }
    }

    /** "libuuid1:amd64" -> "libuuid1". */
    private static String toAptName(String packageName) {
        int architectureSeparator = packageName.indexOf(':');
        return architectureSeparator > 0 ? packageName.substring(0, architectureSeparator) : packageName;
    }

    private static long leadingNumber(String groupName) {
        Matcher matcher = LEADING_NUMBER.matcher(groupName);
        return matcher.find() ? Long.parseLong(matcher.group(1)) : Long.MAX_VALUE;
    }
}
