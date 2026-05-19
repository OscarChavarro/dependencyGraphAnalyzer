package co.cubestudio;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.StringTokenizer;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BuilderFromDpkg {
    private SoftwarePackageGraph graph;

    public BuilderFromDpkg(SoftwarePackageGraph graph) {
	this.graph = graph;
    }

    public void buildFromDpkg() {
        //-----------------------------------------------------------------
        String args[] = new String[2];
        args[0] = "dpkg-query";
        args[1] = "-l";

        Process proceso = Util.system(args);

        buildGraphNodesFromDpkg(proceso);

        Util.finalizeProcess(proceso);

        proceso = null;
        System.gc(); 

        //----------------------------------------------------------------
        ArrayList<SoftwarePackageNode> nodes = graph.getNodes();
        int i;

        System.out.print("Checking " + nodes.size() + " packages: ");

        int N = 72;
	Thread ts[] = new Thread[N];
	DebReader processors[] = new DebReader[N];
	for (i = 0; i < N; i++) {
	    processors[i] = new DebReader();
	    ts[i] = new Thread(processors[i]);
	}
	
        for ( i = 0; i < nodes.size(); i++ ) {
	    processors[i % N].inputs.add(nodes.get(i));
        }
	for (i = 0; i < N; i++) {
  	    ts[i].start();
	}
	for (i = 0; i < N; i++) {
	    try {
  	        ts[i].join();
	    } catch (Exception e) {
	    }
	}
	for (i = 0; i < N; i++) {
	    for (int j = 0; j < processors[i].outputs.size(); j++) {
                addDeps(processors[i].outputs.get(j));
	    }
	}

        System.out.println(" Ok!");
    }

    private void addDeps(DataElement e) {
        for (String key: e.deps) {
            SoftwarePackageNode child;
            child = graph.searchNodeByName(key);

            if ( child != null ) {
                e.node.addChild(child);
            }
            else {
                graph.addBadNode(key);
                child = graph.searchNodeByName(key);
                e.node.addChild(child);
            }
        }
    }
    
    private void buildGraphNodesFromDpkg(Process proceso) 
    {
        //-------------------------------------------------------------
        int i, n = 0;

        InputStream processOutput = proceso.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(processOutput));

        try {
            String line = null;
            do {
                line = br.readLine();
                String token;
                if ( line != null ) {
                    StringTokenizer parser;
                    parser = new StringTokenizer(line, " \n");
                    token = parser.nextToken();
                    if ( token.equals("ii") ) {
                        token = parser.nextToken();
                        graph.addNode(token);
                    }
                }
            } while ( line != null );
        }
        catch ( Exception error ) {
            System.out.println("ERROR intentando leer salida standard del comando!");
            System.out.println("<FileAnalyzerMd5sumSignature.buildGraphNodesFromDpkg>" + error);
            error.printStackTrace();

        }

        //-------------------------------------------------------------
        for ( i = 0; i < graph.getNodes().size(); i++ ) {
            String nn = graph.getNodes().get(i).getName();
            if ( nn.indexOf(':') != -1 ) {
                String x = nn.substring(0, nn.indexOf(':'));
                //System.out.println(x + " is substring of " + nn);
                SoftwarePackageNode o, d;
                o = graph.searchNodeByName(nn);
                d = graph.addNode(x);
                o.addChild(d);
                d.addChild(o);
            }
        }
    }
}
