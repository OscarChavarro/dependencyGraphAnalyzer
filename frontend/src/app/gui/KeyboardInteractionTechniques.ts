import { GraphModel } from '../model/graph-model';

export class KeyboardInteractionTechniques {
  private fullScreenTarget: HTMLElement | null = null;

  public constructor(private readonly graphModel: GraphModel) {
    void this.graphModel;
  }

  public attach(fullScreenTarget?: HTMLElement): void {
    this.fullScreenTarget = fullScreenTarget ?? null;
    window.addEventListener('keydown', this.onKeyDown);
  }

  private readonly onKeyDown = (event: KeyboardEvent): void => {
    if (event.key.toLowerCase() === 'f') {
      event.preventDefault();
      this.toggleFullScreen();
    }
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
