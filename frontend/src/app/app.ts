import { AfterViewInit, Component, ElementRef, Inject, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { BACKEND_BASE_URL } from './config-tokens';
import { Html5CanvasGraphRenderer } from './render/Html5CanvasGraphRenderer';
import {
  GraphModel,
  GraphModelGenerator,
  GraphModelSnapshot,
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

@Component({
  selector: 'app-root',
  imports: [CommonModule, MenuRenderer],
  templateUrl: './app.html',
  styleUrl: './app.sass'
})
export class App implements AfterViewInit {
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
  public readonly selectedLanguage;
  public readonly i18nKeys = I18N_KEYS;

  private readonly endpointUrl: string;
  private readonly groupsDefinitionFolder = '../u/';
  private readonly graphRenderer = new Html5CanvasGraphRenderer();
  private readonly graphSelectionModel = new GraphModel();
  private currentSvgFilename = 'structure.svg';
  private cachedStructureGroupNames: string[] = [];
  private readonly keyboardInteractionTechniques = new KeyboardInteractionTechniques(this.graphSelectionModel);
  private readonly drawingAreaNavigationInteractionTechnique = new DrawingAreaNavigationInteractionTechnique(this.graphRenderer);
  private readonly selectionInteractionTechnique = new SelectionInteractionTechnique(this.graphSelectionModel, this.graphRenderer);
  private selectedNodeActionMenuInteractionTechnique?: SelectedNodeActionMenuInteractionTechnique;

  constructor(
    private readonly httpClient: HttpClient,
    private readonly i18nStateService: I18nStateService,
    private readonly i18nService: I18nService,
    @Inject(BACKEND_BASE_URL) backendBaseUrl: string
  ) {
    this.endpointUrl = `${backendBaseUrl}/v1/updateGraph`;
    this.selectedLanguage = this.i18nStateService.selectedLanguage;
  }

  public ngAfterViewInit(): void {
    const canvas = this.workspaceCanvasRef?.nativeElement;
    if (!canvas || !this.graphRenderer.attach(canvas)) {
      this.errorMessage = this.t(this.i18nKeys.shell.CANVAS_INIT_ERROR);
      return;
    }

    this.keyboardInteractionTechniques.attach(this.workspaceAreaRef?.nativeElement);
    this.drawingAreaNavigationInteractionTechnique.attach(canvas);
    this.selectionInteractionTechnique.attach(canvas);
    if (this.menuRenderer) {
      this.selectedNodeActionMenuInteractionTechnique = new SelectedNodeActionMenuInteractionTechnique(
        this.graphSelectionModel,
        this.menuRenderer,
        () => this.currentSvgFilename === 'structure.svg',
        (filename) => this.loadAndRenderGraphSvg(filename),
        (selectedNodes) => this.onInundateDependencies(selectedNodes),
        (selectedNodes) => this.onInundateClients(selectedNodes),
        (selectedNodes, targetGroup) => this.onMoveTo(selectedNodes, targetGroup),
        () => this.listStructureGroupNames(),
        (id) => this.t(id)
      );
      this.selectedNodeActionMenuInteractionTechnique.attach();
    }
    this.loadAndRenderGraphSvg('structure.svg');
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
    this.isLoading = true;
    this.errorMessage = '';

    const payload: UpdateGraphModelRequest = {
      generator,
      groupsDefinitionFolder: this.groupsDefinitionFolder
    };

    this.httpClient.post<UpdateGraphModelResponse>(this.endpointUrl, payload).subscribe({
      next: (response) => {
        this.graphModel = response.graphModel;
        this.graphSelectionModel.clearSelection();
        this.graphRenderer.setSelectedNodes(this.graphSelectionModel.selectedNodes);
        this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
        this.loadAndRenderGraphSvg('structure.svg');
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = this.t(this.i18nKeys.shell.GRAPH_UPDATE_ERROR);
        this.isLoading = false;
      }
    });
  }

  private loadAndRenderGraphSvg(filename: string): void {
    this.currentSvgFilename = filename;
    this.httpClient
      .get(`/output/svg/${filename}?ts=${Date.now()}`, { responseType: 'text' })
      .subscribe({
        next: (svgText) => {
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
}
