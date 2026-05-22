package graphbuilderplugins.javasources.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class SourceClassNode {
    private final String fqcn;
    private final Set<String> outgoingDependencies;

    public SourceClassNode(String fqcn) {
        this(fqcn, Set.of());
    }

    public SourceClassNode(String fqcn, Set<String> outgoingDependencies) {
        this.fqcn = normalizeFqcn(fqcn);

        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        if (outgoingDependencies != null) {
            for (String dependency : outgoingDependencies) {
                normalized.add(normalizeFqcn(dependency));
            }
        }
        this.outgoingDependencies = Collections.unmodifiableSet(normalized);
    }

    public String fqcn() {
        return fqcn;
    }

    public Set<String> outgoingDependencies() {
        return outgoingDependencies;
    }

    public SourceClassNode withDependency(String dependencyFqcn) {
        String normalized = normalizeFqcn(dependencyFqcn);
        if (outgoingDependencies.contains(normalized)) {
            return this;
        }

        LinkedHashSet<String> updated = new LinkedHashSet<>(outgoingDependencies);
        updated.add(normalized);
        return new SourceClassNode(fqcn, updated);
    }

    private static String normalizeFqcn(String fqcn) {
        String normalized = Objects.requireNonNull(fqcn, "fqcn must not be null").trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("fqcn must not be blank");
        }
        // Keep source-style canonical names for nested classes.
        return normalized.replace('$', '.');
    }
}
