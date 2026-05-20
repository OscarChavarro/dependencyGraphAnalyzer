package co.cubestudio;

import co.cubestudio.highlight.HighlightRulesEngine;
import java.nio.file.Paths;

public class MarkerJedilink {
    private static final String DEFAULT_RULES_FILE = "etc/highlightRules/jedilink-rules.json";
    private final SoftwarePackageGraph graph;

    public MarkerJedilink(SoftwarePackageGraph graph) {
        this.graph = graph;
    }

    void markCustomJedilink() {
        HighlightRulesEngine engine = HighlightRulesEngine.fromJsonFile(
                graph,
                Paths.get(DEFAULT_RULES_FILE));
        engine.applyProfile("markCustomJedilink");
    }
}
