import { Menu } from './menu';
import { MenuOption } from './menu-option';

interface MenuOptionJson {
  id: string;
  actionKey?: string;
  submenu?: MenuOptionJson[];
}

interface MenuJson {
  options: MenuOptionJson[];
}

export class InlineMenuDefinition {
  public static readonly STRUCTURE_MENU_JSON: MenuJson = {
    options: [
      { id: 'shell.MENU_OPEN_NEW_SVG', actionKey: 'open_new_svg' },
      { id: 'shell.MENU_SHOW_RELATION', actionKey: 'show_relation' },
      { id: 'shell.MENU_SET_BACKGROUND_COLOR', actionKey: 'set_background_color' }
    ]
  };

  public static readonly NON_STRUCTURE_MENU_JSON: MenuJson = {
    options: [
      { id: 'shell.MENU_FLOOD_DEPENDENCIES', actionKey: 'flood_dependencies' },
      { id: 'shell.MENU_FLOOD_CLIENTS', actionKey: 'flood_clients' },
      { id: 'shell.MENU_SET_BACKGROUND_COLOR', actionKey: 'set_background_color' },
      { id: 'shell.MENU_MOVE_TO', submenu: [] }
    ]
  };

  public static buildMenuFromJson(
    json: MenuJson,
    actionRegistry: Record<string, (() => void) | undefined>,
    dynamicSubmenus: Record<string, Menu>,
    translate: (id: string) => string
  ): Menu {
    const options = json.options.map((optionJson) =>
      this.buildOption(optionJson, actionRegistry, dynamicSubmenus, translate)
    );
    return new Menu(options);
  }

  private static buildOption(
    optionJson: MenuOptionJson,
    actionRegistry: Record<string, (() => void) | undefined>,
    dynamicSubmenus: Record<string, Menu>,
    translate: (id: string) => string
  ): MenuOption {
    const action = optionJson.actionKey ? actionRegistry[optionJson.actionKey] ?? null : null;

    let submenu: Menu | null = null;
    if (optionJson.submenu && optionJson.submenu.length > 0) {
      submenu = new Menu(
        optionJson.submenu.map((child) => this.buildOption(child, actionRegistry, dynamicSubmenus, translate))
      );
    } else if (dynamicSubmenus[optionJson.id]) {
      submenu = dynamicSubmenus[optionJson.id];
    }

    const nestedMenuOption = submenu && submenu.options.length > 0 ? submenu.options[0] : null;
    return new MenuOption(optionJson.id, translate(optionJson.id), action, nestedMenuOption, submenu);
  }
}
