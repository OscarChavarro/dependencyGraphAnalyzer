package backend.application.service;

import backend.application.port.in.FindPackageGroupsUseCase;
import backend.domain.model.GroupDefinitionModel;
import backend.domain.model.PackageGroupsLookupModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Finds the groups that list a package. An exact match wins; otherwise a case-insensitive
 * match is used.
 */
@Service
public class FindPackageGroupsService implements FindPackageGroupsUseCase {
    private final GroupDefinitionsReader groupDefinitionsReader;

    public FindPackageGroupsService(GroupDefinitionsReader groupDefinitionsReader) {
        this.groupDefinitionsReader = groupDefinitionsReader;
    }

    @Override
    public PackageGroupsLookupModel execute(String groupFolder, String packageName) {
        String query = packageName == null ? "" : packageName.trim();
        if (query.isEmpty()) {
            throw new IllegalArgumentException("packageName is required");
        }

        List<GroupDefinitionModel> definitions = groupDefinitionsReader.read(groupFolder);

        PackageGroupsLookupModel exact = lookup(definitions, query, false);
        if (!exact.groups().isEmpty()) {
            return exact;
        }
        return lookup(definitions, query, true);
    }

    private PackageGroupsLookupModel lookup(List<GroupDefinitionModel> definitions, String query, boolean ignoreCase) {
        String matchedName = null;
        List<String> groups = new ArrayList<>();
        for (GroupDefinitionModel definition : definitions) {
            for (String member : definition.members()) {
                boolean matches = ignoreCase ? member.equalsIgnoreCase(query) : member.equals(query);
                if (matches && (matchedName == null || matchedName.equals(member))) {
                    matchedName = member;
                    groups.add(definition.groupName());
                    break;
                }
            }
        }
        return new PackageGroupsLookupModel(matchedName == null ? query : matchedName, List.copyOf(groups));
    }
}
