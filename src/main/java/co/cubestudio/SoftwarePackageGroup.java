package co.cubestudio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SoftwarePackageGroup
{
    public ArrayList<SoftwarePackageNode> list;
    public SoftwarePackageNode header;

    public SoftwarePackageGroup(String headerName)
    {
        header = new SoftwarePackageNode(headerName);
        list = new ArrayList<SoftwarePackageNode>();
    }

    public SoftwarePackageGroup(String file, SoftwarePackageGraph graph)
    {
	System.out.println("  - Reading group " + file);

        //-----------------------------------------------------------------
        BufferedReader br;
        boolean headerMark = true;
        SoftwarePackageNode a = null, b;

        list = new ArrayList<SoftwarePackageNode>();

        try {
            br = new BufferedReader(new FileReader(file));
            String lineOfText;
            String name;

            while ( (lineOfText = br.readLine()) != null ) {
                if ( lineOfText.startsWith("#") ) {
                    continue;
		}
                StringTokenizer parser;
                parser = new StringTokenizer(lineOfText, " \n");
                name = parser.nextToken();
                if ( name == null ) {
                    continue;
		}
                if ( headerMark ) {
                    headerMark = false;
                    graph.addNode(name);
                    a = graph.searchNodeByName(name);
                    header = a;
		}
		else {
                    b = graph.searchNodeByName(name);
                    if ( a != null && b != null ) {
                        b.setParentGroupNode(a);
                        graph.addDependency(a, b);
                        list.add(b);
		    }
		}
	    }
	}
	catch ( Exception e ) {
            e.printStackTrace();
	}

        //-----------------------------------------------------------------
    }

    public boolean
    contains(SoftwarePackageNode n) {
        int i;
        for ( i = 0; i < list.size(); i++ ) {
            if ( n == list.get(i) ) {
                return true;
	    }
	}
	return false;
    }
    
    public boolean
    areAllSources() {
        int i;
        for ( i = 0; i < list.size(); i++ ) {
            if ( list.get(i).getSource() ) {
                return false;
	    }
	}
	return true;
    }
}
