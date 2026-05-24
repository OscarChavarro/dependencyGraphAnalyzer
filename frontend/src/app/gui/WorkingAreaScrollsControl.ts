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
  private horizontalTrack: HTMLElement | null = null;
  private horizontalThumb: HTMLElement | null = null;
  private verticalTrack: HTMLElement | null = null;
  private verticalThumb: HTMLElement | null = null;
  private activeDragAxis: 'horizontal' | 'vertical' | null = null;
  private dragPointerId: number | null = null;
  private dragOffsetInThumbPx = 0;
  private currentViewModel: WorkingAreaScrollsViewModel = {
    horizontalThumbLeftPercent: 0,
    horizontalThumbWidthPercent: 100,
    verticalThumbTopPercent: 0,
    verticalThumbHeightPercent: 100
  };

  public constructor(
    private readonly renderer: Html5CanvasGraphRenderer,
    private readonly onScrollbarsUpdated: (state: WorkingAreaScrollsViewModel) => void
  ) {}

  public attach(
    horizontalTrack: HTMLElement | null,
    horizontalThumb: HTMLElement | null,
    verticalTrack: HTMLElement | null,
    verticalThumb: HTMLElement | null
  ): void {
    this.detach();
    this.horizontalTrack = horizontalTrack;
    this.horizontalThumb = horizontalThumb;
    this.verticalTrack = verticalTrack;
    this.verticalThumb = verticalThumb;
    this.registerDomListeners();
    this.detachRenderListener = this.renderer.onRender(() => {
      this.updateViewModel(this.computeState());
    });
    this.updateViewModel(this.computeState());
  }

  public detach(): void {
    this.unregisterDomListeners();
    this.detachRenderListener?.();
    this.detachRenderListener = null;
    this.horizontalTrack = null;
    this.horizontalThumb = null;
    this.verticalTrack = null;
    this.verticalThumb = null;
    this.activeDragAxis = null;
    this.dragPointerId = null;
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

  private updateViewModel(state: WorkingAreaScrollsViewModel): void {
    this.currentViewModel = state;
    this.onScrollbarsUpdated(state);
  }

  private registerDomListeners(): void {
    this.horizontalTrack?.addEventListener('pointerdown', this.onHorizontalTrackPointerDown);
    this.horizontalThumb?.addEventListener('pointerdown', this.onHorizontalThumbPointerDown);
    this.verticalTrack?.addEventListener('pointerdown', this.onVerticalTrackPointerDown);
    this.verticalThumb?.addEventListener('pointerdown', this.onVerticalThumbPointerDown);
    window.addEventListener('pointermove', this.onWindowPointerMove);
    window.addEventListener('pointerup', this.onWindowPointerUp);
    window.addEventListener('pointercancel', this.onWindowPointerUp);
  }

  private unregisterDomListeners(): void {
    this.horizontalTrack?.removeEventListener('pointerdown', this.onHorizontalTrackPointerDown);
    this.horizontalThumb?.removeEventListener('pointerdown', this.onHorizontalThumbPointerDown);
    this.verticalTrack?.removeEventListener('pointerdown', this.onVerticalTrackPointerDown);
    this.verticalThumb?.removeEventListener('pointerdown', this.onVerticalThumbPointerDown);
    window.removeEventListener('pointermove', this.onWindowPointerMove);
    window.removeEventListener('pointerup', this.onWindowPointerUp);
    window.removeEventListener('pointercancel', this.onWindowPointerUp);
  }

  private readonly onHorizontalTrackPointerDown = (event: PointerEvent): void => {
    if (!this.horizontalTrack) {
      return;
    }
    if (this.horizontalThumb && event.target instanceof Node && this.horizontalThumb.contains(event.target)) {
      return;
    }
    event.preventDefault();
    const trackRect = this.horizontalTrack.getBoundingClientRect();
    const thumbWidthPx = trackRect.width * (this.currentViewModel.horizontalThumbWidthPercent / 100);
    this.activeDragAxis = 'horizontal';
    this.dragPointerId = event.pointerId;
    this.dragOffsetInThumbPx = thumbWidthPx * 0.5;
    this.applyHorizontalFromPointer(event.clientX);
  };

  private readonly onHorizontalThumbPointerDown = (event: PointerEvent): void => {
    if (!this.horizontalThumb) {
      return;
    }
    event.preventDefault();
    event.stopPropagation();
    const thumbRect = this.horizontalThumb.getBoundingClientRect();
    this.activeDragAxis = 'horizontal';
    this.dragPointerId = event.pointerId;
    this.dragOffsetInThumbPx = this.clamp(event.clientX - thumbRect.left, 0, thumbRect.width);
    this.horizontalThumb.setPointerCapture(event.pointerId);
  };

  private readonly onVerticalTrackPointerDown = (event: PointerEvent): void => {
    if (!this.verticalTrack) {
      return;
    }
    if (this.verticalThumb && event.target instanceof Node && this.verticalThumb.contains(event.target)) {
      return;
    }
    event.preventDefault();
    const trackRect = this.verticalTrack.getBoundingClientRect();
    const thumbHeightPx = trackRect.height * (this.currentViewModel.verticalThumbHeightPercent / 100);
    this.activeDragAxis = 'vertical';
    this.dragPointerId = event.pointerId;
    this.dragOffsetInThumbPx = thumbHeightPx * 0.5;
    this.applyVerticalFromPointer(event.clientY);
  };

  private readonly onVerticalThumbPointerDown = (event: PointerEvent): void => {
    if (!this.verticalThumb) {
      return;
    }
    event.preventDefault();
    event.stopPropagation();
    const thumbRect = this.verticalThumb.getBoundingClientRect();
    this.activeDragAxis = 'vertical';
    this.dragPointerId = event.pointerId;
    this.dragOffsetInThumbPx = this.clamp(event.clientY - thumbRect.top, 0, thumbRect.height);
    this.verticalThumb.setPointerCapture(event.pointerId);
  };

  private readonly onWindowPointerMove = (event: PointerEvent): void => {
    if (!this.activeDragAxis || this.dragPointerId !== event.pointerId) {
      return;
    }
    event.preventDefault();
    if (this.activeDragAxis === 'horizontal') {
      this.applyHorizontalFromPointer(event.clientX);
      return;
    }
    this.applyVerticalFromPointer(event.clientY);
  };

  private readonly onWindowPointerUp = (event: PointerEvent): void => {
    if (this.dragPointerId !== event.pointerId) {
      return;
    }
    this.activeDragAxis = null;
    this.dragPointerId = null;
    this.dragOffsetInThumbPx = 0;
  };

  private applyHorizontalFromPointer(pointerClientX: number): void {
    if (!this.horizontalTrack) {
      return;
    }
    const trackRect = this.horizontalTrack.getBoundingClientRect();
    const maxThumbStartPercent = Math.max(0, 100 - this.currentViewModel.horizontalThumbWidthPercent);
    if (trackRect.width <= 0 || maxThumbStartPercent <= 0) {
      return;
    }
    const thumbStartPx = this.clamp(pointerClientX - trackRect.left - this.dragOffsetInThumbPx, 0, trackRect.width);
    const thumbStartPercent = this.clamp((thumbStartPx / trackRect.width) * 100, 0, maxThumbStartPercent);
    this.panToThumbPosition('horizontal', thumbStartPercent);
  }

  private applyVerticalFromPointer(pointerClientY: number): void {
    if (!this.verticalTrack) {
      return;
    }
    const trackRect = this.verticalTrack.getBoundingClientRect();
    const maxThumbStartPercent = Math.max(0, 100 - this.currentViewModel.verticalThumbHeightPercent);
    if (trackRect.height <= 0 || maxThumbStartPercent <= 0) {
      return;
    }
    const thumbStartPx = this.clamp(pointerClientY - trackRect.top - this.dragOffsetInThumbPx, 0, trackRect.height);
    const thumbStartPercent = this.clamp((thumbStartPx / trackRect.height) * 100, 0, maxThumbStartPercent);
    this.panToThumbPosition('vertical', thumbStartPercent);
  }

  private panToThumbPosition(axis: 'horizontal' | 'vertical', thumbStartPercent: number): void {
    const snapshot = this.renderer.getViewportSnapshot();
    if (!snapshot || !snapshot.sceneBounds || snapshot.cameraScale <= 0) {
      return;
    }

    const isHorizontal = axis === 'horizontal';
    const sceneStart = isHorizontal ? snapshot.sceneBounds.minX : snapshot.sceneBounds.minY;
    const sceneSize = isHorizontal
      ? Math.max(1e-6, snapshot.sceneBounds.maxX - snapshot.sceneBounds.minX)
      : Math.max(1e-6, snapshot.sceneBounds.maxY - snapshot.sceneBounds.minY);
    const visibleSize = isHorizontal
      ? snapshot.canvasWidth / snapshot.cameraScale
      : snapshot.canvasHeight / snapshot.cameraScale;
    const thumbSizePercent = isHorizontal
      ? this.currentViewModel.horizontalThumbWidthPercent
      : this.currentViewModel.verticalThumbHeightPercent;
    const maxThumbStartPercent = Math.max(0, 100 - thumbSizePercent);
    if (maxThumbStartPercent <= 0) {
      return;
    }

    const minVisibleWorld = this.computeMinimumVisibleWorldSpan(sceneSize, visibleSize);
    const minVisibleStart = sceneStart - visibleSize + minVisibleWorld;
    const maxVisibleStart = sceneStart + sceneSize - minVisibleWorld;
    const travelRange = Math.max(1e-6, maxVisibleStart - minVisibleStart);
    const normalized = this.clamp(thumbStartPercent / maxThumbStartPercent, 0, 1);
    const targetVisibleStart = minVisibleStart + normalized * travelRange;
    const targetOffset = -targetVisibleStart * snapshot.cameraScale;

    if (isHorizontal) {
      this.renderer.panBy(targetOffset - snapshot.cameraOffsetX, 0);
      return;
    }
    this.renderer.panBy(0, targetOffset - snapshot.cameraOffsetY);
  }
}
