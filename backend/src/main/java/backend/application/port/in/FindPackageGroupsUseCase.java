package backend.application.port.in;

import backend.domain.model.PackageGroupsLookupModel;

public interface FindPackageGroupsUseCase {
    PackageGroupsLookupModel execute(String groupFolder, String packageName);
}
