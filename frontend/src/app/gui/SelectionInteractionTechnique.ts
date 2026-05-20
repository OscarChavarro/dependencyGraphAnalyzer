import { GraphModel } from '../model/graph-model';
import { Html5CanvasGraphRenderer } from '../render/Html5CanvasGraphRenderer';

export class SelectionInteractionTechnique {
  public constructor(
    private readonly graphModel: GraphModel,
    private readonly renderer: Html5CanvasGraphRenderer
  ) {}

  public attach(canvas: HTMLCanvasElement): void {
    canvas.addEventListener('mousemove', this.onMouseMove);
    canvas.addEventListener('click', this.onMouseClick);
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
  };

  private readonly onMouseLeave = (): void => {
    this.renderer.setHoveredNode(null);
  };

  private pickNodeFromEvent(event: MouseEvent): string | null {
    return this.renderer.pickNodeNameFromEvent(event);
  }
}
