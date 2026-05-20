import { GraphModel } from '../model/graph-model';

export class KeyboardInteractionTechniques {
  private metaOrCtrlPressed = false;
  private fullScreenTarget: HTMLElement | null = null;

  public constructor(private readonly graphModel: GraphModel) {
    void this.graphModel;
  }

  public attach(fullScreenTarget?: HTMLElement): void {
    this.fullScreenTarget = fullScreenTarget ?? null;
    window.addEventListener('keydown', this.onKeyDown);
    window.addEventListener('keyup', this.onKeyUp);
    window.addEventListener('blur', this.onWindowBlur);
  }

  public isMultiSelectModifierPressed(): boolean {
    return this.metaOrCtrlPressed;
  }

  private readonly onKeyDown = (event: KeyboardEvent): void => {
    if (event.key === 'Control' || event.key === 'Meta') {
      this.metaOrCtrlPressed = true;
      return;
    }

    if (event.key.toLowerCase() === 'f') {
      event.preventDefault();
      this.toggleFullScreen();
    }
  };

  private readonly onKeyUp = (event: KeyboardEvent): void => {
    if (event.key === 'Control' || event.key === 'Meta') {
      this.metaOrCtrlPressed = false;
    }
  };

  private readonly onWindowBlur = (): void => {
    this.metaOrCtrlPressed = false;
  };

  private toggleFullScreen(): void {
    if (!this.fullScreenTarget) {
      return;
    }

    if (document.fullscreenElement === this.fullScreenTarget) {
      void document.exitFullscreen();
      return;
    }

    if (document.fullscreenElement) {
      void document.exitFullscreen().finally(() => {
        void this.fullScreenTarget?.requestFullscreen();
      });
      return;
    }

    void this.fullScreenTarget.requestFullscreen();
  }
}
