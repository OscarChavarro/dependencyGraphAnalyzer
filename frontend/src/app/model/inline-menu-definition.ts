import { Menu } from './menu';
import { MenuOption } from './menu-option';

interface MenuOptionJson {
  text: string;
  actionKey?: string;
  submenu?: MenuOptionJson[];
}

interface MenuJson {
  options: MenuOptionJson[];
}

export class InlineMenuDefinition {
  public static readonly STRUCTURE_MENU_JSON: MenuJson = {
    options: [
      { text: 'openNewSvg', actionKey: 'open_new_svg' }
    ]
  };

  public static readonly NON_STRUCTURE_MENU_JSON: MenuJson = {
    options: [
      { text: 'inundar dependencias', actionKey: 'flood_dependencies' },
      { text: 'inundar clientes', actionKey: 'flood_clients' },
      { text: 'mover a...', submenu: [] }
    ]
  };

  public static buildMenuFromJson(
    json: MenuJson,
    actionRegistry: Record<string, (() => void) | undefined>,
    dynamicSubmenus: Record<string, Menu>
  ): Menu {
    const options = json.options.map((optionJson) => this.buildOption(optionJson, actionRegistry, dynamicSubmenus));
    return new Menu(options);
  }

  private static buildOption(
    optionJson: MenuOptionJson,
    actionRegistry: Record<string, (() => void) | undefined>,
    dynamicSubmenus: Record<string, Menu>
  ): MenuOption {
    const action = optionJson.actionKey ? actionRegistry[optionJson.actionKey] ?? null : null;

    let submenu: Menu | null = null;
    if (optionJson.submenu && optionJson.submenu.length > 0) {
      submenu = new Menu(optionJson.submenu.map((child) => this.buildOption(child, actionRegistry, dynamicSubmenus)));
    } else if (dynamicSubmenus[optionJson.text]) {
      submenu = dynamicSubmenus[optionJson.text];
    }

    const nestedMenuOption = submenu && submenu.options.length > 0 ? submenu.options[0] : null;
    return new MenuOption(optionJson.text, action, nestedMenuOption, submenu);
  }
}
