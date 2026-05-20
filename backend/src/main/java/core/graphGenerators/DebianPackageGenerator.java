package core.graphGenerators;

import core.graphGenerators.DataElement;
import core.graphGenerators.DebReader;
import core.graph.SoftwarePackageGraph;
import core.graph.SoftwarePackageNode;
import common.ProcessControl;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DebianPackageGenerator {
    private final SoftwarePackageGraph graph;

    public DebianPackageGenerator(SoftwarePackageGraph graph) {
        this.graph = graph;
    }

    public void generateFromSystemPackageManager() {
        String[] args = new String[] {"dpkg-query", "-l"};
        Process process = ProcessControl.system(args);

        buildGraphNodesFromDpkg(process);
        ProcessControl.finalizeProcess(process);

        process = null;
        System.gc();

        ArrayList<SoftwarePackageNode> nodes = graph.getNodes();
        System.out.print("Checking " + nodes.size() + " packages: ");

        int workersCount = 72;
        Thread[] threads = new Thread[workersCount];
        DebReader[] processors = new DebReader[workersCount];

        for (int i = 0; i < workersCount; i++) {
            processors[i] = new DebReader();
            threads[i] = new Thread(processors[i]);
        }

        for (int i = 0; i < nodes.size(); i++) {
            processors[i % workersCount].inputs.add(nodes.get(i));
        }

        for (int i = 0; i < workersCount; i++) {
            threads[i].start();
        }

        for (int i = 0; i < workersCount; i++) {
            try {
                threads[i].join();
            } catch (Exception ignored) {
            }
        }

        for (int i = 0; i < workersCount; i++) {
            for (int j = 0; j < processors[i].outputs.size(); j++) {
                addDeps(processors[i].outputs.get(j));
            }
        }

        System.out.println(" Ok!");
    }

    private void addDeps(DataElement e) {
        for (String key : e.deps) {
            SoftwarePackageNode child = graph.searchNodeByName(key);
            if (child != null) {
                graph.addDependency(e.node, child);
            } else {
                graph.addBadNode(key);
                child = graph.searchNodeByName(key);
                graph.addDependency(e.node, child);
            }
        }
    }

    private void buildGraphNodesFromDpkg(Process process) {
        InputStream processOutput = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(processOutput));

        try {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    StringTokenizer parser = new StringTokenizer(line, " \n");
                    String token = parser.nextToken();
                    if (token.equals("ii")) {
                        token = parser.nextToken();
                        graph.addNode(token);
                    }
                }
            } while (line != null);
        } catch (Exception error) {
            System.out.println("ERROR intentando leer salida standard del comando!");
            System.out.println("<DebianPackageGenerator.buildGraphNodesFromDpkg>" + error);
            error.printStackTrace();
        }

        for (int i = 0; i < graph.getNodes().size(); i++) {
            String packageName = graph.getNodes().get(i).getName();
            if (packageName.indexOf(':') != -1) {
                String simplified = packageName.substring(0, packageName.indexOf(':'));
                SoftwarePackageNode original = graph.searchNodeByName(packageName);
                SoftwarePackageNode simplifiedNode = graph.addNode(simplified);
                graph.addDependency(original, simplifiedNode);
                graph.addDependency(simplifiedNode, original);
            }
        }
    }
}
