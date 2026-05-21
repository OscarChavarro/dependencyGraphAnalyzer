import { GraphModelEdge, GraphModelNode, GraphModelSnapshot } from '../model/graph-model';

type GroupMemberIndex = {
  byExactLower: Map<string, string>;
  byBaseLower: Map<string, string[]>;
};

export class GroupRelationshipQuery {
  public query(snapshot: GraphModelSnapshot, selectedGroupA: string, selectedGroupB: string): string {
    const groupA = this.extractGroupToken(selectedGroupA);
    const groupB = this.extractGroupToken(selectedGroupB);
    if (!groupA || !groupB) {
      return `No se pudo resolver grupo desde selección: ${selectedGroupA} | ${selectedGroupB}`;
    }

    const enrichedEdges = snapshot.enrichedEdges ?? [];
    if (enrichedEdges.length > 0) {
      const enrichedRelations = this.findEnrichedRelations(enrichedEdges, groupA, groupB);
      if (enrichedRelations.length > 0) {
        return enrichedRelations.join('\n');
      }
    }

    const membersA = this.listGroupMembers(snapshot, groupA);
    const membersB = this.listGroupMembers(snapshot, groupB);
    if (membersA.size === 0 || membersB.size === 0) {
      return `No se encontraron nodos para ${groupA} o ${groupB}.`;
    }

    const indexA = this.buildMemberIndex(membersA);
    const indexB = this.buildMemberIndex(membersB);

    const crossRelations = this.findCrossRelations(snapshot, indexA, indexB);
    if (crossRelations.length > 0) {
      return crossRelations
        .map((edge) => {
          const sourceInA = this.belongsToGroup(edge.s, indexA);
          const sourceGroup = sourceInA ? groupA : groupB;
          const targetGroup = sourceInA ? groupB : groupA;
          const sourceIndex = sourceInA ? indexA : indexB;
          const targetIndex = sourceInA ? indexB : indexA;
          const sourcePackage = this.resolvePreferredMemberToken(edge.s, sourceIndex);
          const targetPackage = this.resolvePreferredMemberToken(edge.t, targetIndex);
          return `${sourceGroup}.${sourcePackage} -> ${targetGroup}.${targetPackage}`;
        })
        .sort((left, right) => left.localeCompare(right))
        .join('\n');
    }

    const overlapRelations = this.findOverlapRelations(indexA, indexB, groupA, groupB);
    if (overlapRelations.length > 0) {
      return overlapRelations.join('\n');
    }

    return `Sin relaciones entre ${groupA} y ${groupB}.`;
  }

  public queryFromEnrichedEdges(edges: GraphModelEdge[], selectedGroupA: string, selectedGroupB: string): string {
    const groupA = this.extractGroupToken(selectedGroupA);
    const groupB = this.extractGroupToken(selectedGroupB);
    if (!groupA || !groupB) {
      return `No se pudo resolver grupo desde selección: ${selectedGroupA} | ${selectedGroupB}`;
    }

    const relations = this.findEnrichedRelations(edges, groupA, groupB);
    if (relations.length === 0) {
      return `Sin relaciones entre ${groupA} y ${groupB}.`;
    }
    return relations.join('\n');
  }

  private findEnrichedRelations(edges: GraphModelEdge[], groupA: string, groupB: string): string[] {
    const canonicalA = this.canonicalizeGroupToken(groupA);
    const canonicalB = this.canonicalizeGroupToken(groupB);

    return edges
      .filter((edge) => {
        const sourceGroup = this.extractGroupToken(edge.s);
        const targetGroup = this.extractGroupToken(edge.t);
        if (!sourceGroup || !targetGroup) {
          return false;
        }
        const sourceCanonical = this.canonicalizeGroupToken(sourceGroup);
        const targetCanonical = this.canonicalizeGroupToken(targetGroup);
        const aToB = sourceCanonical === canonicalA && targetCanonical === canonicalB;
        const bToA = sourceCanonical === canonicalB && targetCanonical === canonicalA;
        return aToB || bToA;
      })
      .map((edge) => `${edge.s} -> ${edge.t}`)
      .sort((left, right) => left.localeCompare(right));
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

    for (const node of this.allNodes(snapshot)) {
      const name = node.name ?? '';
      if (name.toLowerCase().startsWith(lowerPrefix)) {
        result.add(name.substring(groupToken.length + 1));
      }
    }

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

  private buildMemberIndex(members: Set<string>): GroupMemberIndex {
    const byExactLower = new Map<string, string>();
    const byBaseLower = new Map<string, string[]>();

    for (const member of members) {
      const normalized = member.trim();
      const exactKey = normalized.toLowerCase();
      if (!byExactLower.has(exactKey)) {
        byExactLower.set(exactKey, normalized);
      }

      const baseKey = this.extractBaseName(normalized).toLowerCase();
      const list = byBaseLower.get(baseKey) ?? [];
      if (!list.includes(normalized)) {
        list.push(normalized);
      }
      byBaseLower.set(baseKey, list);
    }

    return { byExactLower, byBaseLower };
  }

  private findCrossRelations(snapshot: GraphModelSnapshot, indexA: GroupMemberIndex, indexB: GroupMemberIndex): GraphModelEdge[] {
    const result = new Map<string, GraphModelEdge>();

    for (const edge of this.allEdges(snapshot)) {
      const sourceInA = this.belongsToGroup(edge.s, indexA);
      const targetInA = this.belongsToGroup(edge.t, indexA);
      const sourceInB = this.belongsToGroup(edge.s, indexB);
      const targetInB = this.belongsToGroup(edge.t, indexB);
      const aToB = sourceInA && targetInB;
      const bToA = sourceInB && targetInA;
      if (aToB || bToA) {
        result.set(`${edge.s} -> ${edge.t}`, edge);
      }
    }

    return [...result.values()];
  }

  private findOverlapRelations(indexA: GroupMemberIndex, indexB: GroupMemberIndex, groupA: string, groupB: string): string[] {
    const relations = new Set<string>();

    for (const [baseKey, membersInA] of indexA.byBaseLower) {
      const membersInB = indexB.byBaseLower.get(baseKey);
      if (!membersInB || membersInB.length === 0) {
        continue;
      }
      for (const memberA of membersInA) {
        for (const memberB of membersInB) {
          relations.add(`${groupA}.${memberA} -> ${groupB}.${memberB}`);
        }
      }
    }

    return [...relations].sort((left, right) => left.localeCompare(right));
  }

  private belongsToGroup(endpoint: string, index: GroupMemberIndex): boolean {
    const exactKey = endpoint.trim().toLowerCase();
    if (index.byExactLower.has(exactKey)) {
      return true;
    }
    const baseKey = this.extractBaseName(endpoint).toLowerCase();
    return (index.byBaseLower.get(baseKey)?.length ?? 0) > 0;
  }

  private resolvePreferredMemberToken(endpoint: string, index: GroupMemberIndex): string {
    const exactKey = endpoint.trim().toLowerCase();
    const exact = index.byExactLower.get(exactKey);
    if (exact) {
      return exact;
    }

    const baseKey = this.extractBaseName(endpoint).toLowerCase();
    const byBase = index.byBaseLower.get(baseKey);
    if (byBase && byBase.length > 0) {
      return byBase[0];
    }

    return endpoint.trim();
  }

  private extractBaseName(value: string): string {
    const trimmed = value.trim();
    const slash = trimmed.lastIndexOf('/');
    return slash >= 0 ? trimmed.substring(slash + 1) : trimmed;
  }

  private allNodes(snapshot: GraphModelSnapshot): GraphModelNode[] {
    return [...snapshot.nodes, ...snapshot.structure.nodes];
  }

  private allEdges(snapshot: GraphModelSnapshot): GraphModelEdge[] {
    return [...snapshot.edges, ...snapshot.structure.edges];
  }

  private canonicalizeGroupToken(groupToken: string): string {
    return groupToken.replace(/[^a-zA-Z0-9]/g, '').toLowerCase();
  }

  private isGroupLikeName(value: string): boolean {
    const trimmed = value.trim().replace(/^_/, '');
    return /^\[[^\]]+\]$/.test(trimmed);
  }
}
