import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, HostListener, Input, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Menu } from '../model/menu';
import { MenuOption } from '../model/menu-option';

@Component({
  selector: 'app-menu-renderer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div
      *ngIf="visible && menuStack.length > 0"
      #menuRoot
      class="popup-menu-root"
      [style.left.px]="positionX"
      [style.top.px]="positionY"
    >
      <div class="popup-menu-level" *ngFor="let levelMenu of menuStack; let level = index">
        <div class="popup-menu-search" *ngIf="levelOptions(level).length > 10">
          <span class="search-icon">&#128269;</span>
          <input
            type="text"
            [(ngModel)]="searchByLevel[level]"
            (ngModelChange)="onSearchChange(level)"
            [placeholder]="searchPlaceholder"
          />
        </div>

        <ul class="popup-menu-list" [class.scrollable]="filteredOptions(level).length > 10">
          <li
            *ngFor="let option of filteredOptions(level); let idx = index"
            [class.active]="isActive(level, idx)"
            (mouseenter)="setActive(level, idx)"
            (mouseleave)="clearActive(level)"
            (click)="onOptionClick(level, idx, $event)"
          >
            {{ option.label }}
          </li>
        </ul>
      </div>
    </div>
  `,
  styles: [
    `
      .popup-menu-root {
        position: fixed;
        z-index: 2200;
        display: flex;
        gap: 10px;
      }

      .popup-menu-level {
        min-width: 230px;
        max-width: 320px;
        border: 1px solid #243b53;
        border-radius: 8px;
        background: #ffffff;
        color: #102a43;
        font: 12px/1.3 monospace;
        box-shadow: 0 6px 20px rgba(16, 42, 67, 0.18);
        padding: 8px;
      }

      .popup-menu-search {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 4px 2px 8px;
      }

      .popup-menu-search input {
        width: 100%;
        border: 1px solid #bcccdc;
        border-radius: 5px;
        padding: 5px 7px;
        font: inherit;
      }

      .popup-menu-list {
        margin: 0;
        padding: 0;
        list-style: none;
      }

      .popup-menu-list.scrollable {
        max-height: 280px;
        overflow-y: auto;
      }

      .popup-menu-list li {
        padding: 6px 8px;
        border-radius: 5px;
        cursor: default;
      }

      .popup-menu-list li.active {
        background: #d9e2ec;
      }
    `
  ]
})
export class MenuRenderer {
  @ViewChild('menuRoot')
  private menuRootRef?: ElementRef<HTMLDivElement>;

  @Input() public visible = false;
  @Input() public positionX = 0;
  @Input() public positionY = 0;
  @Input() public searchPlaceholder = '';

  @Output() public closeRequested = new EventEmitter<void>();

  public menuStack: Menu[] = [];
  public activeByLevel: number[] = [];
  public searchByLevel: string[] = [];

  public open(menu: Menu, x: number, y: number): void {
    this.visible = true;
    this.positionX = x;
    this.positionY = y;
    this.menuStack = [menu];
    this.activeByLevel = [-1];
    this.searchByLevel = [''];
    queueMicrotask(() => this.clampMenuToViewport());
  }

  public close(): void {
    this.visible = false;
    this.menuStack = [];
    this.activeByLevel = [];
    this.searchByLevel = [];
  }

  public levelOptions(level: number): MenuOption[] {
    return this.menuStack[level]?.options ?? [];
  }

  public filteredOptions(level: number): MenuOption[] {
    const query = (this.searchByLevel[level] ?? '').trim().toLowerCase();
    const options = this.levelOptions(level);
    if (!query) {
      return options;
    }
    return options.filter((option) => option.label.toLowerCase().includes(query));
  }

  public setActive(level: number, index: number): void {
    this.activeByLevel[level] = index;
  }

  public clearActive(level: number): void {
    this.activeByLevel[level] = -1;
  }

  public isActive(level: number, index: number): boolean {
    return this.activeByLevel[level] === index;
  }

  public onOptionClick(level: number, index: number, event: MouseEvent): void {
    event.preventDefault();
    this.setActive(level, index);
    this.executeActive(level);
  }

  public onSearchChange(level: number): void {
    this.activeByLevel[level] = -1;
  }

  @HostListener('window:keydown', ['$event'])
  public onKeyDown(event: KeyboardEvent): void {
    if (!this.visible || this.menuStack.length === 0) {
      return;
    }

    if (event.key === 'Escape') {
      event.preventDefault();
      this.close();
      this.closeRequested.emit();
      return;
    }

    const level = this.menuStack.length - 1;
    const options = this.filteredOptions(level);

    if (event.key === 'Tab' || event.key === 'ArrowDown' || event.key === 'ArrowUp') {
      event.preventDefault();
      if (options.length === 0) {
        return;
      }
      const direction =
        event.key === 'ArrowUp'
          ? -1
          : event.key === 'ArrowDown'
            ? 1
            : event.shiftKey
              ? -1
              : 1;
      const current = this.activeByLevel[level];
      if (current < 0) {
        this.activeByLevel[level] = direction > 0 ? 0 : options.length - 1;
      } else {
        this.activeByLevel[level] = (current + direction + options.length) % options.length;
      }
      return;
    }

    if (event.key === 'Enter') {
      event.preventDefault();
      this.executeActive(level);
    }
  }

  private executeActive(level: number): void {
    const options = this.filteredOptions(level);
    const activeIndex = this.activeByLevel[level];
    if (activeIndex < 0 || activeIndex >= options.length) {
      return;
    }

    const option = options[activeIndex];
    console.log('Menu option activated:', option.id, { hasAction: !!option.action, hasSubmenu: !!option.submenu });
    if (option.action) {
      option.action();
      this.close();
      this.closeRequested.emit();
      return;
    }

    if (option.submenu && option.submenu.options.length > 0) {
      this.menuStack = this.menuStack.slice(0, level + 1);
      this.activeByLevel = this.activeByLevel.slice(0, level + 1);
      this.searchByLevel = this.searchByLevel.slice(0, level + 1);

      this.menuStack.push(option.submenu);
      this.activeByLevel.push(-1);
      this.searchByLevel.push('');
      console.log('Opening submenu for option:', option.id, 'with', option.submenu.options.length, 'options');
      queueMicrotask(() => this.clampMenuToViewport());
    }
  }

  private clampMenuToViewport(): void {
    const root = this.menuRootRef?.nativeElement;
    if (!root) {
      return;
    }

    const rect = root.getBoundingClientRect();
    const minMargin = 8;
    const maxLeft = window.innerWidth - rect.width - minMargin;
    const maxTop = window.innerHeight - rect.height - minMargin;

    this.positionX = Math.min(Math.max(minMargin, this.positionX), Math.max(minMargin, maxLeft));
    this.positionY = Math.min(Math.max(minMargin, this.positionY), Math.max(minMargin, maxTop));
  }
}
