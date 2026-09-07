import { Injectable, signal } from '@angular/core';

export type UiTheme = 'light' | 'dark-classic' | 'classic';
export type UiFontScale = 'small' | 'normal' | 'large';

@Injectable({ providedIn: 'root' })
export class UiPreferencesService {
  readonly theme = signal<UiTheme>(this.readTheme());
  readonly fontScale = signal<UiFontScale>(this.readFontScale());

  constructor() { this.apply(); }

  setTheme(value: UiTheme): void {
    this.theme.set(value);
    localStorage.setItem('ums.theme', value);
    this.apply();
  }

  setFontScale(value: UiFontScale): void {
    this.fontScale.set(value);
    localStorage.setItem('ums.fontScale', value);
    this.apply();
  }

  increaseFont(): void { this.setFontScale(this.fontScale() === 'small' ? 'normal' : 'large'); }
  decreaseFont(): void { this.setFontScale(this.fontScale() === 'large' ? 'normal' : 'small'); }

  private apply(): void {
    const root = document.documentElement;
    root.dataset['theme'] = this.theme();
    root.dataset['fontScale'] = this.fontScale();
  }

  private readTheme(): UiTheme {
    const value = localStorage.getItem('ums.theme');
    return value === 'dark-classic' || value === 'classic' || value === 'light' ? value : 'light';
  }

  private readFontScale(): UiFontScale {
    const value = localStorage.getItem('ums.fontScale');
    return value === 'small' || value === 'large' || value === 'normal' ? value : 'normal';
  }
}
