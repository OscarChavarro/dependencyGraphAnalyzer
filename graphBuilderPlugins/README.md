# Graph Builder Plugins

Java module with the plugin API and graph builder implementations used by the backend.

## Goal

Decouple the data source from the graph building process to support multiple builders under the same contract.

## API

Package `graphbuilderplugins.api`:

- `GraphBuilderPlugin`: main plugin contract.
- `GraphBuildTarget`: build context/target.
- `GraphBuilderPluginId`: typed plugin identifier.
- `GraphBuilderPluginRegistry`: plugin registration and resolution.

## Included Implementations

Package `graphbuilderplugins.impl`:

- `CacheLoaderGraphBuilderPlugin`: builds from existing cache.
- `DebianPackageGraphBuilderPlugin`: builds from Debian/Ubuntu package analysis.
- `CppSourcesGraphBuilderPlugin`: builds from C++ source analysis.

Internal support:

- `graphbuilderplugins.internal.ProcessRunner`: execution of auxiliary processes required by plugins.

## Monorepo Integration

- Module published/consumed by `backend` through a Gradle composite build (`includeBuild`).
- Allows adding new builders without coupling HTTP controllers to source-specific details.

## Commands

```bash
./gradlew build
```
