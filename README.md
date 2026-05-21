# VSDK Dependency Graph Analyzer

VSDK Dependency Graph Analyzer is a **source-agnostic dependency graph platform**.

Its goal is to build and explore dependency graphs from different domains and ecosystems, using a common graph model and interactive workflow. Debian package analysis is currently the first implemented case study, not the final scope.

## Project vision

This project is designed to answer a generic question:

- "Given a source of entities and relationships, how can we generate, inspect, and evolve a dependency graph consistently?"

The platform is intended to support multiple graph sources over time, such as:

- Operating system package dependencies.
- Java project/module/file dependencies (for example, imports between `.java` files).
- Node.js/npm dependency graphs.
- Maven-based Java dependency graphs.

## Current status

Today, the implemented production case is:

- **Debian/Ubuntu package dependency analysis**.

This case already provides end-to-end graph generation, grouping, visualization, and graph-editing operations through CLI/backend/frontend flows.

## What the platform does (generic capabilities)

- Ingests dependency data from a selected source.
- Builds a directed graph of nodes and relationships.
- Organizes nodes into functional groups.
- Exports graph views for structure-level and group-level inspection.
- Supports interactive graph exploration in a browser UI.
- Enables graph maintenance actions (for example, moving nodes between groups).

## Main user-facing capabilities (available now)

- Build graph from cached data.
- Build graph from a live source scan.
- Inspect structure metrics (nodes and edges).
- Open and navigate group views.
- Query relationships between selected elements.
- Move nodes between groups from the UI.
- Switch UI language (English/Spanish).

## Case studies

### 1. Debian/Ubuntu package dependencies (implemented)

The current case study models installed package dependencies and allows users to inspect and refine grouped views of the system dependency landscape.

### 2. Java source/module dependency analysis (planned)

Target scenario:

- Nodes represent Java source files or modules.
- Edges represent dependency links such as `import` relations.

### 3. Node.js/npm dependency analysis (planned)

Target scenario:

- Nodes represent packages/modules in a Node.js project.
- Edges represent declared or resolved dependency links.

### 4. Maven dependency analysis (planned)

Target scenario:

- Nodes represent Maven artifacts/modules.
- Edges represent dependency relations from project metadata.

## Typical workflow

1. Select a source domain/generator.
2. Build or refresh the dependency graph.
3. Explore the graph visually.
4. Inspect relationships and group boundaries.
5. Apply graph maintenance/refinement actions.

## Repository areas

- `frontend/`: interactive graph workspace.
- `backend/`: analysis services and API.
- `etc/`: source-specific definitions and classification inputs.
- `scripts/`: compile/run convenience scripts.
- `output/`: generated graph artifacts.

## Roadmap structure (ready for extension)

The README is intentionally structured so future updates can add:

- per-source input expectations,
- per-source graph semantics,
- source-specific workflows,
- and examples/comparison tables across domains.
