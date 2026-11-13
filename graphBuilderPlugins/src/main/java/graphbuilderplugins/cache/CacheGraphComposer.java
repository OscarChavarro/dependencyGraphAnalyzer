package graphbuilderplugins.cache;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Composes a base cache file with optional extra cache files. The base file is loaded
 * as is; each extra file only contributes nodes whose dependencies already exist.
 */
public final class CacheGraphComposer {
    private CacheGraphComposer() {
    }

    public static CacheFileContentModel compose(Path baseFile, List<Path> extraFiles) {
        CacheFileContentModel base = CacheFileReader.read(baseFile);
        List<CacheNodeEntryModel> nodes = new ArrayList<>(base.nodes());
        List<CacheRelationEntryModel> relations = new ArrayList<>(base.relations());
        Set<String> existingNodeNames = new LinkedHashSet<>(base.resultingNodeNames());

        for (Path extraFile : extraFiles) {
            CacheFileContentModel extra = CacheFileReader.read(extraFile);
            ExtraCacheSelectionResultModel selection = ExtraCacheSelector.select(extra, existingNodeNames);
            logSelection(extraFile, selection);
            nodes.addAll(selection.accepted().nodes());
            relations.addAll(selection.accepted().relations());
            existingNodeNames.addAll(selection.accepted().resultingNodeNames());
        }
        return new CacheFileContentModel(List.copyOf(nodes), List.copyOf(relations));
    }

    private static void logSelection(Path extraFile, ExtraCacheSelectionResultModel selection) {
        System.out.println("Extra cache " + extraFile + ": accepted "
                + selection.accepted().nodes().size() + " nodes, rejected "
                + selection.rejectedMissingDeps().size() + " nodes");
        selection.rejectedMissingDeps().forEach((node, missing) ->
                System.out.println("  - rejected " + node + ", missing dependencies: " + missing));
        selection.undeclaredSources().forEach(source ->
                System.out.println("  - ignored relations of undeclared node " + source));
    }
}
