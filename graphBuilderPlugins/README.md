# Graph Builder Plugins

Java 17 library that decouples graph data sources from the backend graph engine. Each plugin receives a `GraphBuildTarget`, adds nodes and edges, and, when appropriate, writes a reusable cache.

## Public API

Package `graphbuilderplugins.api`:

- `GraphBuilderPlugin`: main contract. Exposes `id()` and `build(...)` variants with `inputFolders` and `classpathEntries`.
- `GraphBuildTarget`: graph write port (`addNode`, `addBadNode`, `addDependency`, `listNodeNames`, `saveCache`).
- `GraphBuilderPluginId`: supported identifiers.
- `GraphBuilderPluginRegistry`: default registry used by the backend.

The registry includes five builders:

```java
CACHE_LOADER
DEBIAN_PACKAGE_GENERATOR
CPP_SOURCES
JAVA_SOURCES
TYPESCRIPT_SOURCES
```

## Implementations

### CacheLoaderGraphBuilderPlugin

Reads cache files in plain-text format:

```text
n nodeName good
n missingNode bad
r sourceNode targetNode
```

If no `inputFolders` are passed, it attempts to load `cache.txt` and `cache_extra.txt`. If paths are provided, it processes every existing file in order.

### DebianPackageGraphBuilderPlugin

Builds a graph from the local Debian/Ubuntu system:

- Runs `dpkg-query -l` to create installed-package nodes.
- Runs `apt-cache depends <package>` to discover dependencies.
- Splits the dependency scan across 72 threads.
- Adds simplified nodes for architecture-qualified names (`pkg:amd64` -> `pkg`).
- Writes `cache.txt` when finished.

Requires `dpkg-query` and `apt-cache` on `PATH`.

### CppSourcesGraphBuilderPlugin

Builds a graph from C/C++ source folders:

- Requires non-empty `inputFolders`.
- Walks `.h`, `.cpp`, and `.txx` files.
- Uses the path relative to the best matching input root as the node name.
- Creates edges for every `#include "..."` or `#include <...>` directive.
- Adds include targets as nodes even when they do not exist as local files.
- Writes `cache.txt`.

This builder performs lexical include analysis; it does not run a preprocessor or resolve macros.

### JavaSourcesGraphBuilderPlugin

Builds a graph from Java sources:

- Requires non-empty `inputFolders`.
- Converts inputs into source folders.
- Accepts optional `classpathEntries`.
- Uses Java compiler infrastructure (`com.sun.source.tree`) for parsing and semantic analysis.
- Creates nodes per source class using fully qualified class names.
- Creates edges to outgoing dependencies detected by `ClassDependencyScanner`.
- Writes `cache.txt`.

The pipeline is split into file discovery, compilation task creation, AST parsing, semantic analysis, and dependency scanning.

### TypeScriptSourcesGraphBuilderPlugin

Builds a graph from TypeScript source folders:

- Requires non-empty `inputFolders`.
- Uses a Node-based analyzer script with the TypeScript compiler API (AST + module resolver), not regex text parsing.
- Creates nodes by module/file path (MVP), normalized relative to the best matching input root.
- Scans `ImportDeclaration`, `ExportDeclaration` with `moduleSpecifier`, and `ImportEqualsDeclaration`.
- Resolves imports using the TypeScript resolver (`ts.resolveModuleName`) with `tsconfig.json` support when available.
- Excludes by default: `node_modules`, `dist`, `build`, `.git`, and `.d.ts`.
- Supports `.ts`, `.tsx`, `.mts`, `.cts`.
- For unresolved non-local imports, creates external package nodes (for example `react`).
- Writes `cache.txt`.

The current scope is module-level dependency mapping for an MVP. Fine-grained symbol/type dependency analysis with `TypeChecker` is intentionally left for a later phase.

## Internal Structure

- `impl/`: cache, Debian, and C/C++ builders.
- `javasources/`: Java builder and specialized subpackages:
  - `analyzer/`: pipeline orchestration.
  - `discovery/`: Java file discovery and project-dependency resolver port.
  - `javaSourceCompiler/`: compiler session, tasks, AST, and semantic analysis.
  - `scanner/`: dependency extraction from compilation units.
  - `model/`: `DependencyGraph` and `SourceClassNode`.
  - `export/`: dependency graph exporters.
- `internal/ProcessRunner`: helper for launching external processes used by the Debian builder.
- `src/test/java/...`: acceptance and validation tests for the Java analyzer.

## Backend Integration

The backend does not depend on concrete source-specific classes. `core.DebianAnalyzer` selects a `GraphBuilderPluginId`, obtains the plugin from `GraphBuilderPluginRegistry`, and writes through `SoftwarePackageGraphBuildTarget`, an adapter to the backend's legacy graph.

Gradle resolution uses a composite build:

```groovy
includeBuild('../graphBuilderPlugins')
```

## Build and Tests

From `graphBuilderPlugins/`:

```bash
./gradlew npmInstallTypeScriptAnalyzerDeps
./gradlew build
./gradlew test
```

`npmInstallTypeScriptAnalyzerDeps` instala `typescript` para el analizador ubicado en
`src/main/resources/typescript-analyzer/`. El build verifica esta dependencia y falla con
mensaje claro si falta.

From the repository root, `gradle build` compiles the backend and, through the composite dependency, this module.

## Adding a Builder

1. Implement `GraphBuilderPlugin`.
2. Add a value to `GraphBuilderPluginId`.
3. Register the implementation in `GraphBuilderPluginRegistry`.
4. Expose the new generator in the backend (`GraphModelGenerator` and `UpdateGraphModelService`).
5. Update frontend types if the generator must be launched from the UI.
