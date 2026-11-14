package backend.application.port.in;

import backend.domain.model.ProjectDescriptorModel;
import java.util.List;

public interface ListProjectsUseCase {
    List<ProjectDescriptorModel> execute();
}
