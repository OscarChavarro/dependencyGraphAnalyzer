package co.cubestudio;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class DotRunner implements Runnable {
    private ArrayList<String[]> egroup;

    public DotRunner(ArrayList<String[]> egroup) {
	this.egroup = egroup;
    }

    private void
    fillExportDot(Process proceso) {
        int i, n = 0;

        InputStream processOutput = proceso.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(processOutput));

        try {
            String line = null;
            do {
                line = br.readLine();
		if ( line != null ) {
		    System.out.print(line);
		}
            } while ( line != null );
        }
        catch ( Exception error ) {
            System.out.println("ERROR intentando leer salida standard del comando!");
            System.out.println("<FileAnalyzerMd5sumSignature.buildGraphNodes>" + error);
            error.printStackTrace();

        }
    }

    public void
    run() {
	for (String[] args: egroup) {
	    if (args == null || args[3] == null) {
		System.out.println("  - [WARNING: GROUP SKIPPED]");
		continue;
	    }
            Process proceso = Util.system(args);
            fillExportDot(proceso);
            Util.finalizeProcess(proceso);
	}
    }
}
