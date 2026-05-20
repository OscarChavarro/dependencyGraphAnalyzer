import { Component, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { BACKEND_BASE_URL } from './config-tokens';

type GraphModelGenerator = 'CACHE_LOADER' | 'DEBIAN_PACKAGE_GENERATOR';

interface UpdateGraphModelRequest {
  generator: GraphModelGenerator;
  groupsDefinitionFolder: string;
}

interface GraphModelNode {
  name: string;
  isBad: boolean;
  marked: boolean;
  group: boolean;
  sink: boolean;
  source: boolean;
  variant: boolean;
  boldName: boolean;
}

interface GraphModelEdge {
  from: string;
  to: string;
}

interface GraphModelSnapshot {
  nodes: GraphModelNode[];
  edges: GraphModelEdge[];
}

interface UpdateGraphModelResponse {
  graphModel: GraphModelSnapshot;
}

@Component({
  selector: 'app-root',
  imports: [CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  public graphModel: GraphModelSnapshot | null = null;
  public isLoading = false;
  public errorMessage = '';

  private readonly endpointUrl: string;
  private readonly groupsDefinitionFolder = '../u/';

  constructor(
    private readonly httpClient: HttpClient,
    @Inject(BACKEND_BASE_URL) backendBaseUrl: string
  ) {
    this.endpointUrl = `${backendBaseUrl}/v1/updateGraph`;
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
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo actualizar el grafo';
        this.isLoading = false;
      }
    });
  }
}
