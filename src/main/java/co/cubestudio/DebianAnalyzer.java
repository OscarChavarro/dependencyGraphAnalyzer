package co.cubestudio;

//===========================================================================

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;

//===========================================================================

public class DebianAnalyzer {
    private SoftwarePackageGraph graph;
    private ArrayList<SoftwarePackageGroup> groups;
    private String outputFile;

    public DebianAnalyzer() {
        graph = new SoftwarePackageGraph();
        outputFile = "./output/general.dot";
    }

    private void buildFromCache(File fd)
    {
        System.out.print("Building graph from cache ...");

        String name = null;
        String state = null;
        String source = null;
        String target = null;
        String line = null;
        try {
            FileInputStream fos = new FileInputStream(fd);
            BufferedReader br = new BufferedReader(new InputStreamReader(fos));
            SoftwarePackageNode a, b;
 
            do {
                line = br.readLine();
                String token;
                if ( line != null ) {
                    StringTokenizer parser;
                    parser = new StringTokenizer(line, " \n");
                    token = parser.nextToken();
                    if ( token.equals("n") ) {
                        name = parser.nextToken();			
                        state = parser.nextToken();
                        if ( state.equals("good") ) {
                            graph.addNode(name);
                        }
                        else {
                            graph.addBadNode(name);
                        }
                    }
                    else if ( token.equals("r") ) {
                        source = new String(parser.nextToken());
                        target = new String(parser.nextToken());
                        a = graph.searchNodeByName(source);
                        b = graph.searchNodeByName(target);
                        if ( a == null ) {
                            System.err.println("No puedo leer nodo " + source);
                        }
                        graph.addDependency(a, b);
                    }

                }
            } while ( line != null );
        }
        catch ( Exception error ) {
            System.out.println("ERROR intentando leer cache!");
            System.out.println("<FileAnalyzerMd5sumSignature.buildGraphNodesFromDpkg>" + error);
            System.out.println("  - Line: " + line);
            System.out.println("  - Name: " + name);
            System.out.println("  - State: " + state);
            System.out.println("  - Source: " + source);
            System.out.println("  - Target: " + target);
            error.printStackTrace();

        }


        System.out.println("Ok!");
    }
    
    private void exec(String args[]) {
        //----------------------------------------------------------------
        File fd = new File("cache.txt");
        if ( fd.exists() ) {
            buildFromCache(fd);
        }
        else {
            BuilderFromDpkg builder = new BuilderFromDpkg(graph);
	    builder.buildFromDpkg();
            graph.save("cache.txt");
            System.out.println("Cache graph written to cache.txt");
        }
        fd = new File("cache_extra.txt");
        if ( fd.exists() ) {
            buildFromCache(fd);
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

        graph.reportNodesOutsideGroups();

        for ( i = 0; i < groups.size(); i++ ) {
            g = groups.get(i);
            graph.cleangroup(g.list, g.header);
        }

        //----------------------------------------------------------------
        graph.addGroupNodes(groups); // Depends on cleangroup results
        graph.anotateGroups();

        graph.removeTransitiveArcs();
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

        Process proceso = Util.system(eargs);
        Util.finalizeProcess(proceso);

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

    public static void main(String[] args) {
        DebianAnalyzer instance = new DebianAnalyzer();
        List<String> expandedArgs = new ArrayList<>();

        for (String arg : args) {
            if (arg.contains("*") || arg.contains("?")) {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + arg);
                Path parent = Paths.get(arg).getParent();
                if (parent == null) parent = Paths.get(".");

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

        instance.exec(expandedArgs.toArray(new String[0]));
    }
}
