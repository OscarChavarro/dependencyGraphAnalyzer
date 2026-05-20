import { GraphModelEdge, GraphModelNode, GraphModelSnapshot } from '../model/graph-model';

export class GroupRelationshipQuery {
  public query(snapshot: GraphModelSnapshot, selectedGroupA: string, selectedGroupB: string): string {
    const groupA = this.extractGroupToken(selectedGroupA);
    const groupB = this.extractGroupToken(selectedGroupB);
    if (!groupA || !groupB) {
      return `No se pudo resolver grupo desde selección: ${selectedGroupA} | ${selectedGroupB}`;
    }

    const enrichedEdges = snapshot.enrichedEdges ?? [];
    if (enrichedEdges.length > 0) {
      return this.queryFromEnrichedEdges(enrichedEdges, groupA, groupB);
    }

    const membersA = this.listGroupMembers(snapshot, groupA);
    const membersB = this.listGroupMembers(snapshot, groupB);
    if (membersA.size === 0 || membersB.size === 0) {
      return `No se encontraron nodos para ${groupA} o ${groupB}.`;
    }

    const relations = this.findCrossRelations(snapshot, membersA, membersB);
    if (relations.length === 0) {
      return `Sin relaciones entre ${groupA} y ${groupB}.`;
    }
    return relations
      .map((edge) => {
        const sourceInA = membersA.has(edge.s);
        const sourceGroup = sourceInA ? groupA : groupB;
        const targetGroup = sourceInA ? groupB : groupA;
        return `${sourceGroup}.${edge.s} -> ${targetGroup}.${edge.t}`;
      })
      .sort((left, right) => left.localeCompare(right))
      .join('\n');
  }

  public queryFromEnrichedEdges(edges: GraphModelEdge[], selectedGroupA: string, selectedGroupB: string): string {
    const groupA = this.extractGroupToken(selectedGroupA);
    const groupB = this.extractGroupToken(selectedGroupB);
    if (!groupA || !groupB) {
      return `No se pudo resolver grupo desde selección: ${selectedGroupA} | ${selectedGroupB}`;
    }

    const prefixA = `${groupA}.`;
    const prefixB = `${groupB}.`;
    const relations = edges
      .filter((edge) => {
        const aToB = edge.s.startsWith(prefixA) && edge.t.startsWith(prefixB);
        const bToA = edge.s.startsWith(prefixB) && edge.t.startsWith(prefixA);
        return aToB || bToA;
      })
      .map((edge) => `${edge.s} -> ${edge.t}`)
      .sort((left, right) => left.localeCompare(right));

    if (relations.length === 0) {
      return `Sin relaciones entre ${groupA} y ${groupB}.`;
    }
    return relations.join('\n');
  }

  public queryFromEdgeLines(lines: string[], selectedGroupA: string, selectedGroupB: string): string {
    const groupA = this.extractGroupToken(selectedGroupA);
    const groupB = this.extractGroupToken(selectedGroupB);
    if (!groupA || !groupB) {
      return `No se pudo resolver grupo desde selección: ${selectedGroupA} | ${selectedGroupB}`;
    }

    const edges = lines
      .map((line) => this.parseEdgeLine(line))
      .filter((edge): edge is GraphModelEdge => edge !== null);
    if (edges.length === 0) {
      return 'No se encontraron relaciones parseables en archivo e.';
    }

    const membersA = this.listGroupMembersFromEdges(edges, groupA);
    const membersB = this.listGroupMembersFromEdges(edges, groupB);
    if (membersA.size === 0 || membersB.size === 0) {
      return `No se encontraron nodos para ${groupA} o ${groupB}.`;
    }

    const relations = this.findCrossRelationsFromEdges(edges, membersA, membersB);
    if (relations.length === 0) {
      return `Sin relaciones entre ${groupA} y ${groupB}.`;
    }
    return relations
      .map((edge) => `${edge.s} -> ${edge.t}`)
      .sort((left, right) => left.localeCompare(right))
      .join('\n');
  }

  private extractGroupToken(nodeName: string | undefined): string | null {
    if (!nodeName) {
      return null;
    }
    const trimmed = nodeName.trim().replace(/^_/, '');
    const bracketOnly = trimmed.match(/^(\[[^\]]+\])$/);
    if (bracketOnly?.[1]) {
      return bracketOnly[1];
    }
    const prefix = trimmed.match(/^(\[[^\]]+\])\./);
    return prefix?.[1] ?? null;
  }

  private listGroupMembers(snapshot: GraphModelSnapshot, groupToken: string): Set<string> {
    const lowerPrefix = `${groupToken}.`.toLowerCase();
    const groupMarker = groupToken.toLowerCase();
    const underscoredGroupMarker = `_${groupToken}`.toLowerCase();
    const result = new Set<string>();

    // Case 1: nodes already encoded like [GROUP].package
    for (const node of this.allNodes(snapshot)) {
      const name = node.name ?? '';
      if (name.toLowerCase().startsWith(lowerPrefix)) {
        result.add(name.substring(groupToken.length + 1));
      }
    }

    // Case 2: structure edges encode membership as [GROUP] -> package
    for (const edge of this.allEdges(snapshot)) {
      const source = edge.s.toLowerCase();
      const target = edge.t.toLowerCase();
      if (source === groupMarker || source === underscoredGroupMarker) {
        if (!this.isGroupLikeName(edge.t)) {
          result.add(edge.t);
        }
      }
      if (target === groupMarker || target === underscoredGroupMarker) {
        if (!this.isGroupLikeName(edge.s)) {
          result.add(edge.s);
        }
      }
    }

    return result;
  }

  private findCrossRelations(snapshot: GraphModelSnapshot, membersA: Set<string>, membersB: Set<string>): GraphModelEdge[] {
    return this.findCrossRelationsFromEdges(this.allEdges(snapshot), membersA, membersB);
  }

  private allNodes(snapshot: GraphModelSnapshot): GraphModelNode[] {
    return [...snapshot.nodes, ...snapshot.structure.nodes];
  }

  private allEdges(snapshot: GraphModelSnapshot): GraphModelEdge[] {
    return [...snapshot.edges, ...snapshot.structure.edges];
  }

  private parseEdgeLine(line: string): GraphModelEdge | null {
    const trimmed = line.trim();
    if (!trimmed || trimmed.startsWith('#')) {
      return null;
    }
    const parts = trimmed.split('->');
    if (parts.length !== 2) {
      return null;
    }
    const s = parts[0].trim();
    const t = parts[1].trim();
    if (!s || !t) {
      return null;
    }
    return { s, t };
  }

  private listGroupMembersFromEdges(edges: GraphModelEdge[], groupToken: string): Set<string> {
    const lowerPrefix = `${groupToken}.`.toLowerCase();
    const result = new Set<string>();
    for (const edge of edges) {
      if (edge.s.toLowerCase().startsWith(lowerPrefix)) {
        result.add(edge.s);
      }
      if (edge.t.toLowerCase().startsWith(lowerPrefix)) {
        result.add(edge.t);
      }
    }
    return result;
  }

  private findCrossRelationsFromEdges(edges: GraphModelEdge[], membersA: Set<string>, membersB: Set<string>): GraphModelEdge[] {
    const result = new Map<string, GraphModelEdge>();
    for (const edge of edges) {
      const aToB = membersA.has(edge.s) && membersB.has(edge.t);
      const bToA = membersB.has(edge.s) && membersA.has(edge.t);
      if (aToB || bToA) {
        result.set(`${edge.s} -> ${edge.t}`, edge);
      }
    }
    return [...result.values()];
  }

  private isGroupLikeName(value: string): boolean {
    const trimmed = value.trim().replace(/^_/, '');
    return /^\[[^\]]+\]$/.test(trimmed);
  }
}
