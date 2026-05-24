import { Html5CanvasGraphRenderer } from '../render/Html5CanvasGraphRenderer';

export interface WorkingAreaScrollsViewModel {
  horizontalThumbLeftPercent: number;
  horizontalThumbWidthPercent: number;
  verticalThumbTopPercent: number;
  verticalThumbHeightPercent: number;
}

export class WorkingAreaScrollsControl {
  private static readonly MIN_VISIBLE_SCENE_AREA_RATIO = 0.05;
  private detachRenderListener: (() => void) | null = null;

  public constructor(
    private readonly renderer: Html5CanvasGraphRenderer,
    private readonly onScrollbarsUpdated: (state: WorkingAreaScrollsViewModel) => void
  ) {}

  public attach(): void {
    this.detach();
    this.detachRenderListener = this.renderer.onRender(() => {
      this.onScrollbarsUpdated(this.computeState());
    });
    this.onScrollbarsUpdated(this.computeState());
  }

  public detach(): void {
    this.detachRenderListener?.();
    this.detachRenderListener = null;
  }

  private computeState(): WorkingAreaScrollsViewModel {
    const snapshot = this.renderer.getViewportSnapshot();
    if (!snapshot || !snapshot.sceneBounds || snapshot.cameraScale <= 0) {
      return {
        horizontalThumbLeftPercent: 0,
        horizontalThumbWidthPercent: 100,
        verticalThumbTopPercent: 0,
        verticalThumbHeightPercent: 100
      };
    }

    const sceneWidth = Math.max(1e-6, snapshot.sceneBounds.maxX - snapshot.sceneBounds.minX);
    const sceneHeight = Math.max(1e-6, snapshot.sceneBounds.maxY - snapshot.sceneBounds.minY);
    const visibleWorldWidth = snapshot.canvasWidth / snapshot.cameraScale;
    const visibleWorldHeight = snapshot.canvasHeight / snapshot.cameraScale;
    const visibleWorldMinX = (0 - snapshot.cameraOffsetX) / snapshot.cameraScale;
    const visibleWorldMinY = (0 - snapshot.cameraOffsetY) / snapshot.cameraScale;

    const horizontal = this.computeAxis(visibleWorldMinX, visibleWorldWidth, snapshot.sceneBounds.minX, sceneWidth);
    const vertical = this.computeAxis(visibleWorldMinY, visibleWorldHeight, snapshot.sceneBounds.minY, sceneHeight);

    return {
      horizontalThumbLeftPercent: horizontal.positionPercent,
      horizontalThumbWidthPercent: horizontal.sizePercent,
      verticalThumbTopPercent: vertical.positionPercent,
      verticalThumbHeightPercent: vertical.sizePercent
    };
  }

  private computeAxis(
    visibleStart: number,
    visibleSize: number,
    sceneStart: number,
    sceneSize: number
  ): { positionPercent: number; sizePercent: number } {
    if (visibleSize >= sceneSize || sceneSize <= 0 || visibleSize <= 0) {
      return { positionPercent: 0, sizePercent: 100 };
    }

    const minVisibleWorld = this.computeMinimumVisibleWorldSpan(sceneSize, visibleSize);
    const minVisibleStart = sceneStart - visibleSize + minVisibleWorld;
    const maxVisibleStart = sceneStart + sceneSize - minVisibleWorld;
    const travelRange = Math.max(1e-6, maxVisibleStart - minVisibleStart);
    const normalizedPosition = (visibleStart - minVisibleStart) / travelRange;
    const sizePercent = this.clamp((visibleSize / sceneSize) * 100, 0, 100);
    const maxThumbStartPercent = Math.max(0, 100 - sizePercent);
    const positionPercent = this.clamp(normalizedPosition * maxThumbStartPercent, 0, maxThumbStartPercent);

    return {
      positionPercent,
      sizePercent
    };
  }

  private computeMinimumVisibleWorldSpan(sceneSize: number, visibleSize: number): number {
    const minVisibleEdgeRatio = Math.sqrt(WorkingAreaScrollsControl.MIN_VISIBLE_SCENE_AREA_RATIO);
    return Math.min(sceneSize, visibleSize, sceneSize * minVisibleEdgeRatio);
  }

  private clamp(value: number, min: number, max: number): number {
    return Math.min(max, Math.max(min, value));
  }
}
