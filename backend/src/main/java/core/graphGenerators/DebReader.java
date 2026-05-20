package core.graphGenerators;

import common.ProcessControl;
import core.graph.SoftwarePackageNode;
import java.util.ArrayList;
import java.util.TreeSet;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class DebReader implements Runnable {
    public ArrayList<SoftwarePackageNode> inputs;
    public ArrayList<DataElement> outputs;

    public DebReader() {
	inputs = new ArrayList<>();
	outputs = new ArrayList<>();
    }
    
    public void run() {
	for (SoftwarePackageNode node: inputs) {
	    buildNodeArcs(node);
	}
    }

    public void
    buildNodeArcs(SoftwarePackageNode node) {
        String args[];
        args = new String[3];
        args[0] = "apt-cache";
        args[1] = "depends";
        args[2] = node.getName();

        Process proceso = ProcessControl.system(args);

        int i, n = 0;

        InputStream processOutput = proceso.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(processOutput));

        System.out.print(".");

        try {
            String line = null;
            boolean hard = true;

            TreeSet<String> deps = new TreeSet<String>();
            do {
                line = br.readLine();

                if ( line != null && line.contains("Depends: ")) {
                    //-- query dependency --
		    int start = line.indexOf("Depends: ");
		    if (start < 0) {
			continue;
		    }
		    String key = line.substring(start + 9);
		    if (!deps.contains(key) && !key.contains("<")) {
		        deps.add(key);
		    }
		    //System.out.println("  - [" + key + "]");
                }
            } while ( line != null );

            DataElement e = new DataElement();
	    e.deps = deps;
	    e.node = node;
	    outputs.add(e);
        }
        catch ( Exception error ) {
            System.out.println("ERROR intentando leer salida standard del comando!");
            System.out.println("<FileAnalyzerMd5sumSignature.buildGraphNodesFromDpkg>" + error);
            error.printStackTrace();

        }
        ProcessControl.finalizeProcess(proceso);
        proceso = null;
        System.gc();
    }
}
