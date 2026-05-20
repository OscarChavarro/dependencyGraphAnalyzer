package core.highlight;

import core.graph.SoftwarePackageGraph;
import java.nio.file.Paths;

public class Markers {
    private static final String DEFAULT_RULES_FILE = "etc/highlightRules/markers-rules.json";
    private final SoftwarePackageGraph graph;
    private final HighlightRulesEngine engine;

    public Markers(SoftwarePackageGraph graph) {
        this.graph = graph;
        this.engine = HighlightRulesEngine.fromJsonFile(graph, Paths.get(DEFAULT_RULES_FILE));
    }

    private void apply(String profile) {
        engine.applyProfile(profile);
    }

    public void markProhibited() { apply("markProhibited"); }

    public void markBasic() { apply("markBasic"); }

    public void markMultimedia() { apply("markMultimedia"); }

    public void markJava() { apply("markJava"); }

    public void markX11() { apply("markX11"); }

    public void markOpenGL() { apply("markOpenGL"); }

    public void markDevel() { apply("markDevel"); }

    public void markKDE() { apply("markKDE"); }

    public void markGnome() { apply("markGnome"); }

    public void markPerl() { apply("markPerl"); }

    public void markPythonDev() { apply("markPythonDev"); }

    public void markPython() { apply("markPython"); }

    public void markQT() { apply("markQT"); }

    public void markGstreamer() { apply("markGstreamer"); }

    public void markGtk() { apply("markGtk"); }

    public void markGnustep() { apply("markGnustep"); }

    public void markMediaMinimal() { apply("markMediaMinimal"); }

    public void markMinimal() { apply("markMinimal"); }

    public void markIntermediate() { apply("markIntermediate"); }

    public void markLinuxFromScratch() { apply("markLinuxFromScratch"); }

    public void markMultiplatform(String n) {
        graph.anotateSingleNode(n);
        graph.anotateSingleNode(n + ":amd64");
        graph.anotateSingleNode(n + ":i386");
    }
}
