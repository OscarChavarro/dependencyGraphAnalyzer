package backend.application.service;

import backend.application.port.in.ListProjectsUseCase;
import backend.application.port.out.ProjectCatalogPort;
import backend.domain.model.ProjectDescriptorModel;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListProjectsService implements ListProjectsUseCase {
    private final ProjectCatalogPort projectCatalogPort;

    public ListProjectsService(ProjectCatalogPort projectCatalogPort) {
        this.projectCatalogPort = projectCatalogPort;
    }

    @Override
    public List<ProjectDescriptorModel> execute() {
        return projectCatalogPort.listAll();
    }
}
