package backend.application.port.in;

import backend.domain.model.GraphModelEdge;
import backend.domain.model.GraphModelGenerator;
import java.util.List;

public interface BuildEnrichedEdgesUseCase {
    List<GraphModelEdge> execute(GraphModelGenerator generator, String groupsDefinitionFolder, String[] inputFolders);
}
