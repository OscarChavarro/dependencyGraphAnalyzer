package co.cubestudio;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import vsdk.toolkit.io.PersistenceElement;

public class SoftwarePackageGraph {
    private ArrayList<SoftwarePackageNode> nodes;

    private Hashtable<String, SoftwarePackageNode> nodeIndex;

    public SoftwarePackageGraph() {
        nodes = new ArrayList<SoftwarePackageNode>();
        nodeIndex = new Hashtable<String, SoftwarePackageNode>();
    }

    private String
    normalizeFilename(String in) {
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

    public SoftwarePackageNode
    addNode(String name) {
        SoftwarePackageNode n;

        int i;
        for (i = 0; i < nodes.size(); i++) {
            n = nodes.get(i);
            if (name.equals(n.getName())) {
                return n;
            }
        }

        n = new SoftwarePackageNode(name);
        nodes.add(n);
        nodeIndex.put(name, n);
        return n;
    }

    public void
    addBadNode(String name) {
        SoftwarePackageNode n;
        n = new SoftwarePackageNode(name);
        n.markAsBad();
        nodes.add(n);
        nodeIndex.put(name, n);
    }

    public SoftwarePackageNode
    searchNodeByName(String n) {
        return nodeIndex.get(n);
    }

    public ArrayList<SoftwarePackageNode>
    getNodes() {
        return nodes;
    }

    public void
    labelLastGroup(ArrayList<SoftwarePackageGroup> groups) {
        //n.setVariant(groups.get(i).areAllSources());
        SoftwarePackageGroup last = groups.get(groups.size() - 1);
        markVariants(last, groups);
    }

    public void
    markVariants(
            SoftwarePackageGroup general,
            ArrayList<SoftwarePackageGroup> groups) {
        for (SoftwarePackageNode n : general.list) {
            n.setVariant(false);
            for (SoftwarePackageGroup g : groups) {
                if (n.getName().contains(g.header.getName())) {
                    if (g.areAllSources()) {
                        n.setVariant(true);
                    }
                    continue;
                }
            }
        }
    }

    public void
    exportDotNode(OutputStream os, SoftwarePackageNode node, boolean indent)
            throws Exception {
        String n;

        if (node.getName().startsWith("_[") &&
                !node.getName().startsWith("_[00") &&
                node.getChildren().size() <= 0) {
            return;
        }

        n = "    \"" + node.getName() + "\"";
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
            //if (node.getSource()) {
            //    shape += ",peripheries=3";
            //} else {
            shape += ",peripheries=2";
            //}
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

    private void
    exportDotNodes(OutputStream os, SoftwarePackageGroup g, int i)
            throws Exception {
        PersistenceElement.writeAsciiLine(os, "    subgraph cluster" + i + " {");
        PersistenceElement.writeAsciiLine(os, "        color=black;");
        PersistenceElement.writeAsciiLine(os, "        fillcolor=lightgray;");
        PersistenceElement.writeAsciiLine(os, "        style=\"filled,bold\";");
        exportDotNode(os, g.header, true);
        g.header.setGroup(true);

        int j;
        for (j = 0; j < g.list.size(); j++) {
            exportDotNode(os, g.list.get(j), true);
            g.list.get(j).setGroup(true);
        }
        PersistenceElement.writeAsciiLine(os, "    }");
        PersistenceElement.writeAsciiLine(os, "    ");
    }

    public void
    exportDot(String filename, ArrayList<SoftwarePackageGroup> groups) {
        exportDot(filename, groups, -1);
    }

    private boolean namesAreGroupsAndFirstGreater(String a, String b) {
        StringTokenizer parser1;
        StringTokenizer parser2;

        if (!a.startsWith("_[") || !b.startsWith("_[")) {
            return false;
        }

        parser1 = new StringTokenizer(a, "[]_");
        parser2 = new StringTokenizer(b, "[]_");
        int na, nb;

        na = Integer.parseInt(parser1.nextToken());
        nb = Integer.parseInt(parser2.nextToken());

        if (na < nb) {
            return true;
        }
        return false;
    }

    private void
    exportDot(String filename, ArrayList<SoftwarePackageGroup> groups, int id) {
        try {
            File fd = new File(filename);
            FileOutputStream fos = new FileOutputStream(fd);
            int i, j;

            PersistenceElement.writeAsciiLine(fos, "digraph G {");
            PersistenceElement.writeAsciiLine(fos, "    //= NODES ==============================================================");

            ungroupAllNodes();

            if (id == -1) {
                for (i = 0; i < groups.size(); i++) {
                    exportDotNodes(fos, groups.get(i), i);
                }
            } else {
                exportDotNodes(fos, groups.get(id), id);
            }

            if (id == -1) {
                System.out.print("Exporting nodes not in groups...");
                for (i = 0; i < nodes.size(); i++) {
                    if ((nodes.get(i).getGroup() == false) &&
                            !(nodes.get(i).getName().startsWith("[")) &&
                            !(nodes.get(i).getName().startsWith("_["))) {
                        exportDotNode(fos, nodes.get(i), false);
                        //System.err.println(nodes.get(i).getName());
                    }
                }
                System.out.println(" Ok!");
            }

            PersistenceElement.writeAsciiLine(fos, "\n    //= ARCS ===============================================================");
            ArrayList<SoftwarePackageNode> children;

            for (i = 0; i < nodes.size(); i++) {
                children = nodes.get(i).getChildren();
                for (j = 0; j < children.size(); j++) {
                    String a;
                    String b;
                    String r;

                    if (nodes.get(i) == null || children.get(j) == null) {
                        continue;
                    }
                    a = nodes.get(i).getName();
                    b = children.get(j).getName();

                    r = "    \"" + a + "\" -> \"" + b + "\";";
                    if (namesAreGroupsAndFirstGreater(a, b)) {
                        r = "    \"" + a + "\" -> \"" + b + "\" [color=\"red\"];";
                        //System.out.println(a + "->" + b);
                    }

                    if ((id == -1) || groups.get(id).contains(children.get(j))) {
                        PersistenceElement.writeAsciiLine(fos, r);
                    }
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
            int i;
            int j;
            String filename;
            File fd;
            FileOutputStream fos;
            BufferedOutputStream bos;
            SoftwarePackageNode n;

            ungroupAllNodes();
            for (i = 0; i < groups.size(); i++) {
                SoftwarePackageNode parent;

                parent = groups.get(i).header;

                if (parent.getChildren().size() == 0) {
                    continue;
                }

                parent.setGroup(true);
                filename = "./output/txt/" + normalizeFilename(groups.get(i).header.getName()) + ".txt";
                fd = new File(filename);
                fos = new FileOutputStream(fd);
                bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, parent.getName());
                PersistenceElement.writeAsciiLine(bos, "#- Top packages ------------------------------------------------------------");

                for (j = 0; j < parent.getChildren().size(); j++) {
                    n = parent.getChildren().get(j);
                    n.setGroup(true);
                    PersistenceElement.writeAsciiLine(bos, n.getName());
                }
                PersistenceElement.writeAsciiLine(bos, "#- Dependent packages ------------------------------------------------------");
                for (j = 0; j < groups.get(i).list.size(); j++) {
                    n = groups.get(i).list.get(j);
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
            int i;
            int j;
            String filename;
            File fd;
            FileOutputStream fos;
            BufferedOutputStream bos;
            SoftwarePackageNode n;

            ungroupAllNodes();
            for (i = 0; i < groups.size(); i++) {
                SoftwarePackageNode parent;

                parent = groups.get(i).header;

                if (parent.getChildren().size() == 0) {
                    continue;
                }

                parent.setGroup(true);
                filename = "./output/cleansh/" + normalizeFilename(groups.get(i).header.getName()) + ".sh";
                fd = new File(filename);
                fos = new FileOutputStream(fd);
                bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, "# " + parent.getName());

                String command = "apt-get purge ";

                for (j = 0; j < parent.getChildren().size(); j++) {
                    n = parent.getChildren().get(j);
                    n.setGroup(true);
                    command = command + n.getName() + " ";
                }
                for (j = 0; j < groups.get(i).list.size(); j++) {
                    n = groups.get(i).list.get(j);
                    if (!n.getGroup()) {
                        command = command + n.getName() + " ";
                    }
                }
                PersistenceElement.writeAsciiLine(bos, command);

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
            int i;
            int j;
            String filename;
            File fd;
            FileOutputStream fos;
            BufferedOutputStream bos;
            SoftwarePackageNode n;

            ungroupAllNodes();
            for (i = 0; i < groups.size(); i++) {
                SoftwarePackageNode parent;

                parent = groups.get(i).header;

                if (parent.getChildren().size() == 0) {
                    continue;
                }

                parent.setGroup(true);
                filename = "./output/tops/" + normalizeFilename(groups.get(i).header.getName()) + ".sh";
                fd = new File(filename);
                fos = new FileOutputStream(fd);
                bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, "# " + parent.getName());

                String command = "apt-get install ";
                int count = 0;

                for (j = 0; j < parent.getChildren().size(); j++) {
                    n = parent.getChildren().get(j);
                    n.setGroup(true);
                    if (n.getBoldName()) {
                        command = command + n.getName() + " ";
                        count++;
                    }
                }
                for (j = 0; j < groups.get(i).list.size(); j++) {
                    n = groups.get(i).list.get(j);
                    if (!n.getGroup() && n.getBoldName()) {
                        command = command + n.getName() + " ";
                        count++;
                    }
                }

                if (count == 0) {
                    PersistenceElement.writeAsciiLine(bos, "# no top nodes");
                } else {
                    PersistenceElement.writeAsciiLine(bos, command);
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
            int i;
            int j;
            String filename;
            File fd;
            FileOutputStream fos;
            BufferedOutputStream bos;
            SoftwarePackageNode n;

            ungroupAllNodes();
            for (i = 0; i < groups.size(); i++) {
                SoftwarePackageNode parent;

                parent = groups.get(i).header;

                if (parent.getChildren().size() == 0) {
                    continue;
                }

                parent.setGroup(true);
                filename = "./output/installsh/" + normalizeFilename(groups.get(i).header.getName()) + ".sh";
                fd = new File(filename);
                fos = new FileOutputStream(fd);
                bos = new BufferedOutputStream(fos);

                PersistenceElement.writeAsciiLine(bos, "# " + parent.getName());

                String command = "apt-get install ";

                for (j = 0; j < parent.getChildren().size(); j++) {
                    n = parent.getChildren().get(j);
                    n.setGroup(true);
                    //if (n.getMark()) {
                    command = command + n.getName() + " ";
                    //}
                }
                PersistenceElement.writeAsciiLine(bos, command);

                bos.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    public void
    exportDotByGroups(ArrayList<SoftwarePackageGroup> groups) {
        int i;

        int N = 24;
        ArrayList<ArrayList<String[]>> egroups = new ArrayList<ArrayList<String[]>>();
        for (i = 0; i < N; i++) {
            egroups.add(new ArrayList<String[]>());
        }

        for (i = 0; i < groups.size(); i++) {
            String filename;
            System.out.print(".");
            filename = "./output/dot/" + normalizeFilename(normalizeFilename(groups.get(i).header.getName())) + ".dot";

            SoftwarePackageNode parent;
            String args[] = new String[6];

            parent = groups.get(i).header;
            if (parent.getChildren().size() == 0 &&
                    !parent.getName().equals("[STRUCTURE]")) {
                continue;
            }

            exportDot(filename, groups, i);
            args[0] = "/usr/bin/dot";
            args[1] = "-Gnodesep=0";
            args[2] = "-Tpng";
            args[3] = filename;
            args[4] = "-o";
            args[5] = "./output/png/" + normalizeFilename(groups.get(i).header.getName()) + ".png";
            egroups.get(i % N).add(args);
        }

        Thread ts[] = new Thread[N];
        for (i = 0; i < N; i++) {
            DotRunner unit = new DotRunner(egroups.get(i));
            ts[i] = new Thread(unit);
            ts[i].start();
        }

        for (i = 0; i < N; i++) {
            try {
                ts[i].join();
            } catch (Exception e) {
            }
        }
    }

    private boolean
    thereIsAPath(SoftwarePackageNode a, SoftwarePackageNode b, SoftwarePackageNode exclude) {
        int i;

        if (a == null || b == null || a.getMark() == true || a == exclude) {
            return false;
        }

        ArrayList<SoftwarePackageNode> children = a.getChildren();
        a.setMark(true);

        for (i = 0; i < children.size(); i++) {
            if (children.get(i) == b ||
                    thereIsAPath(children.get(i), b, exclude)) {
                return true;
            }
        }
        return false;
    }

    private void
    unmarkAllNodes() {
        int i;
        for (i = 0; i < nodes.size(); i++) {
            nodes.get(i).setMark(false);
        }
    }

    private void
    ungroupAllNodes() {
        int i;
        for (i = 0; i < nodes.size(); i++) {
            nodes.get(i).setGroup(false);
        }
    }

    public void
    removeTransitiveArcs() {
        System.out.print("Removing transitive arcs for " + nodes.size() + " nodes: ");
        int i, j, k;

        for (i = 0; i < nodes.size(); i++) {
            //System.out.print(".");
            if (i > 1 && (i % 200 == 0)) {
                //System.out.print(" [" + i + "] ");
                System.out.print(".");
            }
            ArrayList<SoftwarePackageNode> children = nodes.get(i).getChildren();
            SoftwarePackageNode source, target;
            for (j = 0; j < children.size(); j++) {
                target = children.get(j);
                for (k = 0; k < children.size(); k++) {
                    if (j != k) {
                        source = children.get(k);
                        unmarkAllNodes();
                        if (thereIsAPath(source, target, nodes.get(i))) {
                            children.remove(j);
                            j--;
                            //System.out.print("X");
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(" Ok!");
    }

    void
    anotateGroupNodeByName(String pattern) {
        int i;
        String name = "_" + pattern;

        for (i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getName().equals(name)) {
                nodes.get(i).setMark(true);
                nodes.get(i).setSource(true);
                return;
            }
        }
    }

    public void
    anotateGroups() {
        int i;
        for (i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getName().startsWith("[") &&
                    nodes.get(i).getMark()) {
                anotateGroupNodeByName(nodes.get(i).getName());
            }
        }
    }

    public void
    inundarV2(SoftwarePackageNode n) {
        int i;

        if (n.getMark() == true) {
            return;
        }
        n.setSource(true);
        n.setMark(true);

        ArrayList<SoftwarePackageNode> children = n.getChildren();
        for (i = 0; i < children.size(); i++) {
            inundarV2(children.get(i));
        }
    }

    public void
    inundarV3(SoftwarePackageNode n) {
        int i, j;

        if (n.getMark() == true) {
            return;
        }
        n.setSource(true);
        n.setMark(true);

        SoftwarePackageNode p;
        for (i = 0; i < nodes.size(); i++) {
            p = nodes.get(i);
            ArrayList<SoftwarePackageNode> brothers = p.getChildren();
            for (j = 0; j < brothers.size(); j++) {
                if (brothers.get(j) == n) {
                    inundarV3(p);
                }
            }
        }
    }

    public void
    initNodesAnotation() {
        int i;

        for (i = 0; i < nodes.size(); i++) {
            nodes.get(i).setSource(false);
            nodes.get(i).setSink(false);
            nodes.get(i).setMark(false);
        }
    }

    public void
    anotateSingleNode(String seed) {
        SoftwarePackageNode n;
        n = searchNodeByName(seed);
        if (n == null) {
            return;
        }
        n.setMark(true);
        n.setSource(true);
    }

    public void
    anotateNodesV2(String seed) {
        int i;

        SoftwarePackageNode n;
        n = searchNodeByName(seed);
        if (n == null) {
            return;
        }
        n.setMark(true);
        n.setSource(true);
        n.setBoldName(true);

        ArrayList<SoftwarePackageNode> children = n.getChildren();
        for (i = 0; i < children.size(); i++) {
            inundarV2(children.get(i));
        }
    }

    public void
    anotateNodesV3(String seed) {
        int i, j;

        SoftwarePackageNode n, p;
        n = searchNodeByName(seed);
        if (n == null) {
            return;
        }
        n.setMark(true);
        n.setSource(true);

        for (i = 0; i < nodes.size(); i++) {
            p = nodes.get(i);
            ArrayList<SoftwarePackageNode> brothers = p.getChildren();
            for (j = 0; j < brothers.size(); j++) {
                if (n == brothers.get(j)) {
                    inundarV3(p);
                }
            }
        }
    }

    public void
    anotateNodes(ArrayList<SoftwarePackageGroup> groups) {
        System.out.print("Anotating nodes... ");
        int i, j, k;

        for (i = 0; i < nodes.size(); i++) {
            nodes.get(i).setSource(true);
            nodes.get(i).setSink(false);
        }

        for (i = 0; i < nodes.size(); i++) {
            ArrayList<SoftwarePackageNode> children = nodes.get(i).getChildren();
            if (children.size() == 0) {
                nodes.get(i).setSink(true);
            }

            for (j = 0; j < children.size(); j++) {
                children.get(j).setSource(false);
            }

        }

        for (i = 0; i < groups.size(); i++) {
            SoftwarePackageNode n;
            n = groups.get(i).header;
            for (j = 0; j < n.getChildren().size(); j++) {
                n.getChildren().get(j).setSource(true);
            }
        }

        System.out.println(" Ok!");
    }

    public void save(String filename) {
        try {
            File fd = new File(filename);
            FileOutputStream fos = new FileOutputStream(fd);

            int i, j;
            String line;

            for (i = 0; i < nodes.size(); i++) {
                line = "n " + nodes.get(i).getName() + ((nodes.get(i).getIsBad()) ? " bad" : " good");
                PersistenceElement.writeAsciiLine(fos, line);
            }

            ArrayList<SoftwarePackageNode> children;
            for (i = 0; i < nodes.size(); i++) {
                children = nodes.get(i).getChildren();
                for (j = 0; j < children.size(); j++) {
                    line = "r " + nodes.get(i).getName() + " " + children.get(j).getName();
                    PersistenceElement.writeAsciiLine(fos, line);
                }
            }

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOnList(ArrayList<SoftwarePackageNode> list,
                             SoftwarePackageNode subject) {
        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i) == subject) {
                return true;
            }
        }
        return false;
    }

    /**
     * Given a list of graph nodes (that are part of the same group), this routine
     * simplifies the graph, to contain references to groupnode, instead of
     * dependant list.
     */
    public void cleangroup(ArrayList<SoftwarePackageNode> list,
                           SoftwarePackageNode groupParent) {
        ArrayList<SoftwarePackageNode> children;
        int i, j;
        SoftwarePackageNode n;

        for (i = 0; i < nodes.size(); i++) {
            n = nodes.get(i);
            if (groupParent.getName().equals(n.getName()) ||
                    isOnList(list, n) || n.getParentGroupNode() == null) {
                continue;
            }
            children = n.getChildren();
            for (j = 0; j < children.size(); j++) {
                SoftwarePackageNode cj = children.get(j);
                if (isOnList(list, cj)) {
                    children.remove(j);
                    j--;
                    if (n.getParentGroupNode() != null) {
                        System.err.println(n.getParentGroupNode().getName() + "." + n.getName() + " -> " + groupParent.getName() + "." + cj.getName());
                        n.getParentGroupNode().addExternalGroupDependency(
                                groupParent);
                    }
                }
            }
        }
    }

    private void addGroupDependency(SoftwarePackageNode from,
                                    SoftwarePackageNode to) {
        String fromName = "_" + from.getName();
        String toName = "_" + to.getName();
        SoftwarePackageNode a;
        SoftwarePackageNode b;
        a = searchNodeByName(fromName);
        b = searchNodeByName(toName);
        if (a != null && b != null) {
            a.addChild(b);
        } else {
            System.err.println("ERROR: broken links");
        }
    }

    /**
     * Given a set of groups, this method adds a new node to the graph
     * for each group, and groups them in to a new node, in order to give
     * a brief/abstract group that documents the superstructure of the
     * whole graph.
     */
    public void
    addGroupNodes(ArrayList<SoftwarePackageGroup> groups) {
        SoftwarePackageGroup ng;
        SoftwarePackageNode n;
        int i;
        int j;

        ng = new SoftwarePackageGroup("[STRUCTURE]");
        for (i = 0; i < groups.size(); i++) {
            n = addNode(("_" + groups.get(i).header.getName()));
            ng.list.add(n);
        }

        for (i = 0; i < groups.size(); i++) {
            n = groups.get(i).header;
            for (j = 0;
                 j < n.getExternalGroupDependencies().size();
                 j++) {
                addGroupDependency(n, n.getExternalGroupDependencies().get(j));
            }
        }

        groups.add(ng);
    }

    public void reportNodesOutsideGroups() {
        int i;
        for (i = 0; i < nodes.size(); i++) {
            SoftwarePackageNode n;

            n = nodes.get(i);
            if (n.getParentGroupNode() == null &&
                    !n.getName().startsWith("[")) {
                System.out.println("  - Orphaned node: " + n.getName());
            }
        }
    }
}
