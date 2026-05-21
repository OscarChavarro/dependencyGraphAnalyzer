import { GraphModel } from '../model/graph-model';
import { Html5CanvasGraphRenderer } from '../render/Html5CanvasGraphRenderer';

export class SelectionInteractionTechnique {
  public constructor(
    private readonly graphModel: GraphModel,
    private readonly renderer: Html5CanvasGraphRenderer,
    private readonly isStructureGraphProvider: () => boolean,
    private readonly openGraphByFilename: (filename: string) => void,
    private readonly onSelectionChanged: (selectedNodes: string[]) => void,
    private readonly returnToStructure: () => void
  ) {}

  public attach(canvas: HTMLCanvasElement): void {
    canvas.addEventListener('mousemove', this.onMouseMove);
    canvas.addEventListener('click', this.onMouseClick);
    canvas.addEventListener('dblclick', this.onDoubleClick);
    canvas.addEventListener('mouseleave', this.onMouseLeave);
  }

  private readonly onMouseMove = (event: MouseEvent): void => {
    const hoveredNodeName = this.pickNodeFromEvent(event);
    this.renderer.setHoveredNode(hoveredNodeName);
  };

  private readonly onMouseClick = (event: MouseEvent): void => {
    const hoveredNodeName = this.pickNodeFromEvent(event);

    if (!hoveredNodeName) {
      if (!event.ctrlKey && !event.metaKey) {
        this.graphModel.clearSelection();
        this.renderer.setSelectedNodes(this.graphModel.selectedNodes);
        this.onSelectionChanged([...this.graphModel.selectedNodes]);
      }
      return;
    }

    if (event.ctrlKey || event.metaKey) {
      this.graphModel.toggleMultiSelection(hoveredNodeName);
    } else {
      this.graphModel.selectSingle(hoveredNodeName);
    }

    this.renderer.setSelectedNodes(this.graphModel.selectedNodes);
    this.renderer.setHoveredNode(hoveredNodeName);
    this.onSelectionChanged([...this.graphModel.selectedNodes]);
  };

  private readonly onMouseLeave = (): void => {
    this.renderer.setHoveredNode(null);
  };

  private readonly onDoubleClick = (event: MouseEvent): void => {
    if (!this.isStructureGraphProvider()) {
      const rectangularNodeName = this.renderer.pickRectangularNodeNameFromEvent(event);
      if (rectangularNodeName) {
        this.returnToStructure();
      }
      return;
    }
    const hoveredNodeName = this.pickNodeFromEvent(event);
    const filename = this.mapNodeNameToSvgFilename(hoveredNodeName);
    if (!filename || filename === 'structure.svg') {
      return;
    }
    this.openGraphByFilename(filename);
  };

  private pickNodeFromEvent(event: MouseEvent): string | null {
    return this.renderer.pickNodeNameFromEvent(event);
  }

  private mapNodeNameToSvgFilename(nodeName: string | null): string | null {
    if (!nodeName) {
      return null;
    }
    const trimmed = nodeName.trim();
    if (trimmed === '[STRUCTURE]' || trimmed === '_[STRUCTURE]') {
      return 'structure.svg';
    }
    const match = trimmed.match(/^_\[(.+)\]$/);
    if (!match?.[1]) {
      return null;
    }
    return `${match[1].toLowerCase()}.svg`;
  }
}
