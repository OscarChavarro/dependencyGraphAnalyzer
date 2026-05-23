package backend.application.port.in;

public interface RenameNodeUseCase {
    String execute(String groupFolder, String oldGroupName, String newGroupName);
}
