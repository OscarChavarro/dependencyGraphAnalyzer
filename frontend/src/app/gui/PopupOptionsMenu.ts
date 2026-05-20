export interface PopupMenuOption {
  label: string;
  onSelect: () => void;
}

export class PopupOptionsMenu {
  private rootElement: HTMLDivElement | null = null;
  private options: PopupMenuOption[] = [];
  private activeIndex = -1;

  public openAt(x: number, y: number, options: PopupMenuOption[]): void {
    if (options.length === 0) {
      return;
    }

    this.options = options;
    this.activeIndex = -1;

    const root = this.getOrCreateRoot();
    root.style.display = 'block';
    root.style.left = `${x}px`;
    root.style.top = `${y}px`;

    this.render();
    const rect = root.getBoundingClientRect();
    const minMargin = 8;
    const clampedLeft = Math.min(Math.max(minMargin, x - rect.width / 2), window.innerWidth - rect.width - minMargin);
    const clampedTop = Math.min(Math.max(minMargin, y - rect.height / 2), window.innerHeight - rect.height - minMargin);
    root.style.left = `${Math.max(minMargin, clampedLeft)}px`;
    root.style.top = `${Math.max(minMargin, clampedTop)}px`;

  }

  public close(): void {
    if (!this.rootElement) {
      return;
    }
    this.rootElement.style.display = 'none';
  }

  public isOpen(): boolean {
    return !!this.rootElement && this.rootElement.style.display !== 'none';
  }

  public moveSelectionDown(): void {
    if (this.options.length === 0) {
      return;
    }
    if (this.activeIndex < 0) {
      this.activeIndex = 0;
    } else {
      this.activeIndex = (this.activeIndex + 1) % this.options.length;
    }
    this.render();
  }

  public moveSelectionUp(): void {
    if (this.options.length === 0) {
      return;
    }
    if (this.activeIndex < 0) {
      this.activeIndex = this.options.length - 1;
    } else {
      this.activeIndex = (this.activeIndex - 1 + this.options.length) % this.options.length;
    }
    this.render();
  }

  public runSelected(): boolean {
    if (this.activeIndex < 0 || this.activeIndex >= this.options.length) {
      return false;
    }
    this.options[this.activeIndex].onSelect();
    return true;
  }

  private getOrCreateRoot(): HTMLDivElement {
    if (this.rootElement) {
      return this.rootElement;
    }

    const menu = document.createElement('div');
    menu.style.position = 'fixed';
    menu.style.zIndex = '2000';
    menu.style.padding = '8px';
    menu.style.border = '1px solid #243b53';
    menu.style.borderRadius = '8px';
    menu.style.background = '#ffffff';
    menu.style.color = '#102a43';
    menu.style.font = '12px/1.3 monospace';
    menu.style.boxShadow = '0 6px 20px rgba(16, 42, 67, 0.18)';
    menu.style.minWidth = '220px';

    document.body.appendChild(menu);
    this.rootElement = menu;
    return menu;
  }

  private render(): void {
    if (!this.rootElement) {
      return;
    }

    this.rootElement.innerHTML = '';
    const list = document.createElement('ul');
    list.style.margin = '0';
    list.style.padding = '0';
    list.style.listStyle = 'none';

    this.options.forEach((option, index) => {
      const item = document.createElement('li');
      item.textContent = option.label;
      item.style.padding = '6px 8px';
      item.style.borderRadius = '5px';
      item.style.cursor = 'default';

      item.addEventListener('mouseenter', () => {
        this.activeIndex = index;
        this.render();
      });

      item.addEventListener('mouseleave', () => {
        this.activeIndex = -1;
        this.render();
      });

      item.addEventListener('click', (event) => {
        event.preventDefault();
        this.activeIndex = index;
        this.render();
        this.options[this.activeIndex]?.onSelect();
        this.close();
      });

      if (index === this.activeIndex) {
        item.style.background = '#d9e2ec';
      }

      list.appendChild(item);
    });

    this.rootElement.appendChild(list);
  }
}
