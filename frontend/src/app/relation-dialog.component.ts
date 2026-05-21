import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';

export interface RelationLineViewModel {
  text: string;
  invalid: boolean;
  sourceEndpoint?: string;
  targetEndpoint?: string;
  clickable?: boolean;
  checked?: boolean;
}

export interface RelationEndpointClickEvent {
  endpoint: string;
  counterpartEndpoint?: string;
  mouseX: number;
  mouseY: number;
}

export interface RelationLineCheckboxToggleEvent {
  line: RelationLineViewModel;
  checked: boolean;
}

@Component({
  selector: 'app-relation-dialog',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="relation-box" *ngIf="visible">
      <button type="button" class="relation-box-close" (click)="onCloseClick()" [attr.aria-label]="closeAriaLabel">X</button>
      <div class="relation-box-content" *ngIf="lines.length > 0; else plainTextBlock">
        <div class="relation-box-line" *ngFor="let line of lines" [class.relation-box-line-invalid]="line.invalid">
          <input
            type="checkbox"
            class="relation-box-line-checkbox"
            [checked]="line.checked === true"
            (change)="onLineCheckboxChange(line, $event)"
          />
          <ng-container *ngIf="line.clickable && line.sourceEndpoint && line.targetEndpoint; else plainLineBlock">
            <span>{{ endpointGroupPrefix(line.sourceEndpoint) }}</span>
            <a
              href="#"
              class="relation-box-link"
              [class.relation-box-link-invalid]="line.invalid"
              (click)="onLinkClick($event, line.sourceEndpoint)"
              (contextmenu)="onLinkContextMenu($event, line.sourceEndpoint, line.targetEndpoint)"
              >{{ endpointNodeName(line.sourceEndpoint) }}</a
            >
            <span> -> </span>
            <span>{{ endpointGroupPrefix(line.targetEndpoint) }}</span>
            <a
              href="#"
              class="relation-box-link"
              [class.relation-box-link-invalid]="line.invalid"
              (click)="onLinkClick($event, line.targetEndpoint)"
              (contextmenu)="onLinkContextMenu($event, line.targetEndpoint, line.sourceEndpoint)"
              >{{ endpointNodeName(line.targetEndpoint) }}</a
            >
          </ng-container>
          <ng-template #plainLineBlock>{{ line.text }}</ng-template>
        </div>
      </div>
      <ng-template #plainTextBlock>
        <textarea class="relation-box-textarea" [value]="text" readonly></textarea>
      </ng-template>
    </div>
  `,
  styles: [
    `
      .relation-box {
        position: fixed;
        top: 0.5rem;
        right: 0.5rem;
        z-index: 3000;
        width: 42rem;
        max-width: calc(100vw - 1rem);
        background: #ffffff;
        border: 1px solid #8aa1b8;
        border-radius: 0.45rem;
        box-shadow: 0 8px 24px rgba(16, 42, 67, 0.22);
        padding: 0.35rem;
      }

      .relation-box-close {
        position: absolute;
        top: 0.35rem;
        right: 0.35rem;
        border: 1px solid #8aa1b8;
        border-radius: 0.3rem;
        background: #ffffff;
        min-width: 1.5rem;
        min-height: 1.5rem;
        line-height: 1.2;
        cursor: pointer;
      }

      .relation-box-textarea {
        width: 100%;
        height: 14rem;
        resize: vertical;
        border: 1px solid #bcccdc;
        border-radius: 0.35rem;
        padding: 0.6rem;
        box-sizing: border-box;
        font: 12px/1.35 monospace;
        color: #102a43;
      }

      .relation-box-content {
        width: 100%;
        height: 14rem;
        overflow: auto;
        border: 1px solid #bcccdc;
        border-radius: 0.35rem;
        padding: 0.6rem;
        box-sizing: border-box;
        font: 12px/1.35 monospace;
        color: #102a43;
        white-space: pre;
      }

      .relation-box-line {
        display: flex;
        align-items: center;
        gap: 0.35rem;
        color: #102a43;
      }

      .relation-box-line-checkbox {
        margin: 0;
        flex: 0 0 auto;
      }

      .relation-box-line-invalid {
        color: #b42318;
      }

      .relation-box-link {
        color: #102a43;
        text-decoration: underline;
        cursor: pointer;
      }

      .relation-box-link-invalid {
        color: #b42318;
      }
    `
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RelationDialogComponent {
  @Input() public visible = false;
  @Input() public text = '';
  @Input() public lines: RelationLineViewModel[] = [];
  @Input() public closeAriaLabel = 'Close';

  @Output() public closeRequested = new EventEmitter<void>();
  @Output() public endpointClicked = new EventEmitter<RelationEndpointClickEvent>();
  @Output() public endpointContextMenuRequested = new EventEmitter<RelationEndpointClickEvent>();
  @Output() public lineCheckboxToggled = new EventEmitter<RelationLineCheckboxToggleEvent>();

  public onCloseClick(): void {
    this.closeRequested.emit();
  }

  public onLinkClick(event: MouseEvent, endpoint: string): void {
    event.preventDefault();
    this.endpointClicked.emit({
      endpoint,
      mouseX: event.clientX,
      mouseY: event.clientY
    });
  }

  public onLinkContextMenu(event: MouseEvent, endpoint: string, counterpartEndpoint?: string): void {
    event.preventDefault();
    this.endpointContextMenuRequested.emit({
      endpoint,
      counterpartEndpoint,
      mouseX: event.clientX,
      mouseY: event.clientY
    });
  }

  public onLineCheckboxChange(line: RelationLineViewModel, event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    this.lineCheckboxToggled.emit({ line, checked });
  }

  public endpointGroupPrefix(endpoint: string): string {
    const match = endpoint.trim().match(/^(\[[^\]]+\]\.)/);
    return match?.[1] ?? '';
  }

  public endpointNodeName(endpoint: string): string {
    const match = endpoint.trim().match(/^\[[^\]]+\]\.(.+)$/);
    return match?.[1] ?? endpoint;
  }
}
