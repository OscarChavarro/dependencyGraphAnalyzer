# VSDK Dependency Graph Analyzer

Monorepo for generating, grouping, and exploring dependency graphs. The project started as a Debian/Ubuntu package analyzer and now also builds graphs from cache files, C/C++ sources, Java sources, and TypeScript sources.

The main outputs are Graphviz artifacts (`.dot`, `.svg`, `.png`) plus a JSON graph model consumed by an Angular UI. The application lets users navigate from a structural overview into group-specific views, select nodes, inspect relations between groups, highlight transitive dependencies or clients, and move nodes between group definition files.

## Core Idea

The central idea behind this system is to make a huge and complex graph, often with several thousand nodes, understandable by organizing it into groups. A group is a subgraph of the main graph with a locality principle: its nodes tend to be close to each other through direct relations and to be conceptually related.

This grouping model is the reason the tool exists. Instead of forcing the reader to study a large knowledge structure as a flat mass of nodes and edges, the analyzer lets them approach it as a set of coherent areas, inspect each area independently, and then study the relations between those areas.

Typical use cases include:

1. Analyze dependency relations between classes in an object-oriented software project, making refactors easier and helping reduce software complexity.
2. Analyze dependency relations and effective package usage in an operating system or Docker image, helping remove unnecessary nodes. This supports the security-hardening principle of installing only the minimum required software, which reduces the vulnerability surface while using storage more efficiently.
3. Analyze complex dependency schemes in package-management systems such as Gradle, Maven, or npm. By grouping dependencies according to concepts or major software areas, programmers can understand what dependencies do instead of only seeing that there are many of them.

## Subprojects

- `backend/`: Java 17 Spring Boot service. It exposes the HTTP API, coordinates graph generation, applies grouping/reduction, and serves generated SVGs.
- `frontend/`: Angular 20 application. It loads the backend model, renders SVGs on an HTML5 canvas, and provides the interactive graph exploration tools.
- `graphBuilderPlugins/`: Java 17 library containing the graph builder API and implementations for cache files, Debian packages, C/C++ sources, and Java sources.

The root `settings.gradle` uses a Gradle composite build for `backend` and `graphBuilderPlugins`. The frontend is not part of the Gradle build; it is built with npm and Angular CLI.

## Workflow

1. The frontend requests a graph with a `generator`, a group-definition folder, and, depending on the builder, input paths or classpath entries.
2. The backend resolves the group `.txt` files and delegates graph construction to `graphBuilderPlugins`.
3. The core engine groups nodes, captures unclassified nodes in `999_orphans`, applies markers, reduces transitive relations, and exports artifacts.
4. SVGs are written to `output/svg/`, and the node/edge model is returned as JSON.
5. The UI loads `structure.svg` and then group SVGs (`<group>.svg`) through `GET /output/svg/{filename}`.

## Relevant Structure

- `etc/`: group definitions and project catalogs.
  - `etc/cachedProjects/projects.json`: cache projects exposed to the UI.
  - `etc/cppProjects/projects.json`: C/C++ projects exposed to the UI.
  - `etc/javaProjects/projects.json`: Java projects, with either fixed classpath entries or Gradle-based classpath resolution.
  - `etc/typescriptProjects/projects.json`: TypeScript projects exposed to the UI.
  - `etc/highlightRules/`: highlighting rules used by the core engine.
- `u` and `v`: symlinks to Ubuntu definition folders used by local defaults.
- `cache.txt` and `cache_extra.txt*`: historical caches in the simple node/edge format.
- `output/`: generated artifacts (`dot`, `svg`, `png`, `txt`, auxiliary scripts, and `cleanRelationsGraph.txt`).
- `scripts/`: wrappers for compiling, running the backend, and cleaning local artifacts.

## Requirements

- Java 17.
- Gradle on `PATH` or the wrappers included in subprojects.
- Graphviz with `/usr/bin/dot`; the backend invokes this path directly for `.dia`, `.svg`, and `.png` generation.
- Node.js/npm for the Angular frontend.
- On Debian/Ubuntu Linux, `dpkg-query` and `apt-cache` are required for `DEBIAN_PACKAGE_GENERATOR`.

## Quick Start

Build backend and plugins from the repository root:

```bash
gradle build
```

Start the Spring Boot API:

```bash
./scripts/runBackend.sh
```

Start the frontend in another terminal:

```bash
cd frontend
npm install
npm start
```

The frontend reads `frontend/public/environment.json` at startup. If `backend.url` exists, it is used as the backend base URL; otherwise the UI falls back to `http://localhost:8080`.

## Main API

- `POST /v1/updateGraph` and `POST /v1/updateGraphModel`: generate or regenerate the graph model.
- `POST /v1/updateGraphModel/javaSources`: shortcut for Java-source graph generation.
- `POST /v1/updateGraphModel/typescriptSources`: shortcut for TypeScript-source graph generation.
- `POST /v1/enrichedEdges`: compute enriched package-level edges from cache and group definitions.
- `POST /v1/groupRelations`: return stored relations between two groups.
- `POST /v1/moveNode`: move nodes between group files.
- `GET /v1/cachedProjects`, `/v1/cppProjects`, `/v1/javaProjects`, `/v1/typescriptProjects`: project catalogs for the UI.
- `GET /output/svg/{filename}`: serve generated SVGs from `output/svg`.

## Generators

- `CACHE_LOADER`: reads `cache.txt` and `cache_extra.txt` by default, or the files provided in `inputFolders`.
- `DEBIAN_PACKAGE_GENERATOR`: reads installed packages with `dpkg-query -l` and dependencies with `apt-cache depends`.
- `CPP_SOURCES`: walks input folders, creates nodes for `.h` and `.cpp` files, and creates edges from `#include` directives.
- `JAVA_SOURCES`: analyzes `.java` sources with the Java compiler API, resolving semantic dependencies with an optional classpath.
- `TYPESCRIPT_SOURCES`: analyzes `.ts/.tsx/.mts/.cts` modules with the TypeScript compiler API, builds an MVP dependency graph by module/file from resolved imports/exports, and leaves symbol-level semantic analysis for a later phase.

## Data Formats

Group files are `.txt` files. The first significant line is the group header, usually `[name]`; following lines are member nodes. Empty lines and `#` comments are ignored.

The cache format is plain text:

```text
n package good
n missing-package bad
r source target
```

Builders that reconstruct graphs from source code or Debian packages write `cache.txt`, which allows later runs to use `CACHE_LOADER`.

## Project Documentation

- [Backend](./backend/README.md)
- [Frontend](./frontend/README.md)
- [Graph Builder Plugins](./graphBuilderPlugins/README.md)
