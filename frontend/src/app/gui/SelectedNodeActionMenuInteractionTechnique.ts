import { GraphModel } from '../model/graph-model';
import { InlineMenuDefinition } from '../model/inline-menu-definition';
import { Menu } from '../model/menu';
import { MenuOption } from '../model/menu-option';
import { MenuRenderer } from './menu-renderer';
import type { TranslationKey } from '../i18n/translations/translations-by-namespace.const';

export class SelectedNodeActionMenuInteractionTechnique {
  private lastMouseX = 0;
  private lastMouseY = 0;

  public constructor(
    private readonly graphModel: GraphModel,
    private readonly menuRenderer: MenuRenderer,
    private readonly pickNodeFromEvent: (event: MouseEvent) => string | null,
    private readonly onSelectionChanged: (selectedNodes: string[]) => void,
    private readonly isStructureGraphProvider: () => boolean,
    private readonly openGraphByFilename: (filename: string) => void,
    private readonly inundateDependencies: (selectedNodes: string[]) => void,
    private readonly inundateClients: (selectedNodes: string[]) => void,
    private readonly showRelation: (selectedNodes: string[]) => void,
    private readonly moveTo: (selectedNodes: string[], targetGroup: string) => void,
    private readonly listStructureGroups: () => string[],
    private readonly translate: (id: TranslationKey) => string
  ) {}

  public attach(): void {
    window.addEventListener('mousemove', this.onMouseMove);
    window.addEventListener('keydown', this.onKeyDown);
    window.addEventListener('contextmenu', this.onContextMenu);
  }

  public closeMenu(): void {
    this.menuRenderer.close();
  }

  private readonly onMouseMove = (event: MouseEvent): void => {
    this.lastMouseX = event.clientX;
    this.lastMouseY = event.clientY;
  };

  private readonly onKeyDown = (event: KeyboardEvent): void => {
    if (event.key === 'Escape') {
      this.menuRenderer.close();
      return;
    }

    if (this.menuRenderer.visible) {
      return;
    }

    if (event.key !== ' ') {
      return;
    }

    event.preventDefault();
    if (this.graphModel.selectedNodes.length === 0) {
      return;
    }

    const menu = this.buildMenuModel();
    if (menu.options.length === 0) {
      return;
    }

    this.menuRenderer.open(menu, this.lastMouseX, this.lastMouseY);
  };

  private readonly onContextMenu = (event: MouseEvent): void => {
    const hoveredNodeName = this.pickNodeFromEvent(event);
    if (!hoveredNodeName) {
      return;
    }
    event.preventDefault();

    if (event.ctrlKey || event.metaKey) {
      this.graphModel.toggleMultiSelection(hoveredNodeName);
    } else if (!this.graphModel.selectedNodes.includes(hoveredNodeName)) {
      this.graphModel.selectSingle(hoveredNodeName);
    }
    this.onSelectionChanged([...this.graphModel.selectedNodes]);

    this.lastMouseX = event.clientX;
    this.lastMouseY = event.clientY;

    const menu = this.buildMenuModel();
    if (menu.options.length === 0) {
      return;
    }
    this.menuRenderer.open(menu, this.lastMouseX, this.lastMouseY);
  };

  private buildMenuModel(): Menu {
    const structure = this.isStructureGraphProvider();

    const actionRegistry: Record<string, (() => void) | undefined> = {
      open_new_svg: () => {
        const selectedNodeName = this.graphModel.selectedNodes[0];
        const filename = this.mapNodeNameToSvgFilename(selectedNodeName);
        if (filename) {
          this.openGraphByFilename(filename);
        }
      },
      show_relation: () => this.showRelation([...this.graphModel.selectedNodes]),
      flood_dependencies: () => this.inundateDependencies([...this.graphModel.selectedNodes]),
      flood_clients: () => this.inundateClients([...this.graphModel.selectedNodes])
    };

    const moveToSubmenu = this.buildMoveToSubmenu();
    const dynamicSubmenus: Record<string, Menu> = {
      'shell.MENU_MOVE_TO': moveToSubmenu
    };

    const menu = InlineMenuDefinition.buildMenuFromJson(
      structure ? InlineMenuDefinition.STRUCTURE_MENU_JSON : InlineMenuDefinition.NON_STRUCTURE_MENU_JSON,
      actionRegistry,
      dynamicSubmenus,
      (id) => this.translate(id as TranslationKey)
    );
    if (!structure || this.graphModel.selectedNodes.length !== 2) {
      return new Menu(menu.options.filter((option) => option.id !== 'shell.MENU_SHOW_RELATION'));
    }
    return menu;
  }

  private buildMoveToSubmenu(): Menu {
    const groups = this.listStructureGroups();
    if (groups.length === 0) {
      return new Menu([
        new MenuOption(
          'shell.MENU_NO_GROUPS_AVAILABLE',
          this.translate('shell.MENU_NO_GROUPS_AVAILABLE'),
          null,
          null,
          null
        )
      ]);
    }
    const options = groups.map(
      (groupName) =>
        new MenuOption(
          groupName,
          groupName,
          () => this.moveTo([...this.graphModel.selectedNodes], groupName),
          null,
          null
        )
    );
    return new Menu(options);
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
