package backend.application.port.in;

import backend.domain.model.GraphModelGenerator;
import backend.domain.model.GraphModelSnapshot;

public interface UpdateGraphModelUseCase {
    GraphModelSnapshot execute(GraphModelGenerator generator, String groupsDefinitionFolder, String[] inputFolders);
}
