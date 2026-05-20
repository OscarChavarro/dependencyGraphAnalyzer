import { Menu } from './menu';

export class MenuOption {
  public constructor(
    public readonly id: string,
    public readonly label: string,
    public readonly action: (() => void) | null,
    public readonly nestedMenuOption: MenuOption | null,
    public readonly submenu: Menu | null = null
  ) {}
}
