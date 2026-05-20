package core.graphGenerators;

import core.graph.SoftwarePackageGraph;
import core.graph.SoftwarePackageNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CacheLoaderGenerator {
    private final SoftwarePackageGraph graph;

    public CacheLoaderGenerator(SoftwarePackageGraph graph) {
        this.graph = graph;
    }

    public void generateFromCache(File cacheFile) {
        System.out.print("Building graph from cache ...");

        String name = null;
        String state = null;
        String source = null;
        String target = null;
        String line = null;

        try {
            FileInputStream fis = new FileInputStream(cacheFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            do {
                line = br.readLine();
                if (line != null) {
                    StringTokenizer parser = new StringTokenizer(line, " \n");
                    String token = parser.nextToken();
                    if (token.equals("n")) {
                        name = parser.nextToken();
                        state = parser.nextToken();
                        if (state.equals("good")) {
                            graph.addNode(name);
                        } else {
                            graph.addBadNode(name);
                        }
                    } else if (token.equals("r")) {
                        source = parser.nextToken();
                        target = parser.nextToken();
                        SoftwarePackageNode a = graph.searchNodeByName(source);
                        SoftwarePackageNode b = graph.searchNodeByName(target);
                        if (a == null) {
                            System.err.println("No puedo leer nodo " + source);
                        }
                        graph.addDependency(a, b);
                    }
                }
            } while (line != null);
        } catch (Exception error) {
            System.out.println("ERROR intentando leer cache!");
            System.out.println("<CacheLoaderGenerator.generateFromCache>" + error);
            System.out.println("  - Line: " + line);
            System.out.println("  - Name: " + name);
            System.out.println("  - State: " + state);
            System.out.println("  - Source: " + source);
            System.out.println("  - Target: " + target);
            error.printStackTrace();
        }

        System.out.println("Ok!");
    }
}
