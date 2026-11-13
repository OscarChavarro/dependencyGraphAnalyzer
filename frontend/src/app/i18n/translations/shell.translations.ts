import type { TranslationValue } from '../types/translation-value.type';

export const SHELL_TRANSLATIONS = {
  LFS_BUTTON: {
    en: 'Compute\nLFS',
    es: 'Calcular\nLFS'
  },
  LFS_BUTTON_TITLE: {
    en: 'Download with apt-get source the selected packages, from sinks to sources',
    es: 'Descargar con apt-get source los paquetes seleccionados, de sumideros a fuentes'
  },
  LFS_DIALOG_TITLE: {
    en: 'Linux From Scratch: package sources',
    es: 'Linux From Scratch: fuentes de paquetes'
  },
  LFS_PLANNING: {
    en: 'Computing download order...',
    es: 'Calculando el orden de descarga...'
  },
  LFS_PACKAGES: {
    en: 'packages',
    es: 'paquetes'
  },
  LFS_DOWNLOADING: {
    en: 'Downloading',
    es: 'Descargando'
  },
  LFS_FINISHED: {
    en: 'Finished.',
    es: 'Terminado.'
  },
  LFS_CANCELED: {
    en: 'Canceled.',
    es: 'Cancelado.'
  },
  LFS_FAILED_PACKAGES: {
    en: 'Failed packages',
    es: 'Paquetes con error'
  },
  LFS_SKIPPED_NODES: {
    en: 'Skipped (not apt packages)',
    es: 'Omitidos (no son paquetes apt)'
  },
  LFS_ALREADY_PRESENT: {
    en: 'Skipped, already downloaded',
    es: 'Omitidos, ya descargados'
  },
  LFS_WRITING_GUIDE: {
    en: 'Writing AGENTS.md build guide...',
    es: 'Escribiendo la guía de build AGENTS.md...'
  },
  LFS_GUIDE_WRITTEN: {
    en: 'Build guide',
    es: 'Guía de build'
  },
  LFS_GUIDE_FAILED: {
    en: 'Could not write AGENTS.md',
    es: 'No se pudo escribir AGENTS.md'
  },
  LFS_CANCEL: {
    en: 'Cancel',
    es: 'Cancelar'
  },
  LFS_CLOSE: {
    en: 'Close',
    es: 'Cerrar'
  },
  PANEL_COLLAPSE: {
    en: 'Collapse',
    es: 'Colapsar'
  },
  PANEL_LABEL: {
    en: 'Panel',
    es: 'Panel'
  },
  GRAPH_GENERATOR_TITLE: {
    en: 'Graph Generator',
    es: 'Generador de Grafos'
  },
  CREATE_GRAPH_FROM_CACHE: {
    en: 'Create graph from cache.txt',
    es: 'Crear grafo desde cache.txt'
  },
  ANALYZE_DEBIAN_SYSTEM: {
    en: 'Analyze Debian system',
    es: 'Analizar sistema Debian'
  },
  CACHE_PROJECT_SELECTOR_LABEL: {
    en: 'Cached project',
    es: 'Proyecto cacheado'
  },
  CPP_PROJECT_SELECTOR_LABEL: {
    en: 'C/C++ project',
    es: 'Proyecto C/C++'
  },
  ANALYZE_CPP_SOURCES: {
    en: 'Analyze C/C++ sources',
    es: 'Analizar fuentes C/C++'
  },
  JAVA_PROJECT_SELECTOR_LABEL: {
    en: 'Java project',
    es: 'Proyecto Java'
  },
  ANALYZE_JAVA_SOURCES: {
    en: 'Analyze Java sources',
    es: 'Analizar fuentes Java'
  },
  TYPESCRIPT_PROJECT_SELECTOR_LABEL: {
    en: 'TypeScript project',
    es: 'Proyecto TypeScript'
  },
  ANALYZE_TYPESCRIPT_SOURCES: {
    en: 'Analyze TypeScript sources',
    es: 'Analizar fuentes TypeScript'
  },
  CPP_PROJECT_REQUIRED: {
    en: 'You must select a C/C++ project.',
    es: 'Debes seleccionar un proyecto C/C++.'
  },
  JAVA_PROJECT_REQUIRED: {
    en: 'You must select a Java project.',
    es: 'Debes seleccionar un proyecto Java.'
  },
  TYPESCRIPT_PROJECT_REQUIRED: {
    en: 'You must select a TypeScript project.',
    es: 'Debes seleccionar un proyecto TypeScript.'
  },
  BACK_TO_STRUCTURE: {
    en: 'Back to structure',
    es: 'Volver a structure'
  },
  PROCESSING: {
    en: 'Processing...',
    es: 'Procesando...'
  },
  GRAPH_UPDATE_ERROR: {
    en: 'Could not update graph',
    es: 'No se pudo actualizar el grafo'
  },
  CANVAS_INIT_ERROR: {
    en: 'Could not initialize canvas',
    es: 'No se pudo inicializar el canvas'
  },
  LOAD_SVG_ERROR_PREFIX: {
    en: 'Could not load',
    es: 'No se pudo cargar'
  },
  NODES: {
    en: 'Nodes',
    es: 'Nodos'
  },
  EDGES: {
    en: 'Edges',
    es: 'Aristas'
  },
  WORKSPACE_AREA_ARIA: {
    en: 'Workspace area',
    es: 'Area de trabajo'
  },
  LANGUAGE_SELECTOR_ARIA: {
    en: 'Language selector',
    es: 'Selector de idioma'
  },
  MENU_SEARCH_PLACEHOLDER: {
    en: 'Search...',
    es: 'Buscar...'
  },
  MENU_ADD_NEW_GROUP_PLACEHOLDER: {
    en: 'Add to new group...',
    es: 'Agregar a nuevo grupo...'
  },
  FILTER_NODE_PLACEHOLDER: {
    en: 'Filter node...',
    es: 'Filtrar nodo...'
  },
  FILTER_GROUP_PLACEHOLDER: {
    en: 'Filter group...',
    es: 'Filtrar grupo...'
  },
  SEARCH_PACKAGE_PLACEHOLDER: {
    en: 'Search package...',
    es: 'Buscar paquete...'
  },
  SEARCH_PACKAGE_ARIA: {
    en: 'Search package in groups',
    es: 'Buscar paquete en los grupos'
  },
  SEARCH_PACKAGE_SEARCHING: {
    en: 'Searching...',
    es: 'Buscando...'
  },
  SEARCH_PACKAGE_REQUIRES_GRAPH: {
    en: 'Load a graph before searching packages',
    es: 'Carga un grafo antes de buscar paquetes'
  },
  SEARCH_PACKAGE_NOT_FOUND: {
    en: 'Package not found in any group',
    es: 'Paquete no encontrado en ningún grupo'
  },
  SEARCH_PACKAGE_GROUP_WITHOUT_GRAPH: {
    en: 'The group has no graph generated',
    es: 'El grupo no tiene grafo generado'
  },
  SEARCH_PACKAGE_NOT_IN_GRAPH: {
    en: 'The package is listed in the group but is not in the current graph',
    es: 'El paquete está listado en el grupo pero no está en el grafo actual'
  },
  SEARCH_PACKAGE_ERROR: {
    en: 'Error searching package',
    es: 'Error buscando el paquete'
  },
  MENU_OPEN_NEW_SVG: {
    en: 'Open group',
    es: 'Abrir grupo'
  },
  MENU_SHOW_RELATION: {
    en: 'Show relationships',
    es: 'Mostrar relaciones'
  },
  MENU_FLOOD_DEPENDENCIES: {
    en: 'Flood dependencies',
    es: 'Inundar dependencias'
  },
  MENU_FLOOD_CLIENTS: {
    en: 'Flood clients',
    es: 'Inundar clientes'
  },
  MENU_MOVE_TO: {
    en: 'Move to...',
    es: 'Mover a...'
  },
  MENU_SET_BACKGROUND_COLOR: {
    en: 'Set background color...',
    es: 'Fijar color de fondo...'
  },
  MENU_RENAME_GROUP: {
    en: 'Rename...',
    es: 'Renombrar...'
  },
  MOVE_VERB: {
    en: 'Move',
    es: 'Mover'
  },
  RENAME_GROUP_NEW_NAME_PLACEHOLDER: {
    en: 'New name',
    es: 'Nuevo nombre'
  },
  RENAME_GROUP_BACKEND_NOT_READY: {
    en: 'Could not rename group.',
    es: 'No se pudo renombrar el grupo.'
  },
  MENU_NO_GROUPS_AVAILABLE: {
    en: 'No groups available',
    es: 'Sin grupos disponibles'
  },
  CLOSE_RELATION_BOX_ARIA: {
    en: 'Close relation box',
    es: 'Cerrar cuadro de relación'
  },
  CLOSE_COLOR_DIALOG_ARIA: {
    en: 'Close color dialog',
    es: 'Cerrar dialogo de color'
  },
  COLOR_DIALOG_ACCEPT: {
    en: 'Accept',
    es: 'Aceptar'
  },
  COLOR_DIALOG_CANCEL: {
    en: 'Cancel',
    es: 'Cancelar'
  }
} as const satisfies Record<string, TranslationValue>;
