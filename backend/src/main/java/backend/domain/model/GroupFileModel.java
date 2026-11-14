package backend.domain.model;

import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A group definition file with its location.
 *
 * @param groupName header without brackets, e.g. "22_GEOMETRY_VOLUME"
 * @param members   member names in file order, without comments or blank lines
 */
public record GroupFileModel(String groupName, Path file, List<String> members) {
    public static final String ORPHANS_GROUP_NAME = "999_orphans";
    private static final Pattern LEVEL_PATTERN = Pattern.compile("^(\\d+)");

    public boolean isOrphans() {
        return ORPHANS_GROUP_NAME.equalsIgnoreCase(groupName);
    }

    /** Layer of the group: the leading integer of its name, or null when the name has none. */
    public Integer level() {
        return levelOf(groupName);
    }

    public static Integer levelOf(String groupName) {
        if (groupName == null) {
            return null;
        }
        Matcher matcher = LEVEL_PATTERN.matcher(groupName);
        return matcher.find() ? Integer.valueOf(matcher.group(1)) : null;
    }
}
