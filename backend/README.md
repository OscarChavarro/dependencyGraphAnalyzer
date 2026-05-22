# Backend

Java 17/Spring Boot service exposing the graph generation, query, and editing API. It also keeps a command-line entry point for running the analyzer without HTTP.

## Responsibilities

- Validate HTTP requests and normalize input early.
- Resolve group folders into ordered lists of `.txt` group files.
- Delegate graph construction to the plugin registry (`graphBuilderPlugins`).
- Group nodes, capture orphan nodes in `999_orphans`, apply markers, reduce transitive relations, and export Graphviz artifacts.
- Serve generated SVGs from `output/svg`.
- Query relations between groups and move nodes between group definition files.
- Read project catalogs from `etc/*Projects/projects.json`.
- Configure CORS from `secrets.json` or `BACKEND_SECRETS_FILE`.

## Architecture

- `entryPoints/`: `SpringBootBackendApp` and `CommandLineApp`.
- `backend/application/port/in`: use-case contracts.
- `backend/application/service`: graph generation, enriched-edge, and node-move implementations.
- `backend/domain/model`: internal graph snapshot DTOs returned to the frontend.
- `backend/infrastructure/http/controller`: REST controllers.
- `backend/infrastructure/http/dto`: HTTP payloads and responses.
- `backend/infrastructure/http/config`: CORS, error handling, and secrets.
- `core/`: legacy analyzer engine: graph model, grouping, highlighting rules, and DOT/SVG/PNG export.
- `core/graphPlugins`: adapter between `SoftwarePackageGraph` and the plugin API.

## Endpoints

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/v1/updateGraph` | Generate a graph snapshot. Alias: `/v1/updateGraphModel`. |
| `POST` | `/v1/updateGraphModel/javaSources` | Generate a snapshot with `JAVA_SOURCES`. |
| `POST` | `/v1/enrichedEdges` | Compute group.package edges from cache and group definitions. |
| `POST` | `/v1/groupRelations` | Search relations between two groups in `output/cleanRelationsGraph.txt`. |
| `POST` | `/v1/moveNode` | Move one or more nodes between existing group files. |
| `GET` | `/v1/cachedProjects` | Read `etc/cachedProjects/projects.json`. |
| `GET` | `/v1/cppProjects` | Read `etc/cppProjects/projects.json`. |
| `GET` | `/v1/javaProjects` | Read `etc/javaProjects/projects.json` and optionally resolve classpath with Gradle. |
| `GET` | `/output/svg/{filename}` | Serve a generated `.svg`; rejects paths and non-SVG filenames. |

## Main Payloads

`POST /v1/updateGraph`:

```json
{
  "generator": "CACHE_LOADER",
  "groupsDefinitionFolder": "etc/_ubuntu_24.04",
  "inputFolders": ["etc/caches/cache.txt.ubuntu2404minimal"],
  "classpath": []
}
```

`generator` accepts `CACHE_LOADER`, `DEBIAN_PACKAGE_GENERATOR`, `CPP_SOURCES`, and `JAVA_SOURCES`. `inputFolders` is required for `CPP_SOURCES` and `JAVA_SOURCES`. For Java, `classpath` is optional but needed when the compiler cannot resolve external types from the source folders alone.

`POST /v1/moveNode`:

```json
{
  "groupFolder": "etc/_ubuntu_24.04",
  "originGroup": "99_incoming",
  "originNodes": ["example-package"],
  "destinationGroup": "203_prohibited"
}
```

`destinationGroup` must exist as `<destinationGroup>.txt`. The service removes nodes from the origin file and appends them to the destination file.

## Generated Artifacts

Every `UpdateGraphModelService` run clears `output/cleanRelationsGraph.txt`, builds the graph, and exports:

- `output/general.dot` and `output/general.dia`.
- `output/dot/<group>.dot`.
- `output/svg/structure.svg` and `output/svg/<group>.svg`.
- `output/png/<group>.png`.
- `output/txt/`, `output/cleansh/`, `output/installsh/`, and auxiliary lists.

Graphviz is invoked as `/usr/bin/dot`; image generation fails if that binary is missing.

## Configuration

`src/main/resources/application.properties` enables HTTP compression and long request timeouts for heavy analyses.

CORS is configured in `secrets.json` or in the file pointed to by `BACKEND_SECRETS_FILE`:

```json
{
  "cors": {
    "allowedOriginPatterns": ["http://localhost:*"]
  }
}
```

If the file is missing or invalid, the backend falls back to localhost-only origins.

## Build and Run

From `backend/`:

```bash
./gradlew build
./gradlew run -Papp=entryPoints.SpringBootBackendApp
```

From the repository root:

```bash
./scripts/runBackend.sh
```

The Gradle `run` task uses the repository root as working directory, so paths such as `etc/...`, `cache.txt`, and `output/...` resolve from there.

For CLI mode:

```bash
./scripts/run.sh etc/_ubuntu_24.04/*.txt
```

`CommandLineApp` uses `cache.txt` when it exists; otherwise it analyzes the local Debian/Ubuntu system.

## Dependencies

- Java 17.
- Spring Boot Web and Validation.
- Jackson.
- JGraphT.
- Graphviz (`/usr/bin/dot`).
- `dpkg-query` and `apt-cache` for the Debian generator.
- `graphBuilderPlugins` through the Gradle composite build (`includeBuild('../graphBuilderPlugins')`).
