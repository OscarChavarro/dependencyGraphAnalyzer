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
import { MouseInteractionTechniques } from './gui/MouseInteractionTechniques';
import { KeyboardInteractionTechniques } from './gui/KeyboardInteractionTechniques';

@Component({
  selector: 'app-root',
  imports: [CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements AfterViewInit {
  @ViewChild('workspaceArea')
  private workspaceAreaRef?: ElementRef<HTMLElement>;

  @ViewChild('workspaceCanvas')
  private workspaceCanvasRef?: ElementRef<HTMLCanvasElement>;

  public graphModel: GraphModelSnapshot | null = null;
  public isLoading = false;
  public errorMessage = '';

  private readonly endpointUrl: string;
  private readonly groupsDefinitionFolder = '../u/';
  private readonly graphRenderer = new Html5CanvasGraphRenderer();
  private readonly graphSelectionModel = new GraphModel();
  private readonly keyboardInteractionTechniques = new KeyboardInteractionTechniques(this.graphSelectionModel);
  private readonly mouseInteractionTechniques = new MouseInteractionTechniques(
    this.graphSelectionModel,
    this.graphRenderer,
    this.keyboardInteractionTechniques
  );

  constructor(
    private readonly httpClient: HttpClient,
    @Inject(BACKEND_BASE_URL) backendBaseUrl: string
  ) {
    this.endpointUrl = `${backendBaseUrl}/v1/updateGraph`;
  }

  public ngAfterViewInit(): void {
    const canvas = this.workspaceCanvasRef?.nativeElement;
    if (!canvas || !this.graphRenderer.attach(canvas)) {
      this.errorMessage = 'No se pudo inicializar el canvas';
      return;
    }

    this.keyboardInteractionTechniques.attach(this.workspaceAreaRef?.nativeElement);
    this.mouseInteractionTechniques.attach(canvas);
    this.loadAndRenderStructureSvgVector();
  }

  public createGraphFromCache(): void {
    this.updateGraphModel('CACHE_LOADER');
  }

  public analyzeDebianSystem(): void {
    this.updateGraphModel('DEBIAN_PACKAGE_GENERATOR');
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
        this.loadAndRenderStructureSvgVector();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo actualizar el grafo';
        this.isLoading = false;
      }
    });
  }

  private loadAndRenderStructureSvgVector(): void {
    this.httpClient
      .get(`/output/svg/structure.svg?ts=${Date.now()}`, { responseType: 'text' })
      .subscribe({
        next: (svgText) => {
          this.graphRenderer.loadFromSvgText(svgText);
          this.graphRenderer.setSelectedNodes(this.graphSelectionModel.selectedNodes);
        },
        error: () => {
          this.errorMessage = 'No se pudo cargar output/svg/structure.svg';
        }
      });
  }
}
