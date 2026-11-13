package graphbuilderplugins.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CacheFileReader {
    private static final String NODE_TOKEN = "n";
    private static final String RELATION_TOKEN = "r";
    private static final String BAD_STATE = "bad";

    private CacheFileReader() {
    }

    public static CacheFileContentModel read(Path file) {
        if (file == null || !Files.isRegularFile(file)) {
            return CacheFileContentModel.empty();
        }
        try {
            return parse(Files.readAllLines(file));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read cache file: " + file, e);
        }
    }

    public static CacheFileContentModel parse(List<String> lines) {
        List<CacheNodeEntryModel> nodes = new ArrayList<>();
        List<CacheRelationEntryModel> relations = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length < 2) {
                continue;
            }
            if (NODE_TOKEN.equals(tokens[0])) {
                // A node line without explicit state defaults to good.
                boolean good = tokens.length < 3 || !BAD_STATE.equals(tokens[2]);
                nodes.add(new CacheNodeEntryModel(tokens[1], good));
            } else if (RELATION_TOKEN.equals(tokens[0]) && tokens.length >= 3) {
                relations.add(new CacheRelationEntryModel(tokens[1], tokens[2]));
            }
        }
        return new CacheFileContentModel(List.copyOf(nodes), List.copyOf(relations));
    }
}
