import { AfterViewInit, Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { BACKEND_BASE_URL } from './config-tokens';
import { Html5CanvasGraphRenderer } from './render/Html5CanvasGraphRenderer';
import {
  GraphModel,
  GraphModelGenerator,
  GraphModelSnapshot,
  EnrichedEdgesResponse,
  UpdateGraphModelRequest,
  UpdateGraphModelResponse
} from './model/graph-model';
import { KeyboardInteractionTechniques } from './gui/KeyboardInteractionTechniques';
import { DrawingAreaNavigationInteractionTechnique } from './gui/DrawingAreaNavigationInteractionTechnique';
import { SelectionInteractionTechnique } from './gui/SelectionInteractionTechnique';
import { SelectedNodeActionMenuInteractionTechnique } from './gui/SelectedNodeActionMenuInteractionTechnique';
import { MenuRenderer } from './gui/menu-renderer';
import { I18nStateService } from './i18n/services/i18n-state.service';
import { I18nService } from './i18n/services/i18n.service';
import { I18N_KEYS } from './i18n/translations/i18n-keys.const';
import type { SupportedLanguage } from './i18n/types/supported-language.type';
import type { TranslationKey } from './i18n/translations/translations-by-namespace.const';
import { GroupRelationshipQuery } from './localProcessing/GroupRelationshipQuery';
import { RelationDialogComponent } from './relation-dialog.component';

@Component({
  selector: 'app-root',
  imports: [CommonModule, MenuRenderer, RelationDialogComponent],
  templateUrl: './app.html',
  styleUrl: './app.sass'
})
export class App implements OnInit, AfterViewInit, OnDestroy {
  private static readonly GRAPH_MODEL_SESSION_KEY = 'vsdk.graphModelSnapshot';
  @ViewChild('workspaceArea')
  private workspaceAreaRef?: ElementRef<HTMLElement>;

  @ViewChild('workspaceCanvas')
  private workspaceCanvasRef?: ElementRef<HTMLCanvasElement>;

  @ViewChild(MenuRenderer)
  private menuRenderer?: MenuRenderer;

  public graphModel: GraphModelSnapshot | null = null;
  public isLoading = false;
  public errorMessage = '';
  public isPanelCollapsed = false;
  public relationBoxVisible = false;
  public relationBoxText = '';
  public readonly selectedLanguage;
  public readonly i18nKeys = I18N_KEYS;

  private readonly endpointUrl: string;
  private readonly backendBaseUrl: string;
  private readonly groupsDefinitionFolder = '../u/';
  private readonly graphRenderer = new Html5CanvasGraphRenderer();
  private readonly groupRelationshipQuery = new GroupRelationshipQuery();
  private readonly graphSelectionModel = new GraphModel();
  private currentSvgFilename = 'structure.svg';
  private lastGraphGenerator: GraphModelGenerator = 'CACHE_LOADER';
  private cachedStructureGroupNames: string[] = [];
  private readonly keyboardInteractionTechniques = new KeyboardInteractionTechniques(this.graphSelectionModel);
  private readonly drawingAreaNavigationInteractionTechnique = new DrawingAreaNavigationInteractionTechnique(this.graphRenderer);
  private readonly selectionInteractionTechnique = new SelectionInteractionTechnique(this.graphSelectionModel, this.graphRenderer);
  private selectedNodeActionMenuInteractionTechnique?: SelectedNodeActionMenuInteractionTechnique;
  private readonly onWindowResize = (): void => {
    this.syncCanvasResolutionWithDisplay();
  };

  constructor(
    private readonly httpClient: HttpClient,
    private readonly i18nStateService: I18nStateService,
    private readonly i18nService: I18nService,
    @Inject(BACKEND_BASE_URL) backendBaseUrl: string
  ) {
    this.backendBaseUrl = backendBaseUrl;
    this.endpointUrl = `${backendBaseUrl}/v1/updateGraph`;
    this.selectedLanguage = this.i18nStateService.selectedLanguage;
  }

  public ngOnInit(): void {
    this.restoreGraphModelFromSession();
  }

  public ngAfterViewInit(): void {
    const canvas = this.workspaceCanvasRef?.nativeElement;
    if (!canvas || !this.graphRenderer.attach(canvas)) {
      this.errorMessage = this.t(this.i18nKeys.shell.CANVAS_INIT_ERROR);
      return;
    }
    this.syncCanvasResolutionWithDisplay();
    window.addEventListener('resize', this.onWindowResize);

    this.keyboardInteractionTechniques.attach(document.body);
    this.drawingAreaNavigationInteractionTechnique.attach(canvas, this.workspaceAreaRef?.nativeElement);
    this.selectionInteractionTechnique.attach(canvas);
    if (this.menuRenderer) {
      this.selectedNodeActionMenuInteractionTechnique = new SelectedNodeActionMenuInteractionTechnique(
        this.graphSelectionModel,
        this.menuRenderer,
        (event) => this.graphRenderer.pickNodeNameFromEvent(event),
        (selectedNodes) => this.graphRenderer.setSelectedNodes(selectedNodes),
        () => this.currentSvgFilename === 'structure.svg',
        (filename) => this.loadAndRenderGraphSvg(filename),
        (selectedNodes) => this.onInundateDependencies(selectedNodes),
        (selectedNodes) => this.onInundateClients(selectedNodes),
        (selectedNodes) => this.onShowRelation(selectedNodes),
        (selectedNodes, targetGroup) => this.onMoveTo(selectedNodes, targetGroup),
        () => this.listStructureGroupNames(),
        (id) => this.t(id)
      );
      this.selectedNodeActionMenuInteractionTechnique.attach();
    }
    if (this.graphModel) {
      queueMicrotask(() => this.loadAndRenderGraphSvg('structure.svg', 0));
    }
  }

  public ngOnDestroy(): void {
    window.removeEventListener('resize', this.onWindowResize);
  }

  public closeRelationBox(): void {
    this.relationBoxVisible = false;
    this.relationBoxText = '';
  }

  public createGraphFromCache(): void {
    this.updateGraphModel('CACHE_LOADER');
  }

  public analyzeDebianSystem(): void {
    this.updateGraphModel('DEBIAN_PACKAGE_GENERATOR');
  }

  public toggleSidePanel(): void {
    this.isPanelCollapsed = !this.isPanelCollapsed;
  }

  public setLanguage(language: SupportedLanguage): void {
    this.i18nStateService.setLanguage(language);
  }

  public t(id: TranslationKey): string {
    return this.i18nService.get(id, this.selectedLanguage());
  }

  private updateGraphModel(generator: GraphModelGenerator): void {
    if (this.isLoading) {
      return;
    }
    this.lastGraphGenerator = generator;
    this.isLoading = true;
    this.errorMessage = '';

    const payload: UpdateGraphModelRequest = {
      generator,
      groupsDefinitionFolder: this.groupsDefinitionFolder
    };

    this.httpClient.post<UpdateGraphModelResponse>(this.endpointUrl, payload).subscribe({
      next: (response) => {
        try {
          this.setGraphModelSnapshot(response.graphModel);
          this.graphSelectionModel.clearSelection();
          this.graphRenderer.setSelectedNodes(this.graphSelectionModel.selectedNodes);
          this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
          queueMicrotask(() => this.loadAndRenderGraphSvg('structure.svg', 0));
        } catch {
          this.errorMessage = `${this.t(this.i18nKeys.shell.GRAPH_UPDATE_ERROR)} (post-process)`;
        } finally {
          this.isLoading = false;
        }
      },
      error: () => {
        this.errorMessage = this.t(this.i18nKeys.shell.GRAPH_UPDATE_ERROR);
        this.isLoading = false;
      }
    });
  }

  private loadAndRenderGraphSvg(filename: string, attempt = 0): void {
    this.currentSvgFilename = filename;
    const svgUrl = `/output/svg/${filename}?ts=${Date.now()}`;
    this.httpClient
      .get(svgUrl, { responseType: 'text' })
      .subscribe({
        next: (svgText) => {
          if (!this.isValidSvgPayload(svgText)) {
            if (attempt < 12) {
              setTimeout(() => this.loadAndRenderGraphSvg(filename, attempt + 1), 250);
              return;
            }
            this.errorMessage = `${this.t(this.i18nKeys.shell.LOAD_SVG_ERROR_PREFIX)} output/svg/${filename}`;
            return;
          }
          this.errorMessage = '';
          this.graphRenderer.loadFromSvgText(svgText);
          this.graphRenderer.setSelectedNodes(this.graphSelectionModel.selectedNodes);
          if (filename === 'structure.svg') {
            this.cachedStructureGroupNames = this.graphRenderer
              .getInteractiveEllipses()
              .map((ellipse) => ellipse.nodeName)
              .filter((name) => name.startsWith('_[') && name !== '_[STRUCTURE]')
              .map((name) => name.replace(/^_\[(.+)\]$/, '$1').toLowerCase());
          }
        },
        error: () => {
          if (attempt < 12) {
            setTimeout(() => this.loadAndRenderGraphSvg(filename, attempt + 1), 250);
            return;
          }
          this.errorMessage = `${this.t(this.i18nKeys.shell.LOAD_SVG_ERROR_PREFIX)} output/svg/${filename}`;
        }
      });
  }

  private onInundateDependencies(selectedNodes: string[]): void {
    console.log('SelectedNodeAction: inundar dependencias', selectedNodes);
  }

  private onInundateClients(selectedNodes: string[]): void {
    console.log('SelectedNodeAction: inundar clientes', selectedNodes);
  }

  private onMoveTo(selectedNodes: string[], targetGroup: string): void {
    console.log('SelectedNodeAction: mover a...', selectedNodes, '->', targetGroup);
  }

  private onShowRelation(selectedNodes: string[]): void {
    if (selectedNodes.length !== 2) {
      this.relationBoxText = 'Debes seleccionar exactamente 2 nodos.';
      this.relationBoxVisible = true;
      return;
    }

    const snapshot = this.graphModel ?? this.getGraphModelFromSession();
    if (snapshot?.enrichedEdges?.length) {
      this.relationBoxText = this.groupRelationshipQuery.queryFromEnrichedEdges(
        snapshot.enrichedEdges,
        selectedNodes[0],
        selectedNodes[1]
      );
      this.relationBoxVisible = true;
      return;
    }

    if (!snapshot) {
      this.relationBoxText =
        'No hay grafo en sesión. Primero ejecuta "Crear grafo desde cache.txt" o "Analizar sistema Debian".';
      this.relationBoxVisible = true;
      return;
    }

    this.relationBoxText = 'Cargando relaciones...';
    this.relationBoxVisible = true;

    const payload: UpdateGraphModelRequest = {
      generator: this.lastGraphGenerator,
      groupsDefinitionFolder: this.groupsDefinitionFolder
    };

    this.httpClient.post<EnrichedEdgesResponse>(`${this.backendBaseUrl}/v1/enrichedEdges`, payload).subscribe({
      next: (response) => {
        this.relationBoxText = this.groupRelationshipQuery.queryFromEnrichedEdges(
          response.enrichedEdges,
          selectedNodes[0],
          selectedNodes[1]
        );
        this.relationBoxVisible = true;
      },
      error: () => {
        this.relationBoxText =
          'No se pudieron cargar las relaciones enriquecidas desde backend. Ejecuta "Crear grafo desde cache.txt" o "Analizar sistema Debian".';
        this.relationBoxVisible = true;
      }
    });
  }

  private listStructureGroupNames(): string[] {
    const nodes = this.graphModel?.structure.nodes ?? [];
    const fromSnapshot = nodes
      .map((node) => node.name)
      .filter((name) => name.startsWith('_[') && name !== '_[STRUCTURE]')
      .map((name) => name.replace(/^_\[(.+)\]$/, '$1').toLowerCase());
    if (fromSnapshot.length > 0) {
      return fromSnapshot;
    }
    return this.cachedStructureGroupNames;
  }

  private syncCanvasResolutionWithDisplay(): void {
    const canvas = this.workspaceCanvasRef?.nativeElement;
    if (!canvas) {
      return;
    }
    const dpr = Math.max(1, window.devicePixelRatio || 1);
    const targetWidth = Math.max(1, Math.round(canvas.clientWidth * dpr));
    const targetHeight = Math.max(1, Math.round(canvas.clientHeight * dpr));
    if (canvas.width !== targetWidth || canvas.height !== targetHeight) {
      canvas.width = targetWidth;
      canvas.height = targetHeight;
    }
    this.graphRenderer.render();
  }

  private setGraphModelSnapshot(snapshot: GraphModelSnapshot): void {
    this.graphModel = snapshot;
    try {
      sessionStorage.setItem(App.GRAPH_MODEL_SESSION_KEY, JSON.stringify(snapshot));
    } catch {
      // Ignore storage errors to keep UI functional.
    }
  }

  private restoreGraphModelFromSession(): void {
    const snapshot = this.getGraphModelFromSession();
    if (snapshot) {
      this.graphModel = snapshot;
    }
  }

  private getGraphModelFromSession(): GraphModelSnapshot | null {
    try {
      const raw = sessionStorage.getItem(App.GRAPH_MODEL_SESSION_KEY);
      if (!raw) {
        return null;
      }
      const parsed: unknown = JSON.parse(raw);
      if (!this.isGraphModelSnapshot(parsed)) {
        return null;
      }
      return parsed;
    } catch {
      // Ignore invalid session payloads.
      return null;
    }
  }

  private isGraphModelSnapshot(value: unknown): value is GraphModelSnapshot {
    if (!value || typeof value !== 'object') {
      return false;
    }
    const candidate = value as Partial<GraphModelSnapshot>;
    return (
      Array.isArray(candidate.nodes) &&
      Array.isArray(candidate.edges) &&
      !!candidate.structure &&
      Array.isArray(candidate.structure.nodes) &&
      Array.isArray(candidate.structure.edges)
    );
  }

  private isValidSvgPayload(payload: string): boolean {
    const parser = new DOMParser();
    const doc = parser.parseFromString(payload, 'image/svg+xml');
    const root = doc.documentElement;
    return root?.tagName.toLowerCase() === 'svg' && root.hasAttribute('viewBox');
  }
}
