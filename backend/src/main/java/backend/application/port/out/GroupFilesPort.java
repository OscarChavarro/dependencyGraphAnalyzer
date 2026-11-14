package backend.application.port.out;

import backend.domain.model.GroupFileModel;
import java.nio.file.Path;
import java.util.List;

/** Reads and writes the group definition files of a group folder. */
public interface GroupFilesPort {
    /** Group files sorted by file name. Files without a "[NAME]" header are skipped. */
    List<GroupFileModel> readAll(Path groupFolder);

    /** Replaces the members of an existing group file, keeping its header. */
    void writeMembers(GroupFileModel group, List<String> members);

    /** Creates "<groupName in lower case>.txt" with the header "[GROUPNAME IN UPPER CASE]". */
    GroupFileModel create(Path groupFolder, String groupName);
}
