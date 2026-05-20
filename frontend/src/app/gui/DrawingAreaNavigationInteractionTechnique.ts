import { Html5CanvasGraphRenderer } from '../render/Html5CanvasGraphRenderer';

export class DrawingAreaNavigationInteractionTechnique {
  private isDragging = false;
  private dragLastX = 0;
  private dragLastY = 0;

  public constructor(private readonly renderer: Html5CanvasGraphRenderer) {}

  public attach(canvas: HTMLCanvasElement): void {
    canvas.addEventListener('wheel', this.onWheel, { passive: false });
    canvas.addEventListener('mousedown', this.onMouseDown);
    window.addEventListener('mouseup', this.onMouseUp);
    window.addEventListener('mousemove', this.onMouseMove);
    window.addEventListener('keydown', this.onKeyDown);
  }

  private readonly onWheel = (event: WheelEvent): void => {
    event.preventDefault();

    if (event.ctrlKey) {
      // Trackpad pinch gesture (macOS browsers): use as zoom.
      this.renderer.zoomAt(event.offsetX, event.offsetY, event.deltaY);
      return;
    }

    // Two-finger scroll gesture: pan viewport using both axes.
    this.renderer.panBy(-event.deltaX, -event.deltaY);
  };

  private readonly onMouseDown = (event: MouseEvent): void => {
    this.isDragging = true;
    this.dragLastX = event.clientX;
    this.dragLastY = event.clientY;
  };

  private readonly onMouseUp = (): void => {
    this.isDragging = false;
  };

  private readonly onMouseMove = (event: MouseEvent): void => {
    if (!this.isDragging) {
      return;
    }
    const dx = event.clientX - this.dragLastX;
    const dy = event.clientY - this.dragLastY;
    this.dragLastX = event.clientX;
    this.dragLastY = event.clientY;
    this.renderer.panBy(dx, dy);
  };

  private readonly onKeyDown = (event: KeyboardEvent): void => {
    if (event.key !== '1') {
      return;
    }

    const target = event.target as HTMLElement | null;
    if (target && (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA' || target.isContentEditable)) {
      return;
    }

    event.preventDefault();
    this.renderer.moveAndCenterToFit();
  };
}
