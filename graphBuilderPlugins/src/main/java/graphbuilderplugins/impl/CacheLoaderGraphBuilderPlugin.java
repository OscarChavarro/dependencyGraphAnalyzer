package graphbuilderplugins.impl;

import graphbuilderplugins.api.GraphBuildTarget;
import graphbuilderplugins.api.GraphBuilderPlugin;
import graphbuilderplugins.api.GraphBuilderPluginId;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

public class CacheLoaderGraphBuilderPlugin implements GraphBuilderPlugin {
    @Override
    public GraphBuilderPluginId id() {
        return GraphBuilderPluginId.CACHE_LOADER;
    }

    @Override
    public void build(GraphBuildTarget target) {
        System.out.print("Building graph from cache ...");
        loadIfExists(target, Path.of("cache.txt"));
        loadIfExists(target, Path.of("cache_extra.txt"));
        System.out.println("Ok!");
    }

    private void loadIfExists(GraphBuildTarget target, Path file) {
        if (!Files.isRegularFile(file)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(target, line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read cache file: " + file, e);
        }
    }

    private void parseLine(GraphBuildTarget target, String line) {
        StringTokenizer parser = new StringTokenizer(line, " \n");
        if (!parser.hasMoreTokens()) {
            return;
        }

        String token = parser.nextToken();
        if ("n".equals(token) && parser.countTokens() >= 2) {
            String name = parser.nextToken();
            String state = parser.nextToken();
            if ("good".equals(state)) {
                target.addNode(name);
            } else {
                target.addBadNode(name);
            }
            return;
        }

        if ("r".equals(token) && parser.countTokens() >= 2) {
            String source = parser.nextToken();
            String destination = parser.nextToken();
            target.addDependency(source, destination);
        }
    }
}
