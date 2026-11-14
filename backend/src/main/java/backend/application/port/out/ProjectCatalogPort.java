package backend.application.port.out;

import backend.domain.model.ProjectDescriptorModel;
import java.util.List;

/** Access to every project catalog under etc/projects. */
public interface ProjectCatalogPort {
    List<ProjectDescriptorModel> listAll();

    /** @throws IllegalArgumentException when the project id is unknown */
    ProjectDescriptorModel requireById(String projectId);
}
