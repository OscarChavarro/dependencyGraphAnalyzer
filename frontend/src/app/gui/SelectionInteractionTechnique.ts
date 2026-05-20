import { GraphModel } from '../model/graph-model';
import { Html5CanvasGraphRenderer, InteractiveEllipse } from '../render/Html5CanvasGraphRenderer';

class Ellipse {
  public constructor(
    private readonly cx: number,
    private readonly cy: number,
    private readonly rx: number,
    private readonly ry: number
  ) {}

  public inside(x: number, y: number): boolean {
    if (this.rx <= 0 || this.ry <= 0) {
      return false;
    }
    const dx = (x - this.cx) / this.rx;
    const dy = (y - this.cy) / this.ry;
    return dx * dx + dy * dy <= 1;
  }
}

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
    const point = this.renderer.canvasPointFromEvent(event);
    const world = this.renderer.screenToWorld(point.x, point.y);
    const interactiveEllipses = this.renderer.getInteractiveEllipses();

    for (const ellipse of interactiveEllipses) {
      if (this.isInsideEllipse(ellipse, world.x, world.y)) {
        return ellipse.nodeName;
      }
    }

    return null;
  }

  private isInsideEllipse(ellipse: InteractiveEllipse, x: number, y: number): boolean {
    return new Ellipse(ellipse.cx, ellipse.cy, ellipse.rx, ellipse.ry).inside(x, y);
  }
}
