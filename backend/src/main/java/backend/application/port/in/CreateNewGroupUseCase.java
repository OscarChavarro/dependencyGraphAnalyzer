package backend.application.port.in;

public interface CreateNewGroupUseCase {
    String execute(String groupFolder, String newGroupName);
}
