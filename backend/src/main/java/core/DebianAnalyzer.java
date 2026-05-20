package core;

//===========================================================================

import core.graphGenerators.CacheLoaderGenerator;
import core.graphGenerators.DebianPackageGenerator;
import core.graph.SoftwarePackageGraph;
import core.grouper.GroupSubgraphGrouper;
import core.grouper.SoftwarePackageGroup;
import core.grouper.TransitiveRelationReducer;
import core.highlight.MarkerJedilink;
import core.highlight.Markers;
import common.ProcessControl;
import java.nio.file.Files;
import java.util.ArrayList;

import java.io.File;

//===========================================================================

public class DebianAnalyzer {
    private SoftwarePackageGraph graph;
    private ArrayList<SoftwarePackageGroup> groups;
    private String outputFile;

    public DebianAnalyzer() {
        graph = new SoftwarePackageGraph();
        outputFile = "./output/general.dot";
    }
    
    private void exec(String args[]) {
        CacheLoaderGenerator cacheLoader = new CacheLoaderGenerator(graph);
        DebianPackageGenerator packageGenerator = new DebianPackageGenerator(graph);
        GroupSubgraphGrouper groupSubgraphGrouper = new GroupSubgraphGrouper(graph);
        TransitiveRelationReducer transitiveRelationReducer = new TransitiveRelationReducer(graph);

        //----------------------------------------------------------------
        File fd = new File("cache.txt");
        if ( fd.exists() ) {
            cacheLoader.generateFromCache(fd);
        }
        else {
            packageGenerator.generateFromSystemPackageManager();
            graph.save("cache.txt");
            System.out.println("Cache graph written to cache.txt");
        }
        fd = new File("cache_extra.txt");
        if ( fd.exists() ) {
            cacheLoader.generateFromCache(fd);
        }

        //----------------------------------------------------------------
        int i;
        SoftwarePackageGroup g;

        groups = new ArrayList<SoftwarePackageGroup>();
        for ( i = 0; i < args.length; i++ ) {
            g = new SoftwarePackageGroup(args[i], graph);
            groups.add(g);
        }

        //----------------------------------------------------------------
        graph.initNodesAnotation();
	Markers m = new Markers(graph);

        //m.markProhibited();
        //m.markGnustep();
        //m.markKDE();
        //m.markQT();
	//m.markGnome();
        //m.markGtk();
	//m.markGstreamer();
        //m.markMultimedia();
        //m.markJava();
        //m.markX11();
        //m.markDevel();
        //m.markPython();
        //m.markPythonDev();
        //m.markPerl();

        //m.markCompiled();
        //m.markBasic();
        //m.markMinimal();
        //m.markMediaMinimal();
        //m.markIntermediate();

	//m.markOpenGL();

        //m.markLinuxFromScratch();

        MarkerJedilink mj = new MarkerJedilink(graph);
        mj.markCustomJedilink();

        //graph.markPackageAndItsClients("raycasting/bidirectionalRaytracing/FlagChain.h");

        //graph.markPackageAndItsClients("GL/gl.h");
        //graph.markPackageAndItsClients("GL/glu.h");
        //graph.markPackageAndItsClients("GL/glx.h");
        //graph.markPackageAndItsClients("GL/osmesa.h");
        //graph.markPackageAndItsClients("GL/glut.h");
        //graph.markPackageAndItsClients("OpenGL/gl.h");
        //graph.markPackageAndItsClients("OpenGL/glu.h");
        //graph.markPackageAndItsClients("GLUT/glut.h");
        //graph.markPackageAndItsClients("render/Opengl.h");
        //graph.markPackageAndItsClients("GL/osmesa.h");

	//graph.markPackageAndItsDependencies("spring-cloud-starter");
        //graph.markPackageAndItsDependencies("org.junit:junit-bom:5.8.2");
        //graph.markPackageAndItsDependencies("com.fasterxml.jackson.core:jackson-core:2.15.2");
        //graph.markPackageAndItsDependencies("logback-classic");

        // Landmarks
        //graph.markPackageAndItsClients("cli-common"); // Mono
        //graph.markPackageAndItsClients("libmono0");
        //graph.markPackageAndItsClients("libmono-2.0-1");

        //graph.markPackageAndItsClients("libxfce4util4"); // XFCE

        //graph.markPackageAndItsClients("xserver-common"); // X11 server
        //graph.markPackageAndItsClients("x11-xserver-utils");

        // Known gnome applications
        //graph.markPackageAndItsDependencies("cheese");
        //graph.markPackageAndItsDependencies("gedit");
        //graph.markPackageAndItsDependencies("gnome-terminal");
        //graph.markPackageAndItsDependencies("dia");
        //graph.markPackageAndItsDependencies("gimp");
        //graph.markPackageAndItsDependencies("ghex");
        //graph.markPackageAndItsDependencies("gparted");
        //graph.markPackageAndItsDependencies("firefox");
        //graph.markPackageAndItsDependencies("flashplugin-installer");
        //graph.markPackageAndItsClients("gimp-help-common");
        //graph.markPackageAndItsClients("xfonts-utils");
        //graph.markPackageAndItsClients("libxaw7");
        //graph.markPackageAndItsClients("libva2");


        //graph.anotateNodes(groups);

        groupSubgraphGrouper.groupBySubgraphs(groups);
        transitiveRelationReducer.reduce();
        //graph.anotateNodes(groups);

        //----------------------------------------------------------------
        graph.exportDot(outputFile, groups);
        System.out.println("Graphviz graph has been generated on " + outputFile + "!");
        String eargs[] = new String[6];

        eargs[0] = "/usr/bin/dot";
        eargs[1] = "-Gnodesep=1";
        eargs[2] = "-Tdia";
        eargs[3] = outputFile;
        eargs[4] = "-o";
        eargs[5] = "./output/general.dia";

        Process proceso = ProcessControl.system(eargs);
        ProcessControl.finalizeProcess(proceso);

        //----------------------------------------------------------------
        System.out.print("Exporting individual groups: ");
        graph.labelLastGroup(groups);
        graph.exportDotByGroups(groups);
        graph.exportGroups(groups);
        graph.exportCleanScripts(groups);
        graph.exportInstallScripts(groups);
        graph.exportTopNodesPerGroups(groups);
        System.out.println(" Ok!");
    }

    public void run(String[] args) {
        exec(args);
    }
}
