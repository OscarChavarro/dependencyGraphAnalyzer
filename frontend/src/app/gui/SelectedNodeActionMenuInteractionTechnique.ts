import { GraphModel } from '../model/graph-model';
import { PopupMenuOption, PopupOptionsMenu } from './PopupOptionsMenu';

export class SelectedNodeActionMenuInteractionTechnique {
  private lastMouseX = 0;
  private lastMouseY = 0;
  private readonly popupOptionsMenu = new PopupOptionsMenu();

  public constructor(
    private readonly graphModel: GraphModel,
    private readonly isStructureGraphProvider: () => boolean,
    private readonly openGraphByFilename: (filename: string) => void,
    private readonly inundateDependencies: (selectedNodes: string[]) => void,
    private readonly inundateClients: (selectedNodes: string[]) => void,
    private readonly moveTo: (selectedNodes: string[]) => void
  ) {}

  public attach(): void {
    window.addEventListener('mousemove', this.onMouseMove);
    window.addEventListener('keydown', this.onKeyDown);
  }

  private readonly onMouseMove = (event: MouseEvent): void => {
    this.lastMouseX = event.clientX;
    this.lastMouseY = event.clientY;
  };

  private readonly onKeyDown = (event: KeyboardEvent): void => {
    if (event.key === 'Escape') {
      this.popupOptionsMenu.close();
      return;
    }

    if (this.popupOptionsMenu.isOpen()) {
      if (event.key === 'ArrowDown') {
        event.preventDefault();
        this.popupOptionsMenu.moveSelectionDown();
        return;
      }
      if (event.key === 'ArrowUp') {
        event.preventDefault();
        this.popupOptionsMenu.moveSelectionUp();
        return;
      }
      if (event.key === 'Enter') {
        event.preventDefault();
        if (this.popupOptionsMenu.runSelected()) {
          this.popupOptionsMenu.close();
        }
      }
      return;
    }

    if (event.key !== ' ') {
      return;
    }

    event.preventDefault();
    if (this.graphModel.selectedNodes.length === 0) {
      return;
    }
    this.openMenuAtCurrentMousePosition();
  };

  private openMenuAtCurrentMousePosition(): void {
    const options = this.buildActions();
    if (options.length === 0) {
      return;
    }
    this.popupOptionsMenu.openAt(this.lastMouseX, this.lastMouseY, options);
  }

  private buildActions(): PopupMenuOption[] {
    const structure = this.isStructureGraphProvider();
    if (!structure) {
      return [
        {
          label: 'inundar dependencias',
          onSelect: () => this.inundateDependencies([...this.graphModel.selectedNodes])
        },
        {
          label: 'inundar clientes',
          onSelect: () => this.inundateClients([...this.graphModel.selectedNodes])
        },
        {
          label: 'mover a...',
          onSelect: () => this.moveTo([...this.graphModel.selectedNodes])
        },
      ];
    }

    const selectedNodeName = this.graphModel.selectedNodes[0];
    const filename = this.mapNodeNameToSvgFilename(selectedNodeName);
    if (!filename) {
      return [];
    }

    return [
      {
        label: `openNewSvg (${filename})`,
        onSelect: () => {
          this.openGraphByFilename(filename);
        }
      }
    ];
  }

  private mapNodeNameToSvgFilename(nodeName: string | undefined): string | null {
    if (!nodeName) {
      return null;
    }

    const trimmed = nodeName.trim();
    if (trimmed === '[STRUCTURE]' || trimmed === '_[STRUCTURE]') {
      return 'structure.svg';
    }

    const match = trimmed.match(/^_\[(.+)\]$/);
    if (!match || !match[1]) {
      return null;
    }

    return `${match[1].toLowerCase()}.svg`;
  }
}
