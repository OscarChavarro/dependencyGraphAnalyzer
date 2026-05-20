package core.graph;

import core.OutputFormats;
import core.grouper.SoftwarePackageGroup;
import core.exporters.DotWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import core.graph.DirectedPackageGraphOperations;
import core.graph.JGraphTPackageGraphOperations;
import core.graph.PackageEdge;
import vsdk.toolkit.io.PersistenceElement;

public class SoftwarePackageGraph {
    private final ArrayList<SoftwarePackageNode> nodes;
    private final Hashtable<String, SoftwarePackageNode> nodeIndex;
    private final DirectedPackageGraphOperations graph;

    public SoftwarePackageGraph() {
        nodes = new ArrayList<>();
        nodeIndex = new Hashtable<>();
        graph = new JGraphTPackageGraphOperations();
    }

    private String normalizeFilename(String in) {
        String out = "";
        char c;
        int i;
        int n = in.length();

        for (i = 0; i < n; i++) {
            if (in.charAt(i) != '[') {
                break;
            }
        }

        for (; i < n; i++) {
            c = in.charAt(i);
            if (c == ']') {
                break;
            }
            out = out + c;
        }

        return out.toLowerCase();
    }

    public SoftwarePackageNode addNode(String name) {
        SoftwarePackageNode n = nodeIndex.get(name);
        if (n != null) {
            return n;
        }

        n = new SoftwarePackageNode(name);
        nodes.add(n);
        nodeIndex.put(name, n);
        graph.addVertex(n);
        return n;
    }

    public void addBadNode(String name) {
        SoftwarePackageNode n = nodeIndex.get(name);
        if (n != null) {
            n.markAsBad();
            return;
        }
        n = new SoftwarePackageNode(name);
        n.markAsBad();
        nodes.add(n);
        nodeIndex.put(name, n);
        graph.addVertex(n);
    }

    public SoftwarePackageNode searchNodeByName(String n) {
        return nodeIndex.get(n);
    }

    public ArrayList<SoftwarePackageNode> getNodes() {
        return nodes;
    }

    public boolean addDependency(SoftwarePackageNode from, SoftwarePackageNode to) {
        if (from == null || to == null) {
            return false;
        }
        boolean added = graph.addEdge(from, to);
        if (added) {
            from.addChild(to);
        }
        return added;
    }

    private void removeDependency(SoftwarePackageNode from, SoftwarePackageNode to) {
        if (graph.removeEdge(from, to)) {
            from.getChildren().remove(to);
        }
    }

    public void labelLastGroup(ArrayList<SoftwarePackageGroup> groups) {
        SoftwarePackageGroup last = groups.get(groups.size() - 1);
        markVariants(last, groups);
    }

    public void markVariants(SoftwarePackageGroup general, ArrayList<SoftwarePackageGroup> groups) {
        for (SoftwarePackageNode n : general.list) {
            n.setVariant(false);
            for (SoftwarePackageGroup g : groups) {
                if (n.getName().contains(g.header.getName())) {
                    if (g.areAllSources()) {
                        n.setVariant(true);
                    }
                    break;
                }
            }
        }
    }

    public void exportDotNode(OutputStream os, SoftwarePackageNode node, boolean indent) throws Exception {
        if (node.getName().startsWith("_[") && !node.getName().startsWith("_[00") && graph.outDegreeOf(node) <= 0) {
            return;
        }

        String n = "    \"" + node.getName() + "\"";
        if (indent) {
            n = "        \"" + node.getName() + "\"";
        }

        String shape = ",fontsize=10";
        if (node.getName().startsWith("[")) {
            shape = ",shape=polygon,sides=4";
        } else if (node.getName().startsWith("*")) {
            shape = ",shape=polygon,sides=6";
        }

        if (node.getBoldName()) {
            shape += ",peripheries=2";
        }

        if (!node.getIsBad()) {
            if (node.getSource()) {
                n = n + " [style=filled,color=black,fillcolor=green" + shape + "];";
                if (!indent) {
                    System.out.println("TOP LEVEL NODE: " + node.getName());
                }
            } else if (node.getSink()) {
                n = n + " [style=filled,color=black,fillcolor=lightblue" + shape + "];";
            } else {
                String color = "yellow";
                if (node.getName().startsWith("_[") && node.getVariant()) {
                    color = "lightblue";
                }
                n = n + " [style=filled,color=black,fillcolor=" + color + shape + "];";
            }
        } else {
            n = n + " [style=filled,color=black,fillcolor=red" + shape + "];";
        }
        PersistenceElement.writeAsciiLine(os, n);
    }

    private void exportDotNodes(OutputStream os, SoftwarePackageGroup g, int i) throws Exception {
        PersistenceElement.writeAsciiLine(os, "    subgraph cluster" + i + " {");
        PersistenceElement.writeAsciiLine(os, "        color=black;");
        PersistenceElement.writeAsciiLine(os, "        fillcolor=lightgray;");
        PersistenceElement.writeAsciiLine(os, "        style=\"filled,bold\";");
        exportDotNode(os, g.header, true);
        g.header.setGroup(true);

        for (SoftwarePackageNode node : g.list) {
            exportDotNode(os, node, true);
            node.setGroup(true);
        }
        PersistenceElement.writeAsciiLine(os, "    }");
        PersistenceElement.writeAsciiLine(os, "    ");
    }

    public void exportDot(String filename, ArrayList<SoftwarePackageGroup> groups) {
        exportDot(filename, groups, -1);
    }

    private boolean namesAreGroupsAndFirstGreater(String a, String b) {
        if (!a.startsWith("_[") || !b.startsWith("_[")) {
            return false;
        }

        StringTokenizer parser1 = new StringTokenizer(a, "[]_");
        StringTokenizer parser2 = new StringTokenizer(b, "[]_");
        int na = Integer.parseInt(parser1.nextToken());
        int nb = Integer.parseInt(parser2.nextToken());
        return na < nb;
    }

    private void exportDot(String filename, ArrayList<SoftwarePackageGroup> groups, int id) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filename));

            PersistenceElement.writeAsciiLine(fos, "digraph G {");
            PersistenceElement.writeAsciiLine(fos, "    //= NODES ==============================================================");

            ungroupAllNodes();

            if (id == -1) {
                for (int i = 0; i < groups.size(); i++) {
                    exportDotNodes(fos, groups.get(i), i);
                }
            } else {
                exportDotNodes(fos, groups.get(id), id);
            }

            if (id == -1) {
                System.out.print("Exporting nodes not in groups...");
                for (SoftwarePackageNode node : nodes) {
                    if (!node.getGroup() && !node.getName().startsWith("[") && !node.getName().startsWith("_[")) {
                        exportDotNode(fos, node, false);
                    }
                }
                System.out.println(" Ok!");
            }

            PersistenceElement.writeAsciiLine(fos, "\n    //= ARCS ===============================================================");

            for (PackageEdge edge : graph.edges()) {
                SoftwarePackageNode aNode = edge.from();
                SoftwarePackageNode bNode = edge.to();
                String a = aNode.getName();
                String b = bNode.getName();

                String r = "    \"" + a + "\" -> \"" + b + "\";";
                if (namesAreGroupsAndFirstGreater(a, b)) {
                    r = "    \"" + a + "\" -> \"" + b + "\" [color=\"red\"];";
                }

                if ((id == -1) || groups.get(id).contains(bNode)) {
                    PersistenceElement.writeAsciiLine(fos, r);
                }
            }

            PersistenceElement.writeAsciiLine(fos, "}");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportGroups(ArrayList<SoftwarePackageGroup> groups) {
        try {
            ungroupAllNodes();
            for (SoftwarePackageGroup group : groups) {
                SoftwarePackageNode parent = group.header;
                if (graph.outDegreeOf(parent) == 0) {
                    continue;
                }

                parent.setGroup(true);
                String filename = "./output/txt/" + normalizeFilename(group.header.getName()) + ".txt";
                FileOutputStream fos = new FileOutputStream(new File(filename));
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, parent.getName());
                PersistenceElement.writeAsciiLine(bos, "#- Top packages ------------------------------------------------------------");

                for (SoftwarePackageNode n : graph.outgoingOf(parent)) {
                    n.setGroup(true);
                    PersistenceElement.writeAsciiLine(bos, n.getName());
                }

                PersistenceElement.writeAsciiLine(bos, "#- Dependent packages ------------------------------------------------------");
                for (SoftwarePackageNode n : group.list) {
                    if (!n.getGroup()) {
                        PersistenceElement.writeAsciiLine(bos, n.getName());
                    }
                }

                bos.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    public void exportCleanScripts(ArrayList<SoftwarePackageGroup> groups) {
        try {
            ungroupAllNodes();
            for (SoftwarePackageGroup group : groups) {
                SoftwarePackageNode parent = group.header;
                if (graph.outDegreeOf(parent) == 0) {
                    continue;
                }

                parent.setGroup(true);
                String filename = "./output/cleansh/" + normalizeFilename(group.header.getName()) + ".sh";
                FileOutputStream fos = new FileOutputStream(new File(filename));
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, "# " + parent.getName());
                StringBuilder command = new StringBuilder("apt-get purge ");

                for (SoftwarePackageNode n : graph.outgoingOf(parent)) {
                    n.setGroup(true);
                    command.append(n.getName()).append(' ');
                }
                for (SoftwarePackageNode n : group.list) {
                    if (!n.getGroup()) {
                        command.append(n.getName()).append(' ');
                    }
                }
                PersistenceElement.writeAsciiLine(bos, command.toString());

                bos.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    public void exportTopNodesPerGroups(ArrayList<SoftwarePackageGroup> groups) {
        try {
            ungroupAllNodes();
            for (SoftwarePackageGroup group : groups) {
                SoftwarePackageNode parent = group.header;
                if (graph.outDegreeOf(parent) == 0) {
                    continue;
                }

                parent.setGroup(true);
                String filename = "./output/tops/" + normalizeFilename(group.header.getName()) + ".sh";
                FileOutputStream fos = new FileOutputStream(new File(filename));
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, "# " + parent.getName());

                StringBuilder command = new StringBuilder("apt-get install ");
                int count = 0;

                for (SoftwarePackageNode n : graph.outgoingOf(parent)) {
                    n.setGroup(true);
                    if (n.getBoldName()) {
                        command.append(n.getName()).append(' ');
                        count++;
                    }
                }
                for (SoftwarePackageNode n : group.list) {
                    if (!n.getGroup() && n.getBoldName()) {
                        command.append(n.getName()).append(' ');
                        count++;
                    }
                }

                if (count == 0) {
                    PersistenceElement.writeAsciiLine(bos, "# no top nodes");
                } else {
                    PersistenceElement.writeAsciiLine(bos, command.toString());
                }

                bos.close();
                fos.close();

                if (count == 0) {
                    Files.delete(Paths.get(filename));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    public void exportInstallScripts(ArrayList<SoftwarePackageGroup> groups) {
        try {
            ungroupAllNodes();
            for (SoftwarePackageGroup group : groups) {
                SoftwarePackageNode parent = group.header;
                if (graph.outDegreeOf(parent) == 0) {
                    continue;
                }

                parent.setGroup(true);
                String filename = "./output/installsh/" + normalizeFilename(group.header.getName()) + ".sh";
                FileOutputStream fos = new FileOutputStream(new File(filename));
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, "# " + parent.getName());
                StringBuilder command = new StringBuilder("apt-get install ");

                for (SoftwarePackageNode n : graph.outgoingOf(parent)) {
                    n.setGroup(true);
                    command.append(n.getName()).append(' ');
                }
                PersistenceElement.writeAsciiLine(bos, command.toString());

                bos.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    public void exportDotByGroups(ArrayList<SoftwarePackageGroup> groups, OutputFormats outputFormat) {
        int N = 24;
        ArrayList<ArrayList<String[]>> egroups = new ArrayList<>();
        String graphvizType = outputFormat == OutputFormats.SVG ? "svg" : "png";
        String outputDir = "./output/" + graphvizType;

        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        }

        for (int i = 0; i < N; i++) {
            egroups.add(new ArrayList<>());
        }

        for (int i = 0; i < groups.size(); i++) {
            System.out.print(".");
            String filename = "./output/dot/" + normalizeFilename(normalizeFilename(groups.get(i).header.getName())) + ".dot";

            SoftwarePackageNode parent = groups.get(i).header;
            if (graph.outDegreeOf(parent) == 0 && !parent.getName().equals("[STRUCTURE]")) {
                continue;
            }

            exportDot(filename, groups, i);
            String[] args = new String[] {
                "/usr/bin/dot",
                "-Gnodesep=0",
                "-T" + graphvizType,
                filename,
                "-o",
                outputDir + "/" + normalizeFilename(groups.get(i).header.getName()) + "." + graphvizType
            };
            egroups.get(i % N).add(args);
        }

        Thread[] ts = new Thread[N];
        for (int i = 0; i < N; i++) {
            DotWriter unit = new DotWriter(egroups.get(i));
            ts[i] = new Thread(unit);
            ts[i].start();
        }

        for (int i = 0; i < N; i++) {
            try {
                ts[i].join();
            } catch (Exception ignored) {
            }
        }
    }

    private void ungroupAllNodes() {
        for (SoftwarePackageNode node : nodes) {
            node.setGroup(false);
        }
    }

    public void removeTransitiveArcs() {
        System.out.print("Removing transitive arcs for " + nodes.size() + " nodes: ");

        List<PackageEdge> edges = new ArrayList<>(graph.edges());
        int checked = 0;

        for (PackageEdge edge : edges) {
            SoftwarePackageNode source = edge.from();
            SoftwarePackageNode target = edge.to();

            removeDependency(source, target);
            boolean hasAlternatePath = graph.hasPath(source, target);
            if (!hasAlternatePath) {
                addDependency(source, target);
            }

            checked++;
            if (checked > 1 && (checked % 200 == 0)) {
                System.out.print(".");
            }
        }

        System.out.println(" Ok!");
    }

    void anotateGroupNodeByName(String pattern) {
        String name = "_" + pattern;

        for (SoftwarePackageNode node : nodes) {
            if (node.getName().equals(name)) {
                node.setMark(true);
                node.setSource(true);
                return;
            }
        }
    }

    public void anotateGroups() {
        for (SoftwarePackageNode node : nodes) {
            if (node.getName().startsWith("[") && node.getMark()) {
                anotateGroupNodeByName(node.getName());
            }
        }
    }

    public void inundarV2(SoftwarePackageNode n) {
        ArrayDeque<SoftwarePackageNode> pending = new ArrayDeque<>();
        pending.push(n);

        while (!pending.isEmpty()) {
            SoftwarePackageNode current = pending.pop();
            if (current.getMark()) {
                continue;
            }
            current.setSource(true);
            current.setMark(true);

            for (SoftwarePackageNode next : graph.outgoingOf(current)) {
                pending.push(next);
            }
        }
    }

    public void inundarV3(SoftwarePackageNode n) {
        ArrayDeque<SoftwarePackageNode> pending = new ArrayDeque<>();
        pending.push(n);

        while (!pending.isEmpty()) {
            SoftwarePackageNode current = pending.pop();
            if (current.getMark()) {
                continue;
            }
            current.setSource(true);
            current.setMark(true);

            for (SoftwarePackageNode previous : graph.incomingOf(current)) {
                pending.push(previous);
            }
        }
    }

    public void initNodesAnotation() {
        for (SoftwarePackageNode node : nodes) {
            node.setSource(false);
            node.setSink(false);
            node.setMark(false);
        }
    }

    public void anotateSingleNode(String seed) {
        SoftwarePackageNode n = searchNodeByName(seed);
        if (n == null) {
            return;
        }
        n.setMark(true);
        n.setSource(true);
    }

    public void markPackageAndItsDependencies(String seed) {
        SoftwarePackageNode n = searchNodeByName(seed);
        if (n == null) {
            return;
        }
        n.setMark(true);
        n.setSource(true);
        n.setBoldName(true);

        for (SoftwarePackageNode child : graph.outgoingOf(n)) {
            inundarV2(child);
        }
    }

    public void markPackageAndItsClients(String seed) {
        SoftwarePackageNode n = searchNodeByName(seed);
        if (n == null) {
            return;
        }
        n.setMark(true);
        n.setSource(true);

        for (SoftwarePackageNode parent : graph.incomingOf(n)) {
            inundarV3(parent);
        }
    }

    public void anotateNodes(ArrayList<SoftwarePackageGroup> groups) {
        System.out.print("Anotating nodes... ");

        for (SoftwarePackageNode node : nodes) {
            node.setSource(graph.inDegreeOf(node) == 0);
            node.setSink(graph.outDegreeOf(node) == 0);
        }

        for (SoftwarePackageGroup group : groups) {
            SoftwarePackageNode header = group.header;
            for (SoftwarePackageNode n : graph.outgoingOf(header)) {
                n.setSource(true);
            }
        }

        System.out.println(" Ok!");
    }

    public void save(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filename));

            for (SoftwarePackageNode node : nodes) {
                String line = "n " + node.getName() + (node.getIsBad() ? " bad" : " good");
                PersistenceElement.writeAsciiLine(fos, line);
            }

            for (PackageEdge edge : graph.edges()) {
                SoftwarePackageNode source = edge.from();
                SoftwarePackageNode target = edge.to();
                String line = "r " + source.getName() + " " + target.getName();
                PersistenceElement.writeAsciiLine(fos, line);
            }

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOnList(ArrayList<SoftwarePackageNode> list, SoftwarePackageNode subject) {
        for (SoftwarePackageNode node : list) {
            if (node == subject) {
                return true;
            }
        }
        return false;
    }

    public void cleangroup(ArrayList<SoftwarePackageNode> list, SoftwarePackageNode groupParent) {
        Set<SoftwarePackageNode> groupSet = new HashSet<>(list);

        for (SoftwarePackageNode n : nodes) {
            if (groupParent.getName().equals(n.getName()) || groupSet.contains(n) || n.getParentGroupNode() == null) {
                continue;
            }

            List<SoftwarePackageNode> toRemove = new ArrayList<>();
            for (SoftwarePackageNode child : graph.outgoingOf(n)) {
                if (groupSet.contains(child)) {
                    toRemove.add(child);
                    if (n.getParentGroupNode() != null) {
                        System.err.println(n.getParentGroupNode().getName() + "." + n.getName() + " -> " + groupParent.getName() + "." + child.getName());
                        n.getParentGroupNode().addExternalGroupDependency(groupParent);
                    }
                }
            }

            for (SoftwarePackageNode child : toRemove) {
                removeDependency(n, child);
            }
        }
    }

    private void addGroupDependency(SoftwarePackageNode from, SoftwarePackageNode to) {
        String fromName = "_" + from.getName();
        String toName = "_" + to.getName();
        SoftwarePackageNode a = searchNodeByName(fromName);
        SoftwarePackageNode b = searchNodeByName(toName);
        if (a != null && b != null) {
            addDependency(a, b);
        } else {
            System.err.println("ERROR: broken links");
        }
    }

    public void addGroupNodes(ArrayList<SoftwarePackageGroup> groups) {
        SoftwarePackageGroup ng = new SoftwarePackageGroup("[STRUCTURE]");

        for (SoftwarePackageGroup group : groups) {
            SoftwarePackageNode n = addNode("_" + group.header.getName());
            ng.list.add(n);
        }

        for (SoftwarePackageGroup group : groups) {
            SoftwarePackageNode n = group.header;
            for (SoftwarePackageNode dependency : n.getExternalGroupDependencies()) {
                addGroupDependency(n, dependency);
            }
        }

        groups.add(ng);
    }

    public void reportNodesOutsideGroups() {
        for (SoftwarePackageNode n : nodes) {
            if (n.getParentGroupNode() == null && !n.getName().startsWith("[")) {
                System.out.println("  - Orphaned node: " + n.getName());
            }
        }
    }
}
