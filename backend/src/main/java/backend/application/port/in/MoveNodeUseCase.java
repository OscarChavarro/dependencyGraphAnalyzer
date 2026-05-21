package backend.application.port.in;

import java.util.Set;

public interface MoveNodeUseCase {
    String execute(String groupFolder, String originGroup, Set<String> originNodes, String destinationGroup);
}
