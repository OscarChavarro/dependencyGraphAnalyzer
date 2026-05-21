# Frontend

Angular application for interactive dependency graph exploration.

## Responsibilities

- Render SVG graphs on canvas (`Html5CanvasGraphRenderer`).
- Navigate between `structure.svg` and group-specific views.
- Select nodes (single and multiple) from canvas and side list.
- Open a contextual action menu on the current selection.
- Execute UI actions: show relations, flood dependencies/clients, move group nodes.
- Display relation results in a dialog with contextual actions.
- Support i18n (es/en) with typed keys.

## Backend Integration

Endpoints consumed from `App`:

- `POST /v1/updateGraph`
- `POST /v1/enrichedEdges`
- `POST /v1/groupRelations`
- `POST /v1/moveNode`
- `GET /output/svg/{filename}`

Base URL configurable through the `BACKEND_BASE_URL` token and `public/environment.json`.

## Relevant Structure

- `src/app/app.ts`: state orchestration, HTTP calls, and interaction coordination.
- `src/app/gui/`: interaction techniques (selection, navigation, keyboard, contextual menu).
- `src/app/render/`: canvas renderer and node picking.
- `src/app/i18n/`: language state, translation service, and keys.
- `src/app/localProcessing/`: local logic for relations/flooding where applicable.

## Commands

```bash
npm install
npm run build
npm start
```
