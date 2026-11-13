import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { I18nService } from '../i18n/services/i18n.service';
import { I18nStateService } from '../i18n/services/i18n-state.service';
import { I18N_KEYS } from '../i18n/translations/i18n-keys.const';
import type { TranslationKey } from '../i18n/translations/translations-by-namespace.const';
import type { LfsDownloadProgressState } from './LfsSourceDownloadCoordinator';

/** Modal progress dialog for "Calcular LFS". Only renders; the run is driven by the coordinator. */
@Component({
  selector: 'app-lfs-progress-dialog',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="lfs-dialog-backdrop" *ngIf="state">
      <div class="lfs-dialog" role="dialog" aria-modal="true" [attr.aria-label]="t(keys.LFS_DIALOG_TITLE)">
        <h2 class="lfs-dialog-title">{{ t(keys.LFS_DIALOG_TITLE) }}</h2>

        <ng-container *ngIf="state.phase === 'planning'">
          <p class="lfs-dialog-line">{{ t(keys.LFS_PLANNING) }}</p>
          <div class="lfs-progress lfs-progress-indeterminate"><div class="lfs-progress-fill"></div></div>
        </ng-container>

        <ng-container *ngIf="state.phase !== 'planning' && state.phase !== 'failed'">
          <p class="lfs-dialog-line lfs-dialog-count">
            {{ state.completed }} / {{ state.total }} {{ t(keys.LFS_PACKAGES) }}
          </p>
          <div
            class="lfs-progress"
            role="progressbar"
            aria-valuemin="0"
            [attr.aria-valuemax]="state.total"
            [attr.aria-valuenow]="state.completed"
          >
            <div class="lfs-progress-fill" [style.width.%]="percent"></div>
          </div>
          <p class="lfs-dialog-line lfs-dialog-current" *ngIf="state.current">
            {{ t(keys.LFS_DOWNLOADING) }}: <strong>{{ state.current.packageName }}</strong>
            <span *ngIf="state.current.groupName"> [{{ state.current.groupName }}]</span>
          </p>
          <p class="lfs-dialog-line" *ngIf="state.alreadyPresent > 0">
            {{ t(keys.LFS_ALREADY_PRESENT) }}: {{ state.alreadyPresent }}
          </p>
          <p class="lfs-dialog-line" *ngIf="state.phase === 'writingGuide'">{{ t(keys.LFS_WRITING_GUIDE) }}</p>
          <p class="lfs-dialog-line" *ngIf="state.phase === 'finished'">{{ t(keys.LFS_FINISHED) }}</p>
          <p class="lfs-dialog-line" *ngIf="state.phase === 'canceled'">{{ t(keys.LFS_CANCELED) }}</p>
        </ng-container>

        <p class="lfs-dialog-line lfs-dialog-error" *ngIf="state.phase === 'failed'">{{ state.errorMessage }}</p>
        <p class="lfs-dialog-line" *ngIf="state.guideFile">
          {{ t(keys.LFS_GUIDE_WRITTEN) }}: <code>{{ state.guideFile }}</code>
        </p>
        <p class="lfs-dialog-line lfs-dialog-error" *ngIf="state.guideError">
          {{ t(keys.LFS_GUIDE_FAILED) }}: {{ state.guideError }}
        </p>

        <details class="lfs-dialog-details" *ngIf="state.failures.length > 0">
          <summary class="lfs-dialog-error">{{ t(keys.LFS_FAILED_PACKAGES) }}: {{ state.failures.length }}</summary>
          <ul>
            <li *ngFor="let failure of state.failures">
              <strong>{{ failure.packageName }}</strong>
              <pre>{{ failure.message }}</pre>
            </li>
          </ul>
        </details>
        <details class="lfs-dialog-details" *ngIf="state.skippedNodes.length > 0">
          <summary>{{ t(keys.LFS_SKIPPED_NODES) }}: {{ state.skippedNodes.length }}</summary>
          <p>{{ state.skippedNodes.join(', ') }}</p>
        </details>

        <div class="lfs-dialog-actions">
          <button
            class="lfs-dialog-button"
            type="button"
            *ngIf="isRunning"
            [disabled]="state.phase === 'writingGuide'"
            (click)="cancelRequested.emit()">
            {{ t(keys.LFS_CANCEL) }}
          </button>
          <button class="lfs-dialog-button" type="button" *ngIf="!isRunning" (click)="closeRequested.emit()">
            {{ t(keys.LFS_CLOSE) }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: `
    .lfs-dialog-backdrop {
      position: fixed;
      inset: 0;
      z-index: 2100;
      background: rgba(16, 42, 67, 0.28);
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .lfs-dialog {
      display: flex;
      flex-direction: column;
      gap: 10px;
      width: min(440px, calc(100vw - 32px));
      max-height: calc(100vh - 32px);
      overflow-y: auto;
      border: 1px solid #243b53;
      border-radius: 10px;
      background: #ffffff;
      color: #102a43;
      box-shadow: 0 16px 36px rgba(16, 42, 67, 0.25);
      padding: 18px 16px 16px;
    }
    .lfs-dialog-title {
      margin: 0;
      font-size: 1.05rem;
    }
    .lfs-dialog-line code {
      word-break: break-all;
    }

    .lfs-dialog-line {
      margin: 0;
      overflow-wrap: anywhere;
    }
    .lfs-dialog-count {
      font-variant-numeric: tabular-nums;
    }
    .lfs-dialog-error {
      color: #b42318;
    }
    .lfs-progress {
      position: relative;
      height: 14px;
      border: 1px solid #8aa1b8;
      border-radius: 7px;
      background: #e8edf2;
      overflow: hidden;
    }
    .lfs-progress-fill {
      height: 100%;
      background: #2f80ed;
      transition: width 0.2s ease;
    }
    .lfs-progress-indeterminate .lfs-progress-fill {
      width: 30%;
      animation: lfs-indeterminate 1.1s ease-in-out infinite;
    }
    @keyframes lfs-indeterminate {
      from { transform: translateX(-100%); }
      to { transform: translateX(340%); }
    }
    .lfs-dialog-details pre {
      margin: 2px 0 6px;
      white-space: pre-wrap;
      font-size: 0.78rem;
    }
    .lfs-dialog-details ul {
      margin: 4px 0;
      padding-left: 18px;
    }
    .lfs-dialog-actions {
      display: flex;
      justify-content: flex-end;
    }
    .lfs-dialog-button {
      min-width: 92px;
      min-height: 30px;
      border: 1px solid #8aa1b8;
      border-radius: 6px;
      background: #ffffff;
      color: #102a43;
      cursor: pointer;
    }
  `
})
export class LfsProgressDialogComponent {
  /** Progress to render; null hides the dialog. */
  @Input() public state: LfsDownloadProgressState | null = null;
  @Output() public readonly cancelRequested = new EventEmitter<void>();
  @Output() public readonly closeRequested = new EventEmitter<void>();

  protected readonly keys = I18N_KEYS.shell;

  constructor(
    private readonly i18nService: I18nService,
    private readonly i18nStateService: I18nStateService
  ) {}

  protected get isRunning(): boolean {
    return (
      this.state?.phase === 'planning' || this.state?.phase === 'downloading' || this.state?.phase === 'writingGuide'
    );
  }

  protected get percent(): number {
    if (!this.state || this.state.total === 0) {
      return this.state?.phase === 'finished' ? 100 : 0;
    }
    return Math.round((this.state.completed / this.state.total) * 100);
  }

  protected t(id: TranslationKey): string {
    return this.i18nService.get(id, this.i18nStateService.selectedLanguage());
  }
}
