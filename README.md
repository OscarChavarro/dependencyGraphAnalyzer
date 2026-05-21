# VSDK Dependency Graph Analyzer

Tool to analyze, generate, and explore dependency graphs with a Spring Boot backend, Angular frontend, and graph builder plugins.

## Monorepo Structure

- `frontend/`: web interface to explore SVGs, select nodes, and run interactive actions.
- `backend/`: HTTP API and orchestration for graph analysis/generation/mutation.
- `graphBuilderPlugins/`: plugin API and graph builder providers (cache, Debian, C++).
- `etc/`: group definitions, highlighting rules, and environment-specific auxiliary data.
- `output/`: generated artifacts (`.svg`, `.dot`, output cache).

## Documentation by Project

- [Frontend README](./frontend/README.md)
- [Backend README](./backend/README.md)
- [Plugins README](./graphBuilderPlugins/README.md)

## General Flow

1. The backend receives a request to generate or update a graph.
2. The backend delegates to a `graphBuilderPlugins` plugin based on the requested builder.
3. Model snapshots and visual artifacts are produced in `output/`.
4. The frontend loads those artifacts and enables navigation, selection, relations, flooding, and node movement across groups.
