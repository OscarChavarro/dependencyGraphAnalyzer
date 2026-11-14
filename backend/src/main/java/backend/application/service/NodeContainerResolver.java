package backend.application.service;

import org.springframework.stereotype.Component;

/**
 * Maps a node name to its container: the directory of a path-like name ("a/b/C.h" -> "a/b") or
 * the package of a dotted name ("a.b.C" -> "a.b"). Names without a separator have no container.
 */
@Component
public class NodeContainerResolver {
    public static final String NO_CONTAINER = "(no container)";

    /** @param depth maximum number of segments kept; 0 keeps the full parent path */
    public String resolve(String nodeName, int depth) {
        char separator = nodeName.indexOf('/') >= 0 ? '/' : '.';
        int lastSeparator = nodeName.lastIndexOf(separator);
        if (lastSeparator <= 0) {
            return NO_CONTAINER;
        }
        String parent = nodeName.substring(0, lastSeparator);
        if (depth <= 0) {
            return parent;
        }
        int end = -1;
        for (int segment = 0; segment < depth; segment++) {
            end = parent.indexOf(separator, end + 1);
            if (end < 0) {
                return parent;
            }
        }
        return parent.substring(0, end);
    }
}
