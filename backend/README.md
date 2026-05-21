# Backend

Spring Boot service that exposes the graph analysis and editing API.

## Responsibilities

- Receive HTTP requests to build/update graph snapshots.
- Delegate graph building to the plugin registry.
- Serve generated SVGs for frontend visualization.
- Execute use cases for edge enrichment, relation querying, and node movement.
- Persist/read operational configuration (for example, secrets and CORS) from `application.properties` and dedicated configuration.

## Architecture

Main layers:

- `application/port/in`: use case contracts (`UpdateGraphModelUseCase`, `MoveNodeUseCase`, `BuildEnrichedEdgesUseCase`).
- `application/service`: use case implementations.
- `domain/model`: high-level graph snapshots and entities.
- `infrastructure/http/controller`: REST controllers (`GraphModelController`, `OutputSvgController`).
- `infrastructure/http/dto`: request/response DTOs and API errors.
- `core/`: dependency analysis engine, grouping, and DOT/SVG export.

## Main Endpoints

- `POST /v1/updateGraph`
- `POST /v1/enrichedEdges`
- `POST /v1/groupRelations`
- `POST /v1/moveNode`
- `GET /output/svg/{filename}`

## Module and Build

- Gradle project included in the monorepo through `includeBuild('../graphBuilderPlugins')`.
- Entry points:
  - `entryPoints.SpringBootBackendApp`
  - `entryPoints.CommandLineApp`

## Commands

```bash
./gradlew build
./gradlew run
```
