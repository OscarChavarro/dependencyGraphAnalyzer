export class InvalidRelationshipDetector {
  public isInvalid(relationLine: string): boolean {
    const parts = relationLine.split('->');
    if (parts.length !== 2) {
      return false;
    }
    const sourceGroupOrder = this.extractGroupOrder(parts[0]);
    const targetGroupOrder = this.extractGroupOrder(parts[1]);
    if (sourceGroupOrder == null || targetGroupOrder == null) {
      return false;
    }
    return sourceGroupOrder < targetGroupOrder;
  }

  private extractGroupOrder(endpoint: string): number | null {
    const match = endpoint.trim().match(/^\[(\d+)_/);
    if (!match?.[1]) {
      return null;
    }
    return Number.parseInt(match[1], 10);
  }
}
