import { Component, DestroyRef, inject, signal } from "@angular/core";

import { BreakpointObserver } from "@angular/cdk/layout";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { RouterOutlet } from "@angular/router";

import { MATERIAL_MODULES } from "../../../shared/material/material";

import { NavbarComponent } from "../components/navbar/navbar.component";
import { SidebarComponent } from "../components/sidebar/sidebar.component";
import { FooterComponent } from "../components/footer/footer.component";

@Component({
  selector: "app-dashboard-layout",
  standalone: true,
  imports: [
    RouterOutlet,
    NavbarComponent,
    SidebarComponent,
    FooterComponent,
    ...MATERIAL_MODULES,
  ],
  templateUrl: "./dashboard-layout.component.html",
  styleUrl: "./dashboard-layout.component.scss",
})
export class DashboardLayoutComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly destroyRef = inject(DestroyRef);

  readonly isMobile = signal(false);
  readonly sidebarOpened = signal(true);

  constructor() {
    this.breakpointObserver
      .observe("(max-width: 768px)")
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(({ matches }) => {
        this.isMobile.set(matches);
        this.sidebarOpened.set(!matches);
      });
  }

  toggleSidebar(): void {
    this.sidebarOpened.update((isOpen) => !isOpen);
  }
}
