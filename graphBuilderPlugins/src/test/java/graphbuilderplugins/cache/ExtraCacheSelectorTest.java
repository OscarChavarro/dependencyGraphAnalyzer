package graphbuilderplugins.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExtraCacheSelectorTest {
    private static final Set<String> EXISTING = Set.of("curl", "bash", "gcc");

    private static List<String> acceptedNames(ExtraCacheSelectionResultModel result) {
        return result.accepted().nodes().stream().map(CacheNodeEntryModel::name).toList();
    }

    @Test
    void acceptsNodeWhenAllDependenciesExist() {
        CacheFileContentModel extra = CacheFileReader.parse(List.of(
                "n *sdkman good",
                "r *sdkman curl",
                "r *sdkman bash"));

        ExtraCacheSelectionResultModel result = ExtraCacheSelector.select(extra, EXISTING);

        assertEquals(List.of("*sdkman"), acceptedNames(result));
        assertEquals(2, result.accepted().relations().size());
        assertTrue(result.rejectedMissingDeps().isEmpty());
    }

    @Test
    void rejectsNodeWithMissingDependencyAndDropsItsRelations() {
        CacheFileContentModel extra = CacheFileReader.parse(List.of(
                "n *cde good",
                "r *cde gcc",
                "r *cde ksh"));

        ExtraCacheSelectionResultModel result = ExtraCacheSelector.select(extra, EXISTING);

        assertTrue(result.accepted().nodes().isEmpty());
        assertTrue(result.accepted().relations().isEmpty());
        assertEquals(Set.of("ksh"), result.rejectedMissingDeps().get("*cde"));
    }

    @Test
    void acceptsChainOfExtrasAndPropagatesRejection() {
        CacheFileContentModel extra = CacheFileReader.parse(List.of(
                "n *darknet good",
                "r *darknet *opencv",
                "n *opencv good",
                "r *opencv gcc",
                "n *broken good",
                "r *broken missing-lib",
                "n *dependsOnBroken good",
                "r *dependsOnBroken *broken"));

        ExtraCacheSelectionResultModel result = ExtraCacheSelector.select(extra, EXISTING);

        assertEquals(List.of("*darknet", "*opencv"), acceptedNames(result));
        assertEquals(Set.of("*broken", "*dependsOnBroken"), result.rejectedMissingDeps().keySet());
    }

    @Test
    void acceptsNodeWithoutStateAsGoodAndIgnoresUndeclaredSources() {
        CacheFileContentModel extra = CacheFileReader.parse(List.of(
                "n *apitrace",
                "r *apitrace bash",
                "r *ghost curl"));

        ExtraCacheSelectionResultModel result = ExtraCacheSelector.select(extra, EXISTING);

        assertEquals(List.of(new CacheNodeEntryModel("*apitrace", true)), result.accepted().nodes());
        assertEquals(List.of("*ghost"), result.undeclaredSources());
    }
}
