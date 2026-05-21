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
    if (event.key.toLowerCase() !== 'f') {
      return;
    }
    if (event.shiftKey || event.ctrlKey || event.metaKey || event.altKey) {
      return;
    }
    if (this.isTypingContext(event.target)) {
      return;
    }
    event.preventDefault();
    this.toggleFullScreen();
  };

  private isTypingContext(target: EventTarget | null): boolean {
    if (!(target instanceof HTMLElement)) {
      return false;
    }
    if (target.isContentEditable) {
      return true;
    }
    return target instanceof HTMLInputElement || target instanceof HTMLTextAreaElement;
  }

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
