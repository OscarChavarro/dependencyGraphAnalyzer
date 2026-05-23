import { AfterViewInit, Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { BACKEND_BASE_URL } from './config-tokens';
import { Html5CanvasGraphRenderer } from './render/Html5CanvasGraphRenderer';
import {
  GraphModel,
  GraphModelGenerator,
  GraphModelSnapshot,
  EnrichedEdgesResponse,
  GroupRelationsRequest,
  GroupRelationsResponse,
  MoveNodeRequest,
  MoveNodeResponse,
  CreateNewGroupRequest,
  CreateNewGroupResponse,
  CachedProject,
  CppProject,
  JavaProject,
  TypeScriptProject,
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
import { InvalidRelationshipDetector } from './localProcessing/InvalidRelationshipDetector';
import { FloodingOperations } from './localProcessing/FloodingOperations';
import {
  RelationDialogComponent,
  RelationEndpointClickEvent,
  RelationLineCheckboxToggleEvent,
  RelationLineViewModel
} from './relation-dialog.component';
import { Menu } from './model/menu';
import { MenuOption } from './model/menu-option';

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
  public isGeneratorPanelCollapsed = false;
  public relationBoxVisible = false;
  public relationBoxText = '';
  public relationBoxLines: RelationLineViewModel[] = [];
  public renameGroupDialogVisible = false;
  public renameGroupCurrentName = '';
  public renameGroupNextName = '';
  public readonly selectedLanguage;
  public readonly i18nKeys = I18N_KEYS;
  public nodeFilterPattern = '';
  public currentSvgNodeNames: string[] = [];
  public cachedProjects: CachedProject[] = [];
  public selectedCachedProjectId = '';
  public cppProjects: CppProject[] = [];
  public selectedCppProjectId = '';
  public javaProjects: JavaProject[] = [];
  public selectedJavaProjectId = '';
  public typeScriptProjects: TypeScriptProject[] = [];
  public selectedTypeScriptProjectId = '';

  private readonly endpointUrl: string;
  private readonly backendBaseUrl: string;
  private readonly defaultGroupsDefinitionFolder = '../u/';
  private readonly graphRenderer = new Html5CanvasGraphRenderer();
  private readonly groupRelationshipQuery = new GroupRelationshipQuery();
  private readonly invalidRelationshipDetector = new InvalidRelationshipDetector();
  private readonly floodingOperations = new FloodingOperations();
  private readonly graphSelectionModel = new GraphModel();
  private currentSvgFilename = 'structure.svg';
  private lastGraphGenerator: GraphModelGenerator = 'CACHE_LOADER';
  private lastInputFolders: string[] = [];
  private lastClasspath: string[] = [];
  private activeGroupsDefinitionFolder = this.defaultGroupsDefinitionFolder;
  private cachedStructureGroupNames: string[] = [];
  private readonly keyboardInteractionTechniques = new KeyboardInteractionTechniques(this.graphSelectionModel);
  private readonly drawingAreaNavigationInteractionTechnique = new DrawingAreaNavigationInteractionTechnique(this.graphRenderer);
  private readonly selectionInteractionTechnique = new SelectionInteractionTechnique(
    this.graphSelectionModel,
    this.graphRenderer,
    () => this.currentSvgFilename === 'structure.svg',
    (filename) => this.loadAndRenderGraphSvg(filename),
    (selectedNodes) => this.applySelection(selectedNodes),
    () => this.backToStructure()
  );
  private selectedNodeActionMenuInteractionTechnique?: SelectedNodeActionMenuInteractionTechnique;
  private readonly onWindowResize = (): void => {
    this.syncCanvasResolutionWithDisplay();
  };
  private readonly onGlobalGraphCycleKeyDown = (event: KeyboardEvent): void => {
    if (this.renameGroupDialogVisible && event.key === 'Escape') {
      event.preventDefault();
      this.closeRenameGroupDialog();
      return;
    }
    if (event.key !== 'ArrowLeft' && event.key !== 'ArrowRight') {
      return;
    }
    if (event.shiftKey || event.ctrlKey || event.metaKey || event.altKey) {
      return;
    }
    if (this.isTypingContext(event.target)) {
      return;
    }

    const direction: -1 | 1 = event.key === 'ArrowRight' ? 1 : -1;
    if (!this.cycleGraphByDirection(direction)) {
      return;
    }
    event.preventDefault();
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
    this.loadCachedProjects();
    this.loadCppProjects();
    this.loadJavaProjects();
    this.loadTypeScriptProjects();
  }

  public ngAfterViewInit(): void {
    const canvas = this.workspaceCanvasRef?.nativeElement;
    if (!canvas || !this.graphRenderer.attach(canvas)) {
      this.errorMessage = this.t(this.i18nKeys.shell.CANVAS_INIT_ERROR);
      return;
    }
    this.syncCanvasResolutionWithDisplay();
    window.addEventListener('resize', this.onWindowResize);
    window.addEventListener('keydown', this.onGlobalGraphCycleKeyDown);

    this.keyboardInteractionTechniques.attach(document.body);
    this.drawingAreaNavigationInteractionTechnique.attach(canvas, this.workspaceAreaRef?.nativeElement);
    this.selectionInteractionTechnique.attach(canvas);
    if (this.menuRenderer) {
      this.selectedNodeActionMenuInteractionTechnique = new SelectedNodeActionMenuInteractionTechnique(
        this.graphSelectionModel,
        this.menuRenderer,
        (event) => this.graphRenderer.pickNodeNameFromEvent(event),
        (event) => this.graphRenderer.pickRectangularNodeNameFromEvent(event),
        (selectedNodes) => this.applySelection(selectedNodes),
        () => this.currentSvgFilename === 'structure.svg',
        (filename) => this.loadAndRenderGraphSvg(filename),
        (selectedNodes) => this.onInundateDependencies(selectedNodes),
        (selectedNodes) => this.onInundateClients(selectedNodes),
        (selectedNodes) => this.onShowRelation(selectedNodes),
        (selectedNodes, targetGroup) => this.onMoveTo(selectedNodes, targetGroup),
        (groupToken) => this.onRenameGroupRequested(groupToken),
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
    window.removeEventListener('keydown', this.onGlobalGraphCycleKeyDown);
  }

  public closeRelationBox(): void {
    this.relationBoxVisible = false;
    this.relationBoxText = '';
    this.relationBoxLines = [];
  }

  public onRelationLineCheckboxToggled(event: RelationLineCheckboxToggleEvent): void {
    this.relationBoxLines = this.relationBoxLines.map((line) =>
      line === event.line ? { ...line, checked: event.checked } : line
    );
  }

  public createGraphFromCache(): void {
    const selectedProject = this.cachedProjects.find((project) => project.id === this.selectedCachedProjectId);
    if (!selectedProject) {
      this.updateGraphModel('CACHE_LOADER', this.defaultGroupsDefinitionFolder, []);
      return;
    }
    this.updateGraphModel(
      'CACHE_LOADER',
      selectedProject.groupsDefinitionFolder,
      [selectedProject.cacheFile]
    );
  }

  public analyzeDebianSystem(): void {
    this.updateGraphModel('DEBIAN_PACKAGE_GENERATOR', this.defaultGroupsDefinitionFolder, []);
  }

  public onCppProjectSelectionChange(event: Event): void {
    const target = event.target as HTMLSelectElement | null;
    this.selectedCppProjectId = target?.value ?? '';
  }

  public onCachedProjectSelectionChange(event: Event): void {
    const target = event.target as HTMLSelectElement | null;
    this.selectedCachedProjectId = target?.value ?? '';
  }

  public onJavaProjectSelectionChange(event: Event): void {
    const target = event.target as HTMLSelectElement | null;
    this.selectedJavaProjectId = target?.value ?? '';
  }

  public analyzeCppSources(): void {
    const selectedProject = this.cppProjects.find((project) => project.id === this.selectedCppProjectId);
    if (!selectedProject) {
      this.errorMessage = this.t(this.i18nKeys.shell.CPP_PROJECT_REQUIRED);
      return;
    }
    this.updateGraphModel('CPP_SOURCES', selectedProject.groupsDefinitionFolder, selectedProject.inputFolders);
  }

  public analyzeJavaSources(): void {
    const selectedProject = this.javaProjects.find((project) => project.id === this.selectedJavaProjectId);
    if (!selectedProject) {
      this.errorMessage = this.t(this.i18nKeys.shell.JAVA_PROJECT_REQUIRED);
      return;
    }
    this.updateGraphModel(
      'JAVA_SOURCES',
      selectedProject.groupsDefinitionFolder,
      selectedProject.inputFolders,
      selectedProject.classpath ?? []
    );
  }

  public onTypeScriptProjectSelectionChange(event: Event): void {
    const target = event.target as HTMLSelectElement | null;
    this.selectedTypeScriptProjectId = target?.value ?? '';
  }

  public analyzeTypeScriptSources(): void {
    const selectedProject = this.typeScriptProjects.find((project) => project.id === this.selectedTypeScriptProjectId);
    if (!selectedProject) {
      this.errorMessage = this.t(this.i18nKeys.shell.TYPESCRIPT_PROJECT_REQUIRED);
      return;
    }
    this.updateGraphModel('TYPESCRIPT_SOURCES', selectedProject.groupsDefinitionFolder, selectedProject.inputFolders);
  }

  public toggleSidePanel(): void {
    this.isPanelCollapsed = !this.isPanelCollapsed;
  }

  public toggleGeneratorPanel(): void {
    this.isGeneratorPanelCollapsed = !this.isGeneratorPanelCollapsed;
  }

  public isInStructureMode(): boolean {
    return this.currentSvgFilename === 'structure.svg';
  }

  public backToStructure(): void {
    if (this.currentSvgFilename === 'structure.svg') {
      return;
    }
    this.applySelection([]);
    this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
    this.loadAndRenderGraphSvg('structure.svg', 0);
  }

  public setLanguage(language: SupportedLanguage): void {
    this.i18nStateService.setLanguage(language);
  }

  public get filteredCurrentSvgNodeNames(): string[] {
    const pattern = this.nodeFilterPattern.trim().toLowerCase();
    if (!pattern) {
      return this.currentSvgNodeNames;
    }
    return this.currentSvgNodeNames.filter((name) => name.toLowerCase().includes(pattern));
  }

  public onNodeFilterInput(event: Event): void {
    const target = event.target as HTMLInputElement | null;
    this.nodeFilterPattern = target?.value ?? '';
  }

  public onNodeListSelectionChange(event: Event): void {
    const target = event.target as HTMLSelectElement | null;
    if (!target) {
      return;
    }
    const selectedNodes = Array.from(target.selectedOptions).map((option) => option.value);
    this.applySelection(selectedNodes);
  }

  public onNodeListContextMenu(event: MouseEvent): void {
    const nodeName = this.extractNodeNameFromListContextMenuEvent(event);
    if (!nodeName) {
      return;
    }
    this.selectedNodeActionMenuInteractionTechnique?.openContextMenuForNode(nodeName, event);
  }

  public onRenameGroupInput(event: Event): void {
    const target = event.target as HTMLInputElement | null;
    const normalized = (target?.value ?? '').replace(/ /g, '_');
    this.renameGroupNextName = normalized;
    if (target && target.value !== normalized) {
      target.value = normalized;
    }
  }

  public closeRenameGroupDialog(): void {
    this.renameGroupDialogVisible = false;
    this.renameGroupCurrentName = '';
    this.renameGroupNextName = '';
  }

  public onRenameGroupKeyDown(event: KeyboardEvent): void {
    if (event.key === ' ') {
      event.preventDefault();
      const input = event.target as HTMLInputElement | null;
      if (!input) {
        return;
      }
      const start = input.selectionStart ?? input.value.length;
      const end = input.selectionEnd ?? input.value.length;
      const nextValue = `${input.value.slice(0, start)}_${input.value.slice(end)}`;
      input.value = nextValue;
      input.setSelectionRange(start + 1, start + 1);
      this.renameGroupNextName = nextValue;
      return;
    }
    if (event.key === 'Escape') {
      event.preventDefault();
      this.closeRenameGroupDialog();
      return;
    }
    if (event.key !== 'Enter') {
      return;
    }
    event.preventDefault();
    const newName = this.renameGroupNextName.trim();
    if (!/^\d+_[A-Za-z0-9_]+$/.test(newName)) {
      return;
    }
    this.requestRenameGroupInBackend(this.renameGroupCurrentName, newName);
    this.closeRenameGroupDialog();
  }

  public isNodeSelected(nodeName: string): boolean {
    return this.graphSelectionModel.selectedNodes.includes(nodeName);
  }

  public onRelationEndpointClick(event: RelationEndpointClickEvent): void {
    this.jumpToGroupWithSelectedNode(event.endpoint);
  }

  public onRelationEndpointContextMenu(event: RelationEndpointClickEvent): void {
    const packageName = this.extractPackageFromEndpoint(event.endpoint);
    if (!packageName) {
      return;
    }
    const endpointGroup = this.extractGroupFromEndpoint(event.endpoint);
    const originNodes = this.resolveMoveOriginNodesForRelationSelection(packageName, endpointGroup);
    const counterpartGroupName = this.extractGroupFromEndpoint(event.counterpartEndpoint ?? '');
    const counterpartGroupLower = counterpartGroupName?.toLowerCase() ?? null;
    const groups = this.listStructureGroupNames();
    const submenu = new Menu(
      groups.map(
        (groupName) =>
          new MenuOption(
            groupName,
            groupName,
            () => {
              this.closeRelationBox();
              this.onMoveTo(originNodes, groupName, endpointGroup);
            },
            null,
            null
          )
      )
    );
    const options: MenuOption[] = [
      new MenuOption(
        'shell.MENU_MOVE_TO',
        this.t(this.i18nKeys.shell.MENU_MOVE_TO),
        null,
        submenu.options[0] ?? null,
        submenu
      )
    ];
    if (counterpartGroupLower) {
      options.push(
        new MenuOption(
          `shell.MENU_MOVE_TO_${counterpartGroupLower}`,
          `${this.t(this.i18nKeys.shell.MOVE_VERB)} a ${counterpartGroupLower}`,
          () => {
            this.closeRelationBox();
            this.onMoveTo(originNodes, counterpartGroupLower, endpointGroup);
          },
          null,
          null
        )
      );
    }
    const menu = new Menu(options);
    this.menuRenderer?.open(menu, event.mouseX, event.mouseY);
  }

  public t(id: TranslationKey): string {
    return this.i18nService.get(id, this.selectedLanguage());
  }

  private loadCppProjects(): void {
    this.httpClient.get<CppProject[]>(`${this.backendBaseUrl}/v1/cppProjects`).subscribe({
      next: (projects) => {
        this.cppProjects = Array.isArray(projects) ? projects : [];
        this.selectedCppProjectId = this.cppProjects[0]?.id ?? '';
      },
      error: () => {
        this.cppProjects = [];
        this.selectedCppProjectId = '';
      }
    });
  }

  private loadCachedProjects(): void {
    this.httpClient.get<CachedProject[]>(`${this.backendBaseUrl}/v1/cachedProjects`).subscribe({
      next: (projects) => {
        this.cachedProjects = Array.isArray(projects) ? projects : [];
        this.selectedCachedProjectId = this.cachedProjects[0]?.id ?? '';
      },
      error: () => {
        this.cachedProjects = [];
        this.selectedCachedProjectId = '';
      }
    });
  }

  private loadJavaProjects(): void {
    this.httpClient.get<JavaProject[]>(`${this.backendBaseUrl}/v1/javaProjects`).subscribe({
      next: (projects) => {
        this.javaProjects = Array.isArray(projects) ? projects : [];
        this.selectedJavaProjectId = this.javaProjects[0]?.id ?? '';
      },
      error: () => {
        this.javaProjects = [];
        this.selectedJavaProjectId = '';
      }
    });
  }

  private loadTypeScriptProjects(): void {
    this.httpClient.get<TypeScriptProject[]>(`${this.backendBaseUrl}/v1/typescriptProjects`).subscribe({
      next: (projects) => {
        this.typeScriptProjects = Array.isArray(projects) ? projects : [];
        this.selectedTypeScriptProjectId = this.typeScriptProjects[0]?.id ?? '';
      },
      error: () => {
        this.typeScriptProjects = [];
        this.selectedTypeScriptProjectId = '';
      }
    });
  }

  private updateGraphModel(
    generator: GraphModelGenerator,
    groupsDefinitionFolder: string,
    inputFolders: string[],
    classpath: string[] = []
  ): void {
    if (this.isLoading) {
      return;
    }
    this.lastGraphGenerator = generator;
    this.lastInputFolders = [...inputFolders];
    this.lastClasspath = [...classpath];
    this.activeGroupsDefinitionFolder = groupsDefinitionFolder;
    this.isLoading = true;
    this.errorMessage = '';

    const payload: UpdateGraphModelRequest = {
      generator,
      groupsDefinitionFolder,
      inputFolders,
      classpath
    };

    this.httpClient.post<UpdateGraphModelResponse>(this.endpointUrl, payload).subscribe({
      next: (response) => {
        try {
          this.setGraphModelSnapshot(response.graphModel);
          this.applySelection([]);
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
    const svgUrl = `${this.backendBaseUrl}/output/svg/${filename}?ts=${Date.now()}`;
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
          this.currentSvgNodeNames = this.listAndSortCurrentSvgNodeNames();
          this.graphSelectionModel.selectedNodes = this.graphSelectionModel.selectedNodes
            .filter((name) => this.currentSvgNodeNames.includes(name));
          this.applySelection(this.graphSelectionModel.selectedNodes);
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
    this.applyFlooding(selectedNodes, 'dependencies');
  }

  private onRenameGroupRequested(groupToken: string): void {
    this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
    this.menuRenderer?.close();
    this.renameGroupCurrentName = groupToken.toLowerCase();
    this.renameGroupNextName = '';
    this.renameGroupDialogVisible = true;
  }

  private requestRenameGroupInBackend(groupToken: string, newGroupName: string): void {
    if (this.isLoading) {
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    const payload = {
      groupFolder: this.activeGroupsDefinitionFolder,
      oldGroupName: groupToken,
      newGroupName
    };
    this.httpClient.post(`${this.backendBaseUrl}/v1/renameNode`, payload).subscribe({
      next: () => {
        this.refreshGraphModelAndReloadCurrentSvg();
      },
      error: () => {
        this.errorMessage = this.t(this.i18nKeys.shell.RENAME_GROUP_BACKEND_NOT_READY);
        this.isLoading = false;
      }
    });
  }

  private onInundateClients(selectedNodes: string[]): void {
    this.applyFlooding(selectedNodes, 'clients');
  }

  private onMoveTo(selectedNodes: string[], targetGroup: string, originGroupHint: string | null = null): void {
    this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
    this.menuRenderer?.close();

    if (this.isLoading) {
      return;
    }

    const originNodes = [...new Set(selectedNodes.map((node) => node.trim()).filter((node) => node.length > 0))];
    if (originNodes.length === 0) {
      this.errorMessage = 'Debes seleccionar al menos un nodo para mover.';
      return;
    }

    const originGroupRaw = originGroupHint ?? this.extractGroupFromCurrentSvgFilename();
    const originGroup = originGroupRaw?.toLowerCase() ?? null;
    const destinationGroup = targetGroup.toLowerCase();
    if (!originGroup) {
      this.errorMessage = 'No se pudo determinar el grupo origen para mover el nodo.';
      return;
    }

    this.performMoveTo(originNodes, originGroup, destinationGroup);
  }

  public onCreateNewGroupRequested(newGroupName: string): void {
    this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
    this.menuRenderer?.close();
    if (this.isLoading) {
      return;
    }

    const originNodes = [...new Set(this.graphSelectionModel.selectedNodes.map((node) => node.trim()).filter((node) => node.length > 0))];
    if (originNodes.length === 0) {
      this.errorMessage = 'Debes seleccionar al menos un nodo para mover.';
      return;
    }

    const originGroup = this.extractGroupFromCurrentSvgFilename()?.toLowerCase() ?? null;
    if (!originGroup) {
      this.errorMessage = 'No se pudo determinar el grupo origen para mover el nodo.';
      return;
    }

    const createPayload: CreateNewGroupRequest = {
      groupFolder: this.activeGroupsDefinitionFolder,
      newGroupName
    };

    this.isLoading = true;
    this.errorMessage = '';
    this.httpClient.put<CreateNewGroupResponse>(`${this.backendBaseUrl}/v1/createNewGroup`, createPayload).subscribe({
      next: () => this.performMoveTo(originNodes, originGroup, newGroupName, false),
      error: (error) => {
        this.errorMessage = this.extractApiErrorMessage(error, 'Could not create the new group.');
        this.isLoading = false;
      }
    });
  }

  private performMoveTo(
    originNodes: string[],
    originGroup: string,
    destinationGroup: string,
    startLoading = true
  ): void {
    if (startLoading) {
      this.isLoading = true;
      this.errorMessage = '';
    }

    const movePayload: MoveNodeRequest = {
      groupFolder: this.activeGroupsDefinitionFolder,
      originGroup,
      originNodes,
      destinationGroup: destinationGroup.toLowerCase()
    };

    this.httpClient.post<MoveNodeResponse>(`${this.backendBaseUrl}/v1/moveNode`, movePayload).subscribe({
      next: () => this.refreshGraphModelAndReloadCurrentSvg(),
      error: (error) => {
        this.errorMessage = this.extractApiErrorMessage(error, 'No se pudo mover el nodo al grupo destino.');
        this.isLoading = false;
      }
    });
  }

  private extractApiErrorMessage(error: unknown, fallback: string): string {
    if (!(error instanceof HttpErrorResponse) || !error.error) {
      return fallback;
    }
    const payload = error.error as { details?: string[]; message?: string };
    if (Array.isArray(payload.details) && payload.details.length > 0) {
      return payload.details.join(' | ');
    }
    if (typeof payload.message === 'string' && payload.message.trim().length > 0) {
      return payload.message;
    }
    return fallback;
  }

  private refreshGraphModelAndReloadCurrentSvg(): void {
    const payload: UpdateGraphModelRequest = {
      generator: this.lastGraphGenerator,
      groupsDefinitionFolder: this.activeGroupsDefinitionFolder,
      inputFolders: this.lastInputFolders,
      classpath: this.lastClasspath
    };
    const svgToReload = this.currentSvgFilename;

    this.httpClient.post<UpdateGraphModelResponse>(this.endpointUrl, payload).subscribe({
      next: (response) => {
        try {
          this.setGraphModelSnapshot(response.graphModel);
          this.applySelection([]);
          this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
          this.menuRenderer?.close();
          queueMicrotask(() => this.loadAndRenderGraphSvg(svgToReload, 0));
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

  private onShowRelation(selectedNodes: string[]): void {
    if (selectedNodes.length !== 2) {
      this.setRelationBoxContent('Debes seleccionar exactamente 2 nodos.');
      this.relationBoxVisible = true;
      return;
    }

    if (!this.isInStructureMode()) {
      this.setRelationBoxContent('Mostrar relaciones solo aplica en vista structure.');
      this.relationBoxVisible = true;
      return;
    }

    const snapshot = this.graphModel ?? this.getGraphModelFromSession();
    if (!snapshot) {
      this.setRelationBoxContent(
        'No hay grafo en sesión. Primero ejecuta "Crear grafo desde cache.txt" o "Analizar sistema Debian".'
      );
      this.relationBoxVisible = true;
      return;
    }

    const relationPayload: GroupRelationsRequest = {
      groupA: this.normalizeGroupForRelations(selectedNodes[0]),
      groupB: this.normalizeGroupForRelations(selectedNodes[1])
    };

    this.httpClient.post<GroupRelationsResponse>(`${this.backendBaseUrl}/v1/groupRelations`, relationPayload).subscribe({
      next: (response) => {
        if (response.relations.length > 0) {
          this.setRelationBoxContent(response.relations.join('\n'));
        } else {
          this.setRelationBoxContent(this.groupRelationshipQuery.query(
            snapshot,
            selectedNodes[0],
            selectedNodes[1]
          ));
        }
        this.relationBoxVisible = true;
      },
      error: () => {
        this.setRelationBoxContent(this.groupRelationshipQuery.query(
          snapshot,
          selectedNodes[0],
          selectedNodes[1]
        ));
        this.relationBoxVisible = true;
      }
    });
  }

  private normalizeGroupForRelations(value: string): string {
    const trimmed = (value ?? '').trim();
    const structureMatch = trimmed.match(/^_?\[([^\]]+)\]$/);
    let normalized = structureMatch?.[1] ?? trimmed;
    normalized = normalized.replace(/\.txt$/i, '');
    return normalized.toLowerCase();
  }

  private setRelationBoxContent(text: string): void {
    this.relationBoxText = text;
    this.relationBoxLines = this.buildRelationLineView(text);
  }

  private buildRelationLineView(text: string): RelationLineViewModel[] {
    const lines = text
      .split('\n')
      .map((line) => line.trim())
      .filter((line) => line.length > 0);
    if (lines.length === 0 || lines.some((line) => !line.includes('->'))) {
      return [];
    }
    return lines.map((line) => ({
      text: line,
      invalid: this.invalidRelationshipDetector.isInvalid(line),
      checked: false,
      ...this.parseRelationLine(line)
    }));
  }

  private resolveMoveOriginNodesForRelationSelection(fallbackNode: string, endpointGroup: string | null): string[] {
    const selectedNodes = this.relationBoxLines
      .filter((line) => line.checked === true)
      .flatMap((line) => this.extractRelationNodesFromLineForGroup(line, endpointGroup))
      .filter((name) => name.length > 0);
    const uniqueSelectedNodes = [...new Set(selectedNodes)];
    if (uniqueSelectedNodes.length === 0) {
      return [fallbackNode];
    }
    return uniqueSelectedNodes;
  }

  private extractRelationNodesFromLineForGroup(line: RelationLineViewModel, endpointGroup: string | null): string[] {
    if (!line.clickable || !line.sourceEndpoint || !line.targetEndpoint) {
      return [];
    }
    if (!endpointGroup) {
      return [];
    }
    const nodes: string[] = [];
    if (this.extractGroupFromEndpoint(line.sourceEndpoint)?.toLowerCase() === endpointGroup.toLowerCase()) {
      const packageName = this.extractPackageFromEndpoint(line.sourceEndpoint);
      if (packageName) {
        nodes.push(packageName);
      }
    }
    if (this.extractGroupFromEndpoint(line.targetEndpoint)?.toLowerCase() === endpointGroup.toLowerCase()) {
      const packageName = this.extractPackageFromEndpoint(line.targetEndpoint);
      if (packageName) {
        nodes.push(packageName);
      }
    }
    return nodes;
  }

  private parseRelationLine(line: string): Omit<RelationLineViewModel, 'text' | 'invalid'> {
    const parts = line.split('->').map((value) => value.trim());
    if (parts.length !== 2 || !parts[0] || !parts[1]) {
      return { clickable: false };
    }
    return {
      sourceEndpoint: parts[0],
      targetEndpoint: parts[1],
      clickable: true
    };
  }

  private extractPackageFromEndpoint(endpoint: string): string | null {
    const match = endpoint.trim().match(/^\[[^\]]+\]\.(.+)$/);
    return match?.[1] ?? null;
  }

  private extractGroupFromEndpoint(endpoint: string): string | null {
    const match = endpoint.trim().match(/^\[([^\]]+)\]\./);
    return match?.[1] ?? null;
  }

  private jumpToGroupWithSelectedNode(endpoint: string): void {
    const groupName = this.extractGroupFromEndpoint(endpoint);
    const packageName = this.extractPackageFromEndpoint(endpoint);
    if (!groupName || !packageName) {
      return;
    }
    const filename = `${groupName.toLowerCase()}.svg`;
    this.graphSelectionModel.selectSingle(packageName);
    this.applySelection(this.graphSelectionModel.selectedNodes);
    this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
    this.loadAndRenderGraphSvg(filename, 0);
  }

  private extractGroupFromCurrentSvgFilename(): string | null {
    if (this.currentSvgFilename === 'structure.svg') {
      return null;
    }
    const match = this.currentSvgFilename.match(/^(.+)\.svg$/i);
    if (!match || !match[1]) {
      return null;
    }
    return match[1];
  }

  private cycleGraphByDirection(direction: -1 | 1): boolean {
    const groups = this.listCyclableGroupNames();
    if (groups.length === 0) {
      return false;
    }

    if (this.isInStructureMode()) {
      const targetGroup = direction > 0 ? groups[0] : groups[groups.length - 1];
      this.openGroupGraphByName(targetGroup);
      return true;
    }

    const currentGroup = this.extractGroupFromCurrentSvgFilename()?.toLowerCase() ?? null;
    if (!currentGroup) {
      this.loadAndRenderGraphSvg('structure.svg', 0);
      return true;
    }
    const currentIndex = groups.indexOf(currentGroup);
    if (currentIndex < 0) {
      this.loadAndRenderGraphSvg('structure.svg', 0);
      return true;
    }

    const nextIndex = currentIndex + direction;
    if (nextIndex < 0 || nextIndex >= groups.length) {
      this.loadAndRenderGraphSvg('structure.svg', 0);
      return true;
    }

    this.openGroupGraphByName(groups[nextIndex]);
    return true;
  }

  private openGroupGraphByName(groupName: string): void {
    const normalizedGroupName = groupName.trim().toLowerCase();
    if (!normalizedGroupName) {
      return;
    }
    this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
    this.menuRenderer?.close();
    this.loadAndRenderGraphSvg(`${normalizedGroupName}.svg`, 0);
  }

  private listCyclableGroupNames(): string[] {
    const allGroups = this.listStructureGroupNames();
    const groupNodeCount = this.buildGroupNodeCountIndex();
    if (groupNodeCount.size === 0) {
      return allGroups;
    }
    const filtered = allGroups.filter((group) => (groupNodeCount.get(group) ?? 0) > 0);
    return filtered.length > 0 ? filtered : allGroups;
  }

  private buildGroupNodeCountIndex(): Map<string, number> {
    const nodes = this.graphModel?.nodes ?? [];
    const counts = new Map<string, number>();
    for (const node of nodes) {
      const groupName = this.extractGroupNameFromMemberNode(node.name);
      if (groupName) {
        counts.set(groupName, (counts.get(groupName) ?? 0) + 1);
      }
    }
    return counts;
  }

  private extractGroupNameFromMemberNode(nodeName: string): string | null {
    const normalizedName = nodeName.trim();
    if (normalizedName.startsWith('_[')) {
      return null;
    }
    if (normalizedName.startsWith('[')) {
      const closingBracketIndex = normalizedName.indexOf(']');
      if (closingBracketIndex > 1 && normalizedName.charAt(closingBracketIndex + 1) === '.') {
        return normalizedName.substring(1, closingBracketIndex).toLowerCase();
      }
      return null;
    }
    const separatorIndex = normalizedName.indexOf('.');
    if (separatorIndex > 0) {
      return normalizedName.substring(0, separatorIndex).toLowerCase();
    }
    return null;
  }

  private listStructureGroupNames(): string[] {
    const fromCurrentStructureList = this.currentSvgNodeNames
      .filter((name) => name.startsWith('_[') && name !== '_[STRUCTURE]')
      .map((name) => this.extractGroupNameFromStructureLabel(name))
      .filter((name): name is string => name !== null);
    if (fromCurrentStructureList.length > 0) {
      return [...fromCurrentStructureList].sort((a, b) => this.compareGroupNames(a, b));
    }

    const nodes = this.graphModel?.structure.nodes ?? [];
    const fromSnapshot = nodes
      .map((node) => node.name)
      .filter((name) => name.startsWith('_[') && name !== '_[STRUCTURE]')
      .map((name) => this.extractGroupNameFromStructureLabel(name))
      .filter((name): name is string => name !== null);
    if (fromSnapshot.length > 0) {
      return [...fromSnapshot].sort((a, b) => this.compareGroupNames(a, b));
    }
    return [...this.cachedStructureGroupNames].sort((a, b) => this.compareGroupNames(a, b));
  }

  private isTypingContext(target: EventTarget | null): boolean {
    if (!(target instanceof HTMLElement)) {
      return false;
    }
    if (target.isContentEditable) {
      return true;
    }
    return target instanceof HTMLInputElement || target instanceof HTMLTextAreaElement;
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
    this.updateRendererFillOverrides();
    try {
      sessionStorage.setItem(App.GRAPH_MODEL_SESSION_KEY, JSON.stringify({ graphModel: snapshot }));
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
      if (this.isGraphModelSnapshot(parsed)) {
        return parsed;
      }
      if (!this.isGraphSessionPayload(parsed) || !this.isGraphModelSnapshot(parsed.graphModel)) {
        return null;
      }
      return parsed.graphModel;
    } catch {
      // Ignore invalid session payloads.
      return null;
    }
  }

  private isGraphSessionPayload(value: unknown): value is { graphModel: unknown } {
    if (!value || typeof value !== 'object') {
      return false;
    }
    return 'graphModel' in value;
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

  private applyFlooding(selectedNodes: string[], mode: 'dependencies' | 'clients'): void {
    const snapshot = this.graphModel ?? this.getGraphModelFromSession();
    if (!snapshot) {
      return;
    }
    this.runFloodingWithSnapshot(snapshot, selectedNodes, mode);
  }

  private runFloodingWithSnapshot(
    snapshot: GraphModelSnapshot,
    selectedNodes: string[],
    mode: 'dependencies' | 'clients'
  ): void {
    if (snapshot.enrichedEdges?.length) {
      this.applyFloodingFromSnapshot(snapshot, selectedNodes, mode);
      return;
    }

    const payload: UpdateGraphModelRequest = {
      generator: this.lastGraphGenerator,
      groupsDefinitionFolder: this.activeGroupsDefinitionFolder,
      inputFolders: this.lastInputFolders,
      classpath: this.lastClasspath
    };
    this.httpClient.post<EnrichedEdgesResponse>(`${this.backendBaseUrl}/v1/enrichedEdges`, payload).subscribe({
      next: (response) => {
        const enrichedSnapshot: GraphModelSnapshot = { ...snapshot, enrichedEdges: response.enrichedEdges };
        this.setGraphModelSnapshot(enrichedSnapshot);
        this.applyFloodingFromSnapshot(enrichedSnapshot, selectedNodes, mode);
      },
      error: () => {
        // Fall back to local edges if enriched edges are unavailable.
        this.applyFloodingFromSnapshot(snapshot, selectedNodes, mode);
      }
    });
  }

  private applyFloodingFromSnapshot(
    snapshot: GraphModelSnapshot,
    selectedNodes: string[],
    mode: 'dependencies' | 'clients'
  ): void {
    const currentSvgNodeNames = this.graphRenderer.getInteractiveEllipses().map((ellipse) => ellipse.nodeName);
    const currentGroupToken = this.extractCurrentGroupToken();
    const result = mode === 'dependencies'
      ? this.floodingOperations.inundateDependencies(snapshot, selectedNodes, currentSvgNodeNames, currentGroupToken)
      : this.floodingOperations.inundateClients(snapshot, selectedNodes, currentSvgNodeNames, currentGroupToken);

    this.setGraphModelSnapshot(result.floodedSnapshot);
    this.applySelection([...new Set([
      ...this.graphSelectionModel.selectedNodes,
      ...result.additionalSelectedNodes
    ])]);
    this.selectedNodeActionMenuInteractionTechnique?.closeMenu();
  }

  private extractCurrentGroupToken(): string | null {
    const groupName = this.extractGroupFromCurrentSvgFilename();
    if (!groupName) {
      return null;
    }
    return `[${groupName}]`;
  }

  private updateRendererFillOverrides(): void {
    this.graphRenderer.setNodeFillColorOverrides(this.buildNodeFillOverridesMap(this.graphModel));
  }

  private buildNodeFillOverridesMap(snapshot: GraphModelSnapshot | null): Map<string, string> {
    const overrides = new Map<string, string>();
    if (!snapshot) {
      return overrides;
    }
    for (const node of [...snapshot.nodes, ...snapshot.structure.nodes]) {
      const fillColorOverride = node.fillColorOverride?.trim();
      if (node.name && fillColorOverride) {
        overrides.set(node.name, fillColorOverride);
      }
    }
    return overrides;
  }

  private listAndSortCurrentSvgNodeNames(): string[] {
    if (this.isInStructureMode()) {
      const uniqueGroupNames = new Set(
        this.graphRenderer
          .getInteractiveEllipses()
          .map((ellipse) => ellipse.nodeName)
          .filter((name) => name && name.startsWith('_[') && name !== '_[STRUCTURE]')
      );
      return [...uniqueGroupNames].sort((a, b) =>
        this.compareGroupNames(
          this.extractGroupNameFromStructureLabel(a) ?? a.toLowerCase(),
          this.extractGroupNameFromStructureLabel(b) ?? b.toLowerCase()
        )
      );
    }

    const uniqueNames = new Set(
      this.graphRenderer
        .getInteractiveEllipses()
        .map((ellipse) => ellipse.nodeName)
        .filter((name) => name && !name.startsWith('[') && !name.startsWith('_['))
    );
    return [...uniqueNames].sort((a, b) => a.localeCompare(b));
  }

  private extractGroupNameFromStructureLabel(nodeName: string): string | null {
    const normalizedName = nodeName.trim();
    if (!normalizedName.startsWith('_[') || !normalizedName.endsWith(']')) {
      return null;
    }
    if (normalizedName.length <= 3) {
      return null;
    }
    return normalizedName.substring(2, normalizedName.length - 1).toLowerCase();
  }

  private compareGroupNames(groupA: string, groupB: string): number {
    const leadingNumberA = this.extractLeadingNumber(groupA);
    const leadingNumberB = this.extractLeadingNumber(groupB);
    if (leadingNumberA !== null && leadingNumberB !== null && leadingNumberA !== leadingNumberB) {
      return leadingNumberA - leadingNumberB;
    }
    return groupA.localeCompare(groupB);
  }

  private extractLeadingNumber(groupName: string): number | null {
    const match = groupName.match(/^(\d+)_/);
    if (!match || !match[1]) {
      return null;
    }
    const parsed = Number.parseInt(match[1], 10);
    return Number.isNaN(parsed) ? null : parsed;
  }

  private extractNodeNameFromListContextMenuEvent(event: MouseEvent): string | null {
    const targetElement = event.target as HTMLElement | null;
    const targetOption = targetElement?.closest('option');
    if (targetOption) {
      return targetOption.getAttribute('value') ?? targetOption.textContent?.trim() ?? null;
    }

    const elementAtPoint = document.elementFromPoint(event.clientX, event.clientY);
    if (elementAtPoint instanceof HTMLOptionElement) {
      return elementAtPoint.value || elementAtPoint.textContent?.trim() || null;
    }

    return null;
  }

  private applySelection(selectedNodes: string[]): void {
    this.graphSelectionModel.selectedNodes = [...new Set(selectedNodes)];
    this.graphRenderer.setSelectedNodes(this.graphSelectionModel.selectedNodes);
  }
}
