package graphbuilderplugins.impl;

import graphbuilderplugins.api.GraphBuildTarget;
import graphbuilderplugins.api.GraphBuilderPlugin;
import graphbuilderplugins.api.GraphBuilderPluginId;
import graphbuilderplugins.internal.ProcessRunner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class DebianPackageGraphBuilderPlugin implements GraphBuilderPlugin {
    @Override
    public GraphBuilderPluginId id() {
        return GraphBuilderPluginId.DEBIAN_PACKAGE_GENERATOR;
    }

    @Override
    public void build(GraphBuildTarget target) {
        Process process = ProcessRunner.start("dpkg-query", "-l");
        buildGraphNodesFromDpkg(process, target);
        ProcessRunner.waitFor(process);

        List<String> nodeNames = target.listNodeNames();
        System.out.print("Checking " + nodeNames.size() + " packages: ");

        int workersCount = 72;
        List<List<String>> shards = createShards(workersCount);
        for (int i = 0; i < nodeNames.size(); i++) {
            shards.get(i % workersCount).add(nodeNames.get(i));
        }

        List<Thread> threads = new ArrayList<>(workersCount);
        List<List<DataElement>> outputs = new ArrayList<>(workersCount);

        for (int i = 0; i < workersCount; i++) {
            DebReaderWorker worker = new DebReaderWorker(shards.get(i));
            Thread thread = new Thread(worker);
            threads.add(thread);
            outputs.add(worker.outputs());
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Dependency analysis interrupted", e);
            }
        }

        for (List<DataElement> out : outputs) {
            for (DataElement element : out) {
                addDeps(target, element);
            }
        }

        System.out.println(" Ok!");
    }

    private List<List<String>> createShards(int workersCount) {
        List<List<String>> shards = new ArrayList<>(workersCount);
        for (int i = 0; i < workersCount; i++) {
            shards.add(new ArrayList<>());
        }
        return shards;
    }

    private void addDeps(GraphBuildTarget target, DataElement element) {
        for (String dependency : element.dependencies()) {
            target.addDependency(element.nodeName(), dependency);
        }
    }

    private void buildGraphNodesFromDpkg(Process process, GraphBuildTarget target) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer parser = new StringTokenizer(line, " \n");
                if (!parser.hasMoreTokens()) {
                    continue;
                }
                String token = parser.nextToken();
                if ("ii".equals(token) && parser.hasMoreTokens()) {
                    target.addNode(parser.nextToken());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read dpkg-query output", e);
        }

        List<String> nodes = new ArrayList<>(target.listNodeNames());
        for (String packageName : nodes) {
            int separator = packageName.indexOf(':');
            if (separator == -1) {
                continue;
            }
            String simplified = packageName.substring(0, separator);
            target.addNode(simplified);
            target.addDependency(packageName, simplified);
            target.addDependency(simplified, packageName);
        }
    }

    private record DataElement(String nodeName, Set<String> dependencies) {
    }

    private static class DebReaderWorker implements Runnable {
        private final List<String> packageNames;
        private final List<DataElement> outputs = new ArrayList<>();

        private DebReaderWorker(List<String> packageNames) {
            this.packageNames = packageNames;
        }

        public List<DataElement> outputs() {
            return outputs;
        }

        @Override
        public void run() {
            for (String packageName : packageNames) {
                outputs.add(readDependencies(packageName));
            }
        }

        private DataElement readDependencies(String packageName) {
            Process process = ProcessRunner.start("apt-cache", "depends", packageName);
            System.out.print(".");

            TreeSet<String> dependencies = new TreeSet<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int start = line.indexOf("Depends: ");
                    if (start < 0) {
                        continue;
                    }
                    String dependency = line.substring(start + 9).trim();
                    if (!dependency.isEmpty() && !dependency.contains("<")) {
                        dependencies.add(dependency);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read apt-cache output for: " + packageName, e);
            } finally {
                ProcessRunner.waitFor(process);
            }
            return new DataElement(packageName, dependencies);
        }
    }
}
