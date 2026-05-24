export type SvgPrimitiveKind = 'polygon' | 'ellipse' | 'path' | 'text';
export type SvgLayer = 'cluster' | 'edge' | 'node' | 'other';

export interface ViewBoxRect {
  x: number;
  y: number;
  width: number;
  height: number;
}

export interface BBoxRect {
  minX: number;
  minY: number;
  maxX: number;
  maxY: number;
}

export interface RendererViewportSnapshot {
  canvasWidth: number;
  canvasHeight: number;
  cameraScale: number;
  cameraOffsetX: number;
  cameraOffsetY: number;
  sceneBounds: BBoxRect | null;
}

export interface SvgPrimitive {
  kind: SvgPrimitiveKind;
  layer: SvgLayer;
  bbox: BBoxRect | null;
  stroke: string | null;
  fill: string | null;
  strokeWidth: number;
  points?: number[];
  cx?: number;
  cy?: number;
  rx?: number;
  ry?: number;
  d?: string;
  text?: string;
  x?: number;
  y?: number;
  fontSize?: number;
  fontFamily?: string;
  textAnchor?: CanvasTextAlign;
  nodeName?: string;
}

export interface InteractiveEllipse {
  nodeName: string;
  cx: number;
  cy: number;
  rx: number;
  ry: number;
}

interface SimpleTransform {
  sx: number;
  sy: number;
  tx: number;
  ty: number;
}

export class Html5CanvasGraphRenderer {
  private canvas: HTMLCanvasElement | null = null;
  private context: CanvasRenderingContext2D | null = null;
  private svgPrimitives: SvgPrimitive[] = [];
  private svgViewBox: ViewBoxRect | null = null;
  private interactiveEllipses: InteractiveEllipse[] = [];
  private sceneBounds: BBoxRect | null = null;
  private hoveredNodeName: string | null = null;
  private selectedNodeNames = new Set<string>();
  private nodeFillColorOverrides = new Map<string, string>();
  private cameraScale = 1;
  private cameraOffsetX = 0;
  private cameraOffsetY = 0;
  private readonly minScale = 0.05;
  private readonly maxScale = 8;
  private readonly zoomInFactor = 1.12;
  private readonly zoomOutFactor = 0.89;
  private readonly minZoomOutDamping = 0.15;
  private readonly minVisibleSceneAreaRatio = 0.05;
  private readonly renderListeners = new Set<() => void>();

  public attach(canvas: HTMLCanvasElement): boolean {
    this.canvas = canvas;
    this.context = canvas.getContext('2d');
    if (!this.context) {
      return false;
    }
    return true;
  }

  public loadFromSvgText(svgText: string): void {
    this.parseSvgScene(svgText);
    this.moveAndCenterToFit();
  }

  public getInteractiveEllipses(): ReadonlyArray<InteractiveEllipse> {
    return this.interactiveEllipses;
  }

  public screenToWorld(screenX: number, screenY: number): { x: number; y: number } {
    return {
      x: (screenX - this.cameraOffsetX) / this.cameraScale,
      y: (screenY - this.cameraOffsetY) / this.cameraScale
    };
  }

  public canvasPointFromEvent(event: MouseEvent): { x: number; y: number } {
    if (!this.canvas) {
      return { x: event.offsetX, y: event.offsetY };
    }
    const rect = this.canvas.getBoundingClientRect();
    const scaleX = rect.width > 0 ? this.canvas.width / rect.width : 1;
    const scaleY = rect.height > 0 ? this.canvas.height / rect.height : 1;
    return {
      x: (event.clientX - rect.left) * scaleX,
      y: (event.clientY - rect.top) * scaleY
    };
  }

  public canvasDeltaFromCss(deltaX: number, deltaY: number): { x: number; y: number } {
    if (!this.canvas) {
      return { x: deltaX, y: deltaY };
    }
    const rect = this.canvas.getBoundingClientRect();
    return {
      x: deltaX * (rect.width > 0 ? this.canvas.width / rect.width : 1),
      y: deltaY * (rect.height > 0 ? this.canvas.height / rect.height : 1)
    };
  }

  public pickNodeNameFromEvent(event: MouseEvent): string | null {
    const point = this.canvasPointFromEvent(event);
    const world = this.screenToWorld(point.x, point.y);
    for (const ellipse of this.interactiveEllipses) {
      if (this.isInsideEllipse(ellipse, world.x, world.y)) {
        return ellipse.nodeName;
      }
    }
    return null;
  }

  public pickRectangularNodeNameFromEvent(event: MouseEvent): string | null {
    const point = this.canvasPointFromEvent(event);
    const world = this.screenToWorld(point.x, point.y);
    for (let index = this.svgPrimitives.length - 1; index >= 0; index -= 1) {
      const primitive = this.svgPrimitives[index];
      if (
        primitive.kind !== 'polygon' ||
        primitive.layer !== 'node' ||
        !primitive.nodeName ||
        !primitive.bbox
      ) {
        continue;
      }
      if (this.isInsideBBox(primitive.bbox, world.x, world.y)) {
        return primitive.nodeName;
      }
    }
    return null;
  }

  public getZoomOutCenteringProgress(): number {
    return this.computeZoomOutCenteringProgress();
  }

  public setHoveredNode(nodeName: string | null): void {
    if (this.hoveredNodeName === nodeName) {
      return;
    }
    this.hoveredNodeName = nodeName;
    this.render();
  }

  public setSelectedNodes(nodeNames: string[]): void {
    this.selectedNodeNames = new Set(nodeNames);
    this.render();
  }

  public setNodeFillColorOverrides(overridesByNodeName: ReadonlyMap<string, string>): void {
    this.nodeFillColorOverrides = new Map(overridesByNodeName);
    this.render();
  }

  public render(): void {
    if (!this.canvas || !this.context) {
      this.notifyRenderListeners();
      return;
    }

    this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    if (this.svgPrimitives.length === 0 || !this.svgViewBox) {
      this.notifyRenderListeners();
      return;
    }

    this.clampCameraScale();
    this.keepSceneAtLeastPartlyVisible();

    const viewportWorld: BBoxRect = {
      minX: (0 - this.cameraOffsetX) / this.cameraScale,
      minY: (0 - this.cameraOffsetY) / this.cameraScale,
      maxX: (this.canvas.width - this.cameraOffsetX) / this.cameraScale,
      maxY: (this.canvas.height - this.cameraOffsetY) / this.cameraScale
    };

    const hideEdges = this.cameraScale < 0.12;
    const hideText = this.cameraScale < 0.35;

    this.context.save();
    this.context.translate(this.cameraOffsetX, this.cameraOffsetY);
    this.context.scale(this.cameraScale, this.cameraScale);
    this.context.beginPath();
    this.context.rect(this.svgViewBox.x, this.svgViewBox.y, this.svgViewBox.width, this.svgViewBox.height);
    this.context.clip();

    for (const primitive of this.svgPrimitives) {
      if (primitive.bbox && !this.intersects(viewportWorld, primitive.bbox)) {
        continue;
      }
      if (hideEdges && primitive.layer === 'edge') {
        continue;
      }
      if (hideText && primitive.kind === 'text') {
        continue;
      }
      this.drawPrimitive(primitive);
    }

    this.context.restore();
    this.notifyRenderListeners();
  }

  public onRender(listener: () => void): () => void {
    this.renderListeners.add(listener);
    return () => {
      this.renderListeners.delete(listener);
    };
  }

  public getViewportSnapshot(): RendererViewportSnapshot | null {
    if (!this.canvas) {
      return null;
    }
    return {
      canvasWidth: this.canvas.width,
      canvasHeight: this.canvas.height,
      cameraScale: this.cameraScale,
      cameraOffsetX: this.cameraOffsetX,
      cameraOffsetY: this.cameraOffsetY,
      sceneBounds: this.sceneBounds ? { ...this.sceneBounds } : null
    };
  }

  public zoomAt(screenX: number, screenY: number, wheelDeltaY: number): void {
    const worldBeforeX = (screenX - this.cameraOffsetX) / this.cameraScale;
    const worldBeforeY = (screenY - this.cameraOffsetY) / this.cameraScale;
    const previousScale = this.cameraScale;

    this.cameraScale = this.computeNextZoomScale(wheelDeltaY);
    this.cameraOffsetX = screenX - worldBeforeX * this.cameraScale;
    this.cameraOffsetY = screenY - worldBeforeY * this.cameraScale;
    if (this.cameraScale < previousScale) {
      this.applyZoomOutRecentering();
    }
    this.keepSceneAtLeastPartlyVisible();
    this.render();
  }

  public panBy(deltaX: number, deltaY: number): void {
    this.cameraOffsetX += deltaX;
    this.cameraOffsetY += deltaY;
    this.keepSceneAtLeastPartlyVisible();
    this.render();
  }

  public moveAndCenterToFit(): void {
    if (!this.canvas) {
      return;
    }

    const bounds = this.getSceneBoundsRect();
    if (!bounds || bounds.width <= 0 || bounds.height <= 0) {
      return;
    }

    const scaleX = this.canvas.width / bounds.width;
    const scaleY = this.canvas.height / bounds.height;
    this.cameraScale = Math.max(this.minScale, Math.min(this.maxScale, Math.min(scaleX, scaleY)));

    const centerX = bounds.x + bounds.width * 0.5;
    const centerY = bounds.y + bounds.height * 0.5;
    this.cameraOffsetX = this.canvas.width * 0.5 - centerX * this.cameraScale;
    this.cameraOffsetY = this.canvas.height * 0.5 - centerY * this.cameraScale;
    this.clampCameraScale();
    this.keepSceneAtLeastPartlyVisible();
    this.render();
  }

  public moveAndFitVerticalToTop(): void {
    if (!this.canvas) {
      return;
    }

    const bounds = this.getSceneBoundsRect();
    if (!bounds || bounds.width <= 0 || bounds.height <= 0) {
      return;
    }

    const scaleY = this.canvas.height / bounds.height;
    this.cameraScale = Math.max(this.minScale, Math.min(this.maxScale, scaleY));

    this.cameraOffsetX = -bounds.x * this.cameraScale;
    this.cameraOffsetY = -bounds.y * this.cameraScale;
    this.clampCameraScale();
    this.keepSceneAtLeastPartlyVisible();
    this.render();
  }

  public moveAndFitHorizontalToLeft(): void {
    if (!this.canvas) {
      return;
    }

    const bounds = this.getSceneBoundsRect();
    if (!bounds || bounds.width <= 0 || bounds.height <= 0) {
      return;
    }

    const scaleX = this.canvas.width / bounds.width;
    this.cameraScale = Math.max(this.minScale, Math.min(this.maxScale, scaleX));

    this.cameraOffsetX = -bounds.x * this.cameraScale;
    this.cameraOffsetY = -bounds.y * this.cameraScale;
    this.clampCameraScale();
    this.keepSceneAtLeastPartlyVisible();
    this.render();
  }

  private parseSvgScene(svgText: string): void {
    const parser = new DOMParser();
    const doc = parser.parseFromString(svgText, 'image/svg+xml');
    const root = doc.documentElement;

    const viewBox = root.getAttribute('viewBox');
    if (!viewBox) {
      this.svgPrimitives = [];
      this.svgViewBox = null;
      this.interactiveEllipses = [];
      this.sceneBounds = null;
      return;
    }

    const [x, y, width, height] = viewBox.split(/\s+/).map((value) => Number(value));
    this.svgViewBox = { x, y, width, height };

    const rootGraphGroup = root.querySelector('g.graph');
    const globalTransform = this.parseGraphvizTransform(rootGraphGroup?.getAttribute('transform'));

    const primitives: SvgPrimitive[] = [];
    const interactiveEllipses: InteractiveEllipse[] = [];

    root.querySelectorAll('g').forEach((group) => {
      const className = group.getAttribute('class') ?? '';
      const layer: SvgLayer = className.includes('node')
        ? 'node'
        : className.includes('edge')
          ? 'edge'
          : className.includes('cluster')
            ? 'cluster'
            : 'other';

      const nodeName = this.readNodeName(group);

      group.querySelectorAll(':scope > polygon').forEach((el) => {
        const points = this.applyTransformToPoints(this.parsePoints(el.getAttribute('points') ?? ''), globalTransform);
        primitives.push({
          kind: 'polygon',
          layer,
          bbox: this.computePolygonBBox(points),
          stroke: this.normalizeColor(el.getAttribute('stroke')),
          fill: this.normalizeColor(el.getAttribute('fill')),
          strokeWidth: this.parseNumber(el.getAttribute('stroke-width'), 1),
          points,
          nodeName
        });
      });

      group.querySelectorAll(':scope > ellipse').forEach((el) => {
        const rawCx = this.parseNumber(el.getAttribute('cx'), 0);
        const rawCy = this.parseNumber(el.getAttribute('cy'), 0);
        const center = this.applyTransformToPoint(rawCx, rawCy, globalTransform);
        const rx = this.parseNumber(el.getAttribute('rx'), 0) * Math.abs(globalTransform.sx);
        const ry = this.parseNumber(el.getAttribute('ry'), 0) * Math.abs(globalTransform.sy);
        const stroke = this.normalizeColor(el.getAttribute('stroke'));
        const fill = this.normalizeColor(el.getAttribute('fill'));

        const ellipsePrimitive: SvgPrimitive = {
          kind: 'ellipse',
          layer,
          bbox: { minX: center.x - rx, minY: center.y - ry, maxX: center.x + rx, maxY: center.y + ry },
          stroke,
          fill,
          strokeWidth: this.parseNumber(el.getAttribute('stroke-width'), 1),
          cx: center.x,
          cy: center.y,
          rx,
          ry,
          nodeName
        };
        primitives.push(ellipsePrimitive);

        if (nodeName && this.isInteractiveEllipse(layer)) {
          interactiveEllipses.push({ nodeName, cx: center.x, cy: center.y, rx, ry });
        }
      });

      group.querySelectorAll(':scope > path').forEach((el) => {
        const d = this.applyTransformToPathData(el.getAttribute('d') ?? '', globalTransform);
        primitives.push({
          kind: 'path',
          layer,
          bbox: this.computePathBBox(d),
          stroke: this.normalizeColor(el.getAttribute('stroke')),
          fill: this.normalizeColor(el.getAttribute('fill')),
          strokeWidth: this.parseNumber(el.getAttribute('stroke-width'), 1),
          d,
          nodeName
        });
      });

      group.querySelectorAll(':scope > text').forEach((el) => {
        const xText = el.getAttribute('x');
        const yText = el.getAttribute('y');
        if (xText == null || yText == null) {
          return;
        }

        const sourceX = this.parseNumber(xText, 0);
        const sourceY = this.parseNumber(yText, 0);
        const p = this.applyTransformToPoint(sourceX, sourceY, globalTransform);
        const fontSize = this.parseNumber(el.getAttribute('font-size'), 12);
        const text = el.textContent ?? '';
        const estimatedWidth = text.length * fontSize * 0.58;

        primitives.push({
          kind: 'text',
          layer,
          bbox: {
            minX: p.x - estimatedWidth / 2,
            minY: p.y - fontSize,
            maxX: p.x + estimatedWidth / 2,
            maxY: p.y + fontSize * 0.3
          },
          stroke: null,
          fill: this.normalizeColor(el.getAttribute('fill')) ?? '#000000',
          strokeWidth: 0,
          text,
          x: p.x,
          y: p.y,
          fontSize,
          fontFamily: el.getAttribute('font-family') ?? 'serif',
          textAnchor: this.mapTextAnchor(el.getAttribute('text-anchor')),
          nodeName
        });
      });
    });

    this.svgPrimitives = primitives;
    this.interactiveEllipses = interactiveEllipses;
    this.sceneBounds = this.computeSceneBounds(primitives);
  }

  private fitCameraToScene(): void {
    if (!this.canvas) {
      return;
    }
    const bounds = this.getSceneBoundsRect();
    if (!bounds) {
      return;
    }
    const minScaleToFit = this.computeMinScaleToFit();
    this.cameraScale = minScaleToFit;
    const centerX = bounds.x + bounds.width * 0.5;
    const centerY = bounds.y + bounds.height * 0.5;
    this.cameraOffsetX = this.canvas.width * 0.5 - centerX * this.cameraScale;
    this.cameraOffsetY = this.canvas.height * 0.5 - centerY * this.cameraScale;
    this.clampCameraScale();
    this.keepSceneAtLeastPartlyVisible();
  }

  private drawPrimitive(primitive: SvgPrimitive): void {
    if (!this.context) {
      return;
    }

    let strokeColor = primitive.stroke;
    let strokeWidth = primitive.strokeWidth || 1;

    if (primitive.kind === 'ellipse' && primitive.nodeName && this.isInteractiveEllipse(primitive.layer)) {
      const isHovered = primitive.nodeName === this.hoveredNodeName;
      const isSelected = this.selectedNodeNames.has(primitive.nodeName);
      if (isHovered || isSelected) {
        strokeColor = 'red';
        strokeWidth = Math.max(1, strokeWidth * 2);
      }
    }

    this.context.lineWidth = strokeWidth;
    const fillColor = this.resolveFillColor(primitive);

    if (primitive.kind === 'polygon' && primitive.points && primitive.points.length >= 4) {
      this.context.beginPath();
      this.context.moveTo(primitive.points[0], primitive.points[1]);
      for (let i = 2; i < primitive.points.length; i += 2) {
        this.context.lineTo(primitive.points[i], primitive.points[i + 1]);
      }
      this.context.closePath();
      if (fillColor && fillColor !== 'none') {
        this.context.fillStyle = fillColor;
        this.context.fill();
      }
      if (strokeColor && strokeColor !== 'none') {
        this.context.strokeStyle = strokeColor;
        this.context.stroke();
      }
      return;
    }

    if (primitive.kind === 'ellipse' && primitive.cx != null && primitive.cy != null && primitive.rx != null && primitive.ry != null) {
      this.context.beginPath();
      this.context.ellipse(primitive.cx, primitive.cy, primitive.rx, primitive.ry, 0, 0, Math.PI * 2);
      if (fillColor && fillColor !== 'none') {
        this.context.fillStyle = fillColor;
        this.context.fill();
      }
      if (strokeColor && strokeColor !== 'none') {
        this.context.strokeStyle = strokeColor;
        this.context.stroke();
      }
      return;
    }

    if (primitive.kind === 'path' && primitive.d) {
      const path = new Path2D(primitive.d);
      if (fillColor && fillColor !== 'none') {
        this.context.fillStyle = fillColor;
        this.context.fill(path);
      }
      if (strokeColor && strokeColor !== 'none') {
        this.context.strokeStyle = strokeColor;
        this.context.stroke(path);
      }
      return;
    }

    if (primitive.kind === 'text' && primitive.text != null && primitive.x != null && primitive.y != null) {
      this.context.fillStyle = primitive.fill ?? '#000000';
      this.context.font = `${primitive.fontSize ?? 12}px ${primitive.fontFamily ?? 'serif'}`;
      this.context.textAlign = primitive.textAnchor ?? 'center';
      this.context.textBaseline = 'alphabetic';
      this.context.fillText(primitive.text, primitive.x, primitive.y);
    }
  }

  private resolveFillColor(primitive: SvgPrimitive): string | null {
    const overrideColor = primitive.nodeName ? this.nodeFillColorOverrides.get(primitive.nodeName) : null;
    return overrideColor ?? primitive.fill;
  }

  private readNodeName(group: Element): string | undefined {
    const title = group.querySelector(':scope > title')?.textContent?.trim();
    if (title) {
      return title;
    }
    const text = group.querySelector(':scope > text')?.textContent?.trim();
    return text || undefined;
  }

  private parsePoints(pointsText: string): number[] {
    return pointsText
      .trim()
      .split(/\s+/)
      .flatMap((pair) => {
        const [x, y] = pair.split(',');
        return [Number(x), Number(y)];
      })
      .filter((value) => Number.isFinite(value));
  }

  private computePolygonBBox(points: number[]): BBoxRect | null {
    if (points.length < 2) {
      return null;
    }
    let minX = points[0];
    let maxX = points[0];
    let minY = points[1];
    let maxY = points[1];
    for (let i = 2; i < points.length; i += 2) {
      const x = points[i];
      const y = points[i + 1];
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
    }
    return { minX, minY, maxX, maxY };
  }

  private computePathBBox(pathData: string): BBoxRect | null {
    const numbers = (pathData.match(/-?\d*\.?\d+/g) ?? []).map((value) => Number(value));
    if (numbers.length < 2) {
      return null;
    }
    let minX = Number.POSITIVE_INFINITY;
    let maxX = Number.NEGATIVE_INFINITY;
    let minY = Number.POSITIVE_INFINITY;
    let maxY = Number.NEGATIVE_INFINITY;
    for (let i = 0; i < numbers.length - 1; i += 2) {
      const x = numbers[i];
      const y = numbers[i + 1];
      if (!Number.isFinite(x) || !Number.isFinite(y)) {
        continue;
      }
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
    }
    if (!Number.isFinite(minX)) {
      return null;
    }
    return { minX, minY, maxX, maxY };
  }

  private computeSceneBounds(primitives: SvgPrimitive[]): BBoxRect | null {
    let minX = Number.POSITIVE_INFINITY;
    let minY = Number.POSITIVE_INFINITY;
    let maxX = Number.NEGATIVE_INFINITY;
    let maxY = Number.NEGATIVE_INFINITY;

    for (const primitive of primitives) {
      if (!primitive.bbox) {
        continue;
      }
      minX = Math.min(minX, primitive.bbox.minX);
      minY = Math.min(minY, primitive.bbox.minY);
      maxX = Math.max(maxX, primitive.bbox.maxX);
      maxY = Math.max(maxY, primitive.bbox.maxY);
    }

    if (!Number.isFinite(minX) || !Number.isFinite(minY) || !Number.isFinite(maxX) || !Number.isFinite(maxY)) {
      return null;
    }

    return { minX, minY, maxX, maxY };
  }

  private normalizeColor(value: string | null): string | null {
    if (!value || value.trim() === '') {
      return null;
    }
    return value.trim();
  }

  private parseNumber(value: string | null, fallback: number): number {
    if (!value) {
      return fallback;
    }
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : fallback;
  }

  private mapTextAnchor(anchor: string | null): CanvasTextAlign {
    if (anchor === 'start') {
      return 'left';
    }
    if (anchor === 'end') {
      return 'right';
    }
    return 'center';
  }

  private intersects(a: BBoxRect, b: BBoxRect): boolean {
    return !(a.maxX < b.minX || a.minX > b.maxX || a.maxY < b.minY || a.minY > b.maxY);
  }

  private isInsideBBox(bounds: BBoxRect, x: number, y: number): boolean {
    return x >= bounds.minX && x <= bounds.maxX && y >= bounds.minY && y <= bounds.maxY;
  }

  private parseGraphvizTransform(transform: string | null | undefined): SimpleTransform {
    let sx = 1;
    let sy = 1;
    let tx = 0;
    let ty = 0;

    if (!transform) {
      return { sx, sy, tx, ty };
    }

    const scaleMatch = transform.match(/scale\(([-\d.]+)(?:\s+|,)?([-\d.]+)?\)/);
    if (scaleMatch) {
      sx = Number(scaleMatch[1]);
      sy = scaleMatch[2] != null ? Number(scaleMatch[2]) : sx;
      if (!Number.isFinite(sx)) {
        sx = 1;
      }
      if (!Number.isFinite(sy)) {
        sy = 1;
      }
    }

    const translateMatch = transform.match(/translate\(([-\d.]+)(?:\s+|,)?([-\d.]+)?\)/);
    if (translateMatch) {
      tx = Number(translateMatch[1]);
      ty = translateMatch[2] != null ? Number(translateMatch[2]) : 0;
      if (!Number.isFinite(tx)) {
        tx = 0;
      }
      if (!Number.isFinite(ty)) {
        ty = 0;
      }
    }

    return { sx, sy, tx, ty };
  }

  private applyTransformToPoint(x: number, y: number, transform: SimpleTransform): { x: number; y: number } {
    return {
      x: x * transform.sx + transform.tx,
      y: y * transform.sy + transform.ty
    };
  }

  private applyTransformToPoints(points: number[], transform: SimpleTransform): number[] {
    const transformed: number[] = [];
    for (let i = 0; i < points.length - 1; i += 2) {
      const p = this.applyTransformToPoint(points[i], points[i + 1], transform);
      transformed.push(p.x, p.y);
    }
    return transformed;
  }

  private applyTransformToPathData(pathData: string, transform: SimpleTransform): string {
    const tokens = pathData.match(/[A-Za-z]|-?\d*\.?\d+/g);
    if (!tokens) {
      return pathData;
    }

    let i = 0;
    let command = '';
    const out: string[] = [];

    while (i < tokens.length) {
      const token = tokens[i];
      if (/^[A-Za-z]$/.test(token)) {
        command = token;
        out.push(token);
        i++;
        continue;
      }

      if (command === 'M' || command === 'L') {
        const x = Number(tokens[i]);
        const y = Number(tokens[i + 1]);
        const p = this.applyTransformToPoint(x, y, transform);
        out.push(String(p.x), String(p.y));
        i += 2;
        continue;
      }

      if (command === 'C') {
        for (let step = 0; step < 3; step++) {
          const x = Number(tokens[i]);
          const y = Number(tokens[i + 1]);
          const p = this.applyTransformToPoint(x, y, transform);
          out.push(String(p.x), String(p.y));
          i += 2;
        }
        continue;
      }

      out.push(token);
      i++;
    }

    return out.join(' ');
  }

  private isInteractiveEllipse(layer: SvgLayer): boolean {
    return layer === 'node';
  }

  private isInsideEllipse(ellipse: InteractiveEllipse, x: number, y: number): boolean {
    if (ellipse.rx <= 0 || ellipse.ry <= 0) {
      return false;
    }
    const dx = (x - ellipse.cx) / ellipse.rx;
    const dy = (y - ellipse.cy) / ellipse.ry;
    return dx * dx + dy * dy <= 1;
  }

  private clampCameraScale(): void {
    const minScaleToFit = this.computeMinScaleToFit();
    this.cameraScale = Math.max(minScaleToFit, Math.min(this.maxScale, this.cameraScale));
  }

  private computeNextZoomScale(wheelDeltaY: number): number {
    const minScaleToFit = this.computeMinScaleToFit();
    if (wheelDeltaY < 0) {
      return Math.max(minScaleToFit, Math.min(this.maxScale, this.cameraScale * this.zoomInFactor));
    }

    const fitScale = this.computeFitScale();
    const zoomOutRange = Number.isFinite(fitScale) ? Math.max(0.000001, fitScale - minScaleToFit) : 1;
    const distanceFromMin = Math.max(0, Math.min(1, (this.cameraScale - minScaleToFit) / zoomOutRange));
    const dampedZoomOutFactor = 1 - (1 - this.zoomOutFactor) * (this.minZoomOutDamping + (1 - this.minZoomOutDamping) * distanceFromMin);
    return Math.max(minScaleToFit, Math.min(this.maxScale, this.cameraScale * dampedZoomOutFactor));
  }

  private applyZoomOutRecentering(): void {
    if (!this.canvas) {
      return;
    }

    const fitScale = this.computeFitScale();
    if (this.cameraScale >= fitScale) {
      return;
    }

    const bounds = this.getSceneBoundsRect();
    if (!bounds) {
      return;
    }

    const centerX = bounds.x + bounds.width * 0.5;
    const centerY = bounds.y + bounds.height * 0.5;
    const centeredOffsetX = this.canvas.width * 0.5 - centerX * this.cameraScale;
    const centeredOffsetY = this.canvas.height * 0.5 - centerY * this.cameraScale;
    const recenterFactor = this.computeZoomOutCenteringProgress();

    this.cameraOffsetX += (centeredOffsetX - this.cameraOffsetX) * recenterFactor;
    this.cameraOffsetY += (centeredOffsetY - this.cameraOffsetY) * recenterFactor;
  }

  private keepSceneAtLeastPartlyVisible(): void {
    if (!this.canvas) {
      return;
    }

    const bounds = this.getSceneBoundsRect();
    if (!bounds) {
      return;
    }

    const visibleCanvas = this.getVisibleCanvasRect();
    const sceneWidth = bounds.width * this.cameraScale;
    const sceneHeight = bounds.height * this.cameraScale;
    const minVisibleEdgeRatio = Math.sqrt(this.minVisibleSceneAreaRatio);
    const minVisibleWidth = Math.min(sceneWidth, visibleCanvas.width, sceneWidth * minVisibleEdgeRatio);
    const minVisibleHeight = Math.min(sceneHeight, visibleCanvas.height, sceneHeight * minVisibleEdgeRatio);

    const minOffsetX = visibleCanvas.x + minVisibleWidth - (bounds.x + bounds.width) * this.cameraScale;
    const maxOffsetX = visibleCanvas.x + visibleCanvas.width - minVisibleWidth - bounds.x * this.cameraScale;
    const minOffsetY = visibleCanvas.y + minVisibleHeight - (bounds.y + bounds.height) * this.cameraScale;
    const maxOffsetY = visibleCanvas.y + visibleCanvas.height - minVisibleHeight - bounds.y * this.cameraScale;

    this.cameraOffsetX = Math.max(minOffsetX, Math.min(maxOffsetX, this.cameraOffsetX));
    this.cameraOffsetY = Math.max(minOffsetY, Math.min(maxOffsetY, this.cameraOffsetY));
  }

  private computeZoomOutCenteringProgress(): number {
    const fitScale = this.computeFitScale();
    if (!Number.isFinite(fitScale) || this.cameraScale >= fitScale) {
      return 0;
    }

    const minScaleToFit = this.computeMinScaleToFit();
    const zoomOutRange = Math.max(0.000001, fitScale - minScaleToFit);
    return Math.max(0, Math.min(1, (fitScale - this.cameraScale) / zoomOutRange));
  }

  private computeMinScaleToFit(): number {
    const fitScale = this.computeFitScale();
    if (!Number.isFinite(fitScale)) {
      return this.minScale;
    }
    return Math.max(this.minScale, Math.min(this.maxScale, fitScale * 0.5));
  }

  private computeFitScale(): number {
    if (!this.canvas) {
      return Number.NaN;
    }
    const bounds = this.getSceneBoundsRect();
    if (!bounds || bounds.width <= 0 || bounds.height <= 0) {
      return Number.NaN;
    }
    const scaleX = this.canvas.width / bounds.width;
    const scaleY = this.canvas.height / bounds.height;
    return Math.min(scaleX, scaleY);
  }

  private getSceneBoundsRect(): ViewBoxRect | null {
    if (this.sceneBounds) {
      return {
        x: this.sceneBounds.minX,
        y: this.sceneBounds.minY,
        width: this.sceneBounds.maxX - this.sceneBounds.minX,
        height: this.sceneBounds.maxY - this.sceneBounds.minY
      };
    }
    if (!this.svgViewBox) {
      return null;
    }
    return this.svgViewBox;
  }

  private getVisibleCanvasRect(): ViewBoxRect {
    if (!this.canvas) {
      return { x: 0, y: 0, width: 1, height: 1 };
    }

    const rect = this.canvas.getBoundingClientRect();
    const scaleX = rect.width > 0 ? this.canvas.width / rect.width : 1;
    const scaleY = rect.height > 0 ? this.canvas.height / rect.height : 1;
    const scrollContainer = this.canvas.parentElement;

    if (!scrollContainer) {
      return { x: 0, y: 0, width: this.canvas.width, height: this.canvas.height };
    }

    return {
      x: scrollContainer.scrollLeft * scaleX,
      y: scrollContainer.scrollTop * scaleY,
      width: Math.min(this.canvas.width, scrollContainer.clientWidth * scaleX),
      height: Math.min(this.canvas.height, scrollContainer.clientHeight * scaleY)
    };
  }

  private notifyRenderListeners(): void {
    for (const listener of this.renderListeners) {
      listener();
    }
  }
}
