import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';

export interface RelationLineViewModel {
  text: string;
  invalid: boolean;
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
          {{ line.text }}
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
        color: #102a43;
      }

      .relation-box-line-invalid {
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

  public onCloseClick(): void {
    this.closeRequested.emit();
  }
}
