package co.cubestudio;

import co.cubestudio.highlight.HighlightRulesEngine;
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

    void markProhibited() { apply("markProhibited"); }

    void markBasic() { apply("markBasic"); }

    void markMultimedia() { apply("markMultimedia"); }

    void markJava() { apply("markJava"); }

    void markX11() { apply("markX11"); }

    void markOpenGL() { apply("markOpenGL"); }

    void markDevel() { apply("markDevel"); }

    void markKDE() { apply("markKDE"); }

    void markGnome() { apply("markGnome"); }

    void markPerl() { apply("markPerl"); }

    void markPythonDev() { apply("markPythonDev"); }

    void markPython() { apply("markPython"); }

    void markQT() { apply("markQT"); }

    void markGstreamer() { apply("markGstreamer"); }

    void markGtk() { apply("markGtk"); }

    void markGnustep() { apply("markGnustep"); }

    void markMediaMinimal() { apply("markMediaMinimal"); }

    void markMinimal() { apply("markMinimal"); }

    void markIntermediate() { apply("markIntermediate"); }

    public void markLinuxFromScratch() { apply("markLinuxFromScratch"); }

    void markMultiplatform(String n) {
        graph.anotateSingleNode(n);
        graph.anotateSingleNode(n + ":amd64");
        graph.anotateSingleNode(n + ":i386");
    }
}
