package entryPoints;

import core.DebianAnalyzer;
import core.OutputFormats;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandLineApp {
    public static void main(String[] args) {
        DebianAnalyzer instance = new DebianAnalyzer();
        List<String> expandedArgs = new ArrayList<>();

        for (String arg : args) {
            if (arg.contains("*") || arg.contains("?")) {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + arg);
                Path parent = Paths.get(arg).getParent();
                if (parent == null) {
                    parent = Paths.get(".");
                }

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(parent)) {
                    for (Path path : stream) {
                        if (matcher.matches(path)) {
                            expandedArgs.add(path.toString());
                        }
                    }
                } catch (IOException e) {
                }
            } else {
                expandedArgs.add(Paths.get(arg).toString());
            }
        }

        String[] expanded = expandedArgs.toArray(new String[0]);
        if (Files.exists(Paths.get("cache.txt"))) {
            instance.runFromCache(expanded, OutputFormats.PNG);
        } else {
            instance.runFromDebian(expanded, OutputFormats.PNG);
        }
    }
}
