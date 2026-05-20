export type GraphModelGenerator = 'CACHE_LOADER' | 'DEBIAN_PACKAGE_GENERATOR';

export interface UpdateGraphModelRequest {
  generator: GraphModelGenerator;
  groupsDefinitionFolder: string;
}

export interface GraphModelNode {
  name: string;
  isBad?: boolean;
  marked?: boolean;
  group?: boolean;
  sink?: boolean;
  s?: boolean;
  variant?: boolean;
  boldName?: boolean;
}

export interface GraphModelEdge {
  s: string;
  t: string;
}

export interface GraphModelStructure {
  nodes: GraphModelNode[];
  edges: GraphModelEdge[];
}

export interface GraphModelSnapshot {
  nodes: GraphModelNode[];
  edges: GraphModelEdge[];
  structure: GraphModelStructure;
}

export interface UpdateGraphModelResponse {
  graphModel: GraphModelSnapshot;
}

export class GraphModel {
  public selectedNodes: string[] = [];

  public clearSelection(): void {
    this.selectedNodes = [];
  }

  public selectSingle(nodeName: string): void {
    this.selectedNodes = [nodeName];
  }

  public toggleMultiSelection(nodeName: string): void {
    if (this.selectedNodes.includes(nodeName)) {
      this.selectedNodes = this.selectedNodes.filter((name) => name !== nodeName);
      return;
    }
    this.selectedNodes = [...this.selectedNodes, nodeName];
  }
}
