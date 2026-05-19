package co.cubestudio;

public class Util {
    public static Process system(String args[]) 
    {
        Process p;
        Runtime rt = Runtime.getRuntime();
        try {
            p = rt.exec(args);
        }
        catch ( Exception error ) {
		/*
	    if (args != null) {
                System.out.println("<system>" + error + " / ARR: " + args.length);
	    } else {
                System.out.println("<system>" + error + " / NULL");
	    }
            error.printStackTrace();
	    for (int i = 0; i < args.length; i++) {
	        System.out.println(args[i]);
	    }
		*/
            p = null;
        }
        return p;
    }

    public static void finalizeProcess(Process process)
    {

        try {
            //process.waitFor();
            process.destroy();
        }
        catch ( Exception e ) {
            System.out.println("<FileAnalyzerMd5sumSignature.finalizeProcess>" + e);
            e.printStackTrace();
        }
    }
}
