import { GraphModelEdge, GraphModelNode, GraphModelSnapshot } from '../model/graph-model';

export class FloodingOperations {
  private static readonly DEFAULT_FILL_OVERRIDE = '#ffffff';
  private static readonly SELECTED_FILL_OVERRIDE = '#80ff80';

  public inundateDependencies(
    snapshot: GraphModelSnapshot,
    selectedNodeNames: string[],
    currentSvgNodeNames: string[],
    currentGroupToken: string | null
  ): FloodingResult {
    return this.applyFlooding(snapshot, selectedNodeNames, currentSvgNodeNames, currentGroupToken, 'dependencies');
  }

  public inundateClients(
    snapshot: GraphModelSnapshot,
    selectedNodeNames: string[],
    currentSvgNodeNames: string[],
    currentGroupToken: string | null
  ): FloodingResult {
    return this.applyFlooding(snapshot, selectedNodeNames, currentSvgNodeNames, currentGroupToken, 'clients');
  }

  private applyFlooding(
    snapshot: GraphModelSnapshot,
    selectedNodeNames: string[],
    currentSvgNodeNames: string[],
    currentGroupToken: string | null,
    mode: FloodingMode
  ): FloodingResult {
    const selectedNodes = this.toNameSet(selectedNodeNames);
    const traversalEdges = snapshot.enrichedEdges?.length ? snapshot.enrichedEdges : snapshot.edges;
    const traversalSeeds = this.toTraversalSeeds(selectedNodes, currentGroupToken);
    const highlightedTraversalNodes = this.findReachableNodes(traversalEdges, traversalSeeds, mode);
    const highlightedLocalNodes = this.toLocalNodeNames(highlightedTraversalNodes);
    this.includeContainerGroups(highlightedTraversalNodes, highlightedLocalNodes);
    const applyNodeOverride = (node: GraphModelNode): GraphModelNode => ({
      ...node,
      fillColorOverride: highlightedLocalNodes.has(this.normalizeName(node.name))
        ? FloodingOperations.SELECTED_FILL_OVERRIDE
        : FloodingOperations.DEFAULT_FILL_OVERRIDE
    });

    const floodedSnapshot: GraphModelSnapshot = {
      ...snapshot,
      nodes: snapshot.nodes.map(applyNodeOverride),
      structure: {
        ...snapshot.structure,
        nodes: snapshot.structure.nodes.map(applyNodeOverride)
      }
    };

    const currentSvgSet = this.toNameSet(currentSvgNodeNames);
    const additionalSelectedNodes = [...highlightedLocalNodes]
      .filter((nodeName) => currentSvgSet.has(nodeName))
      .map((nodeName) => this.findDisplayNodeName(nodeName, currentSvgNodeNames));
    return { floodedSnapshot, additionalSelectedNodes };
  }

  private findReachableNodes(edges: GraphModelEdge[], seeds: Set<string>, mode: FloodingMode): Set<string> {
    const adjacency = this.buildAdjacency(edges, mode);
    const visited = new Set<string>();
    const stack = [...seeds];
    while (stack.length > 0) {
      const current = stack.pop();
      if (!current || visited.has(current)) {
        continue;
      }
      visited.add(current);
      const neighbors = adjacency.get(current);
      if (!neighbors) {
        continue;
      }
      for (const next of neighbors) {
        if (!visited.has(next)) {
          stack.push(next);
        }
      }
    }
    return visited;
  }

  private buildAdjacency(edges: GraphModelEdge[], mode: FloodingMode): Map<string, string[]> {
    const adjacency = new Map<string, string[]>();
    for (const edge of edges) {
      const from = this.normalizeName(mode === 'dependencies' ? edge.s : edge.t);
      const to = this.normalizeName(mode === 'dependencies' ? edge.t : edge.s);
      if (!from || !to) {
        continue;
      }
      if (!adjacency.has(from)) {
        adjacency.set(from, []);
      }
      adjacency.get(from)?.push(to);
    }
    return adjacency;
  }

  private toNameSet(names: string[]): Set<string> {
    return new Set(
      names
        .map((name) => this.normalizeName(name))
        .filter((name) => name.length > 0)
    );
  }

  private toTraversalSeeds(selectedNodes: Set<string>, currentGroupToken: string | null): Set<string> {
    const result = new Set<string>();
    for (const nodeName of selectedNodes) {
      if (this.isQualifiedNodeName(nodeName)) {
        result.add(this.normalizeName(nodeName));
        continue;
      }
      if (currentGroupToken) {
        result.add(this.normalizeName(`${currentGroupToken}.${nodeName}`));
      } else {
        result.add(this.normalizeName(nodeName));
      }
    }
    return result;
  }

  private toLocalNodeNames(nodes: Set<string>): Set<string> {
    const result = new Set<string>();
    for (const nodeName of nodes) {
      result.add(this.normalizeName(this.extractLocalNodeName(nodeName)));
    }
    return result;
  }

  private extractLocalNodeName(nodeName: string): string {
    const match = nodeName.match(/^\[[^\]]+\]\.(.+)$/);
    return match?.[1] ?? nodeName;
  }

  private isQualifiedNodeName(nodeName: string): boolean {
    return /^\[[^\]]+\]\./.test(nodeName);
  }

  private normalizeName(value: string): string {
    return value.trim().toLowerCase();
  }

  private findDisplayNodeName(normalizedName: string, availableNames: string[]): string {
    return availableNames.find((name) => this.normalizeName(name) === normalizedName) ?? normalizedName;
  }

  private includeContainerGroups(highlightedTraversalNodes: Set<string>, highlightedLocalNodes: Set<string>): void {
    const groups = new Set<string>();
    for (const nodeName of highlightedTraversalNodes) {
      const groupToken = this.extractGroupTokenFromQualified(nodeName);
      if (groupToken) {
        groups.add(groupToken);
      }
    }
    for (const groupToken of groups) {
      highlightedLocalNodes.add(this.normalizeName(groupToken));
      highlightedLocalNodes.add(this.normalizeName(`_${groupToken}`));
    }
  }

  private extractGroupTokenFromQualified(nodeName: string): string | null {
    const match = nodeName.match(/^(\[[^\]]+\])\..+$/);
    return match?.[1] ?? null;
  }
}

type FloodingMode = 'dependencies' | 'clients';

export interface FloodingResult {
  floodedSnapshot: GraphModelSnapshot;
  additionalSelectedNodes: string[];
}
