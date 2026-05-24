import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-background-color-dialog',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="background-color-dialog-backdrop" *ngIf="visible" (click)="requestCancel()">
      <div
        class="background-color-dialog"
        role="dialog"
        aria-modal="true"
        [attr.aria-label]="title"
        (click)="$event.stopPropagation()"
      >
        <button
          class="background-color-dialog-close"
          type="button"
          [attr.aria-label]="closeAriaLabel"
          (click)="requestCancel()"
        >
          ×
        </button>

        <label class="background-color-dialog-label">
          {{ title }}
        </label>
        <div class="background-color-preview" [style.background]="selectedColor"></div>
        <input
          class="background-color-hex-input"
          type="text"
          [value]="selectedColor"
          (input)="onHexInput($event)"
          (keydown)="onDialogKeyDown($event)"
          placeholder="#ffffff"
          autofocus
        />
        <label class="background-color-slider-label">
          R
          <input
            type="range"
            min="0"
            max="255"
            [value]="red"
            (input)="onRgbSliderInput('r', $event)"
            (keydown)="onDialogKeyDown($event)"
          />
          <span>{{ red }}</span>
        </label>
        <label class="background-color-slider-label">
          G
          <input
            type="range"
            min="0"
            max="255"
            [value]="green"
            (input)="onRgbSliderInput('g', $event)"
            (keydown)="onDialogKeyDown($event)"
          />
          <span>{{ green }}</span>
        </label>
        <label class="background-color-slider-label">
          B
          <input
            type="range"
            min="0"
            max="255"
            [value]="blue"
            (input)="onRgbSliderInput('b', $event)"
            (keydown)="onDialogKeyDown($event)"
          />
          <span>{{ blue }}</span>
        </label>
        <div class="background-color-presets">
          <button
            *ngFor="let preset of presetColors"
            class="background-color-preset"
            type="button"
            [style.background]="preset"
            [attr.aria-label]="preset"
            (click)="onPresetSelected(preset)"
          ></button>
        </div>
        <div class="background-color-actions">
          <button type="button" class="background-color-action-button" (click)="acceptCurrentColor()">
            {{ acceptLabel }}
          </button>
          <button type="button" class="background-color-action-button" (click)="requestCancel()">
            {{ cancelLabel }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: `
    .background-color-dialog-backdrop {
      position: fixed;
      inset: 0;
      z-index: 2100;
      background: rgba(16, 42, 67, 0.28);
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .background-color-dialog {
      position: relative;
      display: flex;
      flex-direction: column;
      align-items: stretch;
      justify-content: flex-start;
      width: min(360px, calc(100vw - 32px));
      min-height: 240px;
      border: 1px solid #243b53;
      border-radius: 10px;
      background: #ffffff;
      box-shadow: 0 16px 36px rgba(16, 42, 67, 0.25);
      padding: 24px 16px 16px;
      gap: 10px;
    }
    .background-color-dialog-close {
      position: absolute;
      top: 8px;
      right: 8px;
      border: none;
      background: transparent;
      color: #102a43;
      font: 18px/1 monospace;
      cursor: pointer;
      padding: 2px 4px;
    }
    .background-color-dialog-label {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #102a43;
      font: 13px/1.3 monospace;
    }
    .background-color-preview {
      width: 100%;
      height: 38px;
      border: 1px solid #486581;
      border-radius: 6px;
    }
    .background-color-hex-input {
      width: 100%;
      border: 1px solid #bcccdc;
      border-radius: 6px;
      padding: 6px 8px;
      color: #102a43;
      font: 13px/1.3 monospace;
    }
    .background-color-slider-label {
      display: grid;
      grid-template-columns: 18px 1fr 40px;
      align-items: center;
      gap: 8px;
      color: #102a43;
      font: 12px/1.2 monospace;
    }
    .background-color-slider-label span {
      text-align: right;
    }
    .background-color-presets {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 6px;
    }
    .background-color-preset {
      width: 100%;
      height: 24px;
      border: 1px solid #486581;
      border-radius: 4px;
      cursor: pointer;
    }
    .background-color-actions {
      display: flex;
      justify-content: flex-end;
      gap: 8px;
      margin-top: 2px;
    }
    .background-color-action-button {
      border: 1px solid #243b53;
      border-radius: 6px;
      background: #f0f4f8;
      color: #102a43;
      font: 12px/1.2 monospace;
      padding: 6px 10px;
      cursor: pointer;
    }
  `
})
export class BackgroundColorDialogComponent {
  @Input() public visible = false;
  @Input() public initialColor = '#ffffff';
  @Input() public title = '';
  @Input() public closeAriaLabel = 'Close';
  @Input() public acceptLabel = 'Accept';
  @Input() public cancelLabel = 'Cancel';
  @Output() public readonly accepted = new EventEmitter<string>();
  @Output() public readonly canceled = new EventEmitter<void>();

  public selectedColor = '#ffffff';
  public red = 255;
  public green = 255;
  public blue = 255;
  public readonly presetColors = ['#ffffff', '#ff8080', '#80ff80', '#8080ff', '#ffff80', '#ff80ff', '#80ffff'];

  public ngOnChanges(): void {
    const normalized = this.normalizeHexColor(this.initialColor) ?? '#ffffff';
    this.applyColor(normalized);
  }

  public onHexInput(event: Event): void {
    const input = event.target as HTMLInputElement | null;
    if (!input?.value) {
      return;
    }
    const normalized = this.normalizeHexColor(input.value);
    if (!normalized) {
      return;
    }
    this.applyColor(normalized);
  }

  public onRgbSliderInput(channel: 'r' | 'g' | 'b', event: Event): void {
    const input = event.target as HTMLInputElement | null;
    if (!input?.value) {
      return;
    }
    const value = Number.parseInt(input.value, 10);
    if (Number.isNaN(value)) {
      return;
    }
    if (channel === 'r') {
      this.red = value;
    } else if (channel === 'g') {
      this.green = value;
    } else {
      this.blue = value;
    }
    this.selectedColor = this.rgbToHex(this.red, this.green, this.blue);
  }

  public onDialogKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      event.preventDefault();
      this.acceptCurrentColor();
      return;
    }
    if (event.key === 'Escape') {
      event.preventDefault();
      this.requestCancel();
    }
  }

  public requestCancel(): void {
    this.canceled.emit();
  }

  public acceptCurrentColor(): void {
    this.accepted.emit(this.selectedColor);
  }

  public onPresetSelected(color: string): void {
    this.applyColor(color);
  }

  private applyColor(color: string): void {
    this.selectedColor = color;
    const { r, g, b } = this.hexToRgb(color);
    this.red = r;
    this.green = g;
    this.blue = b;
  }

  private normalizeHexColor(value: string): string | null {
    const trimmed = value.trim();
    const hex = trimmed.startsWith('#') ? trimmed.substring(1) : trimmed;
    if (!/^[0-9a-fA-F]{6}$/.test(hex)) {
      return null;
    }
    return `#${hex.toLowerCase()}`;
  }

  private hexToRgb(hexColor: string): { r: number; g: number; b: number } {
    const hex = hexColor.replace('#', '');
    return {
      r: Number.parseInt(hex.substring(0, 2), 16),
      g: Number.parseInt(hex.substring(2, 4), 16),
      b: Number.parseInt(hex.substring(4, 6), 16)
    };
  }

  private rgbToHex(r: number, g: number, b: number): string {
    const toHex = (value: number) => value.toString(16).padStart(2, '0');
    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
  }
}
