import type { TranslationValue } from '../types/translation-value.type';

export const SHELL_TRANSLATIONS = {
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
  FILTER_NODE_PLACEHOLDER: {
    en: 'Filter node...',
    es: 'Filtrar nodo...'
  },
  FILTER_GROUP_PLACEHOLDER: {
    en: 'Filter group...',
    es: 'Filtrar grupo...'
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
  MOVE_VERB: {
    en: 'Move',
    es: 'Mover'
  },
  MENU_NO_GROUPS_AVAILABLE: {
    en: 'No groups available',
    es: 'Sin grupos disponibles'
  },
  CLOSE_RELATION_BOX_ARIA: {
    en: 'Close relation box',
    es: 'Cerrar cuadro de relación'
  }
} as const satisfies Record<string, TranslationValue>;
