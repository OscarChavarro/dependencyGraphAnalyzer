import { Html5CanvasGraphRenderer } from '../render/Html5CanvasGraphRenderer';

export class DrawingAreaNavigationInteractionTechnique {
  private isDragging = false;
  private dragLastX = 0;
  private dragLastY = 0;
  private scrollContainer: HTMLElement | null = null;
  private readonly inputPlatform: 'mac' | 'linux' | 'other';

  public constructor(private readonly renderer: Html5CanvasGraphRenderer) {
    this.inputPlatform = this.detectInputPlatform();
  }

  public attach(canvas: HTMLCanvasElement, scrollContainer: HTMLElement | null | undefined): void {
    this.scrollContainer = scrollContainer ?? null;
    canvas.addEventListener('wheel', this.onWheel, { passive: false });
    canvas.addEventListener('mousedown', this.onMouseDown);
    window.addEventListener('mouseup', this.onMouseUp);
    window.addEventListener('mousemove', this.onMouseMove);
    window.addEventListener('keydown', this.onKeyDown);
  }

  private readonly onWheel = (event: WheelEvent): void => {
    event.preventDefault();

    if (this.inputPlatform === 'linux') {
      const point = this.renderer.canvasPointFromEvent(event);
      this.renderer.zoomAt(point.x, point.y, event.deltaY);
      if (event.deltaY > 0) {
        this.moveScrollbarsTowardCenter();
      }
      return;
    }

    if (event.ctrlKey) {
      // Trackpad pinch gesture (macOS browsers): use as zoom.
      const point = this.renderer.canvasPointFromEvent(event);
      this.renderer.zoomAt(point.x, point.y, event.deltaY);
      if (event.deltaY > 0) {
        this.moveScrollbarsTowardCenter();
      }
      return;
    }

    // Two-finger scroll gesture: pan viewport using both axes.
    const delta = this.renderer.canvasDeltaFromCss(-event.deltaX, -event.deltaY);
    this.renderer.panBy(delta.x, delta.y);
  };

  private readonly onMouseDown = (event: MouseEvent): void => {
    this.isDragging = true;
    const point = this.renderer.canvasPointFromEvent(event);
    this.dragLastX = point.x;
    this.dragLastY = point.y;
  };

  private readonly onMouseUp = (): void => {
    this.isDragging = false;
  };

  private readonly onMouseMove = (event: MouseEvent): void => {
    if (!this.isDragging) {
      return;
    }
    const point = this.renderer.canvasPointFromEvent(event);
    const dx = point.x - this.dragLastX;
    const dy = point.y - this.dragLastY;
    this.dragLastX = point.x;
    this.dragLastY = point.y;
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

  private moveScrollbarsTowardCenter(): void {
    if (!this.scrollContainer) {
      return;
    }

    const progress = this.renderer.getZoomOutCenteringProgress();
    if (progress <= 0) {
      return;
    }

    const maxScrollLeft = this.scrollContainer.scrollWidth - this.scrollContainer.clientWidth;
    const maxScrollTop = this.scrollContainer.scrollHeight - this.scrollContainer.clientHeight;
    const targetLeft = maxScrollLeft * 0.5;
    const targetTop = maxScrollTop * 0.5;
    const damping = Math.min(0.45, progress * 0.45);

    this.scrollContainer.scrollLeft += (targetLeft - this.scrollContainer.scrollLeft) * damping;
    this.scrollContainer.scrollTop += (targetTop - this.scrollContainer.scrollTop) * damping;
  }

  private detectInputPlatform(): 'mac' | 'linux' | 'other' {
    const uaDataPlatform = (navigator as Navigator & { userAgentData?: { platform?: string } }).userAgentData?.platform ?? '';
    const platform = `${uaDataPlatform} ${navigator.platform} ${navigator.userAgent}`.toLowerCase();
    if (platform.includes('mac')) {
      return 'mac';
    }
    if (platform.includes('linux')) {
      return 'linux';
    }
    return 'other';
  }
}
