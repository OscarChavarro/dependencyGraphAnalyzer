package backend.application.port.in;

public interface MoveNodeUseCase {
    String execute(String groupFolder, String originGroup, String originNode, String destinationGroup);
}
