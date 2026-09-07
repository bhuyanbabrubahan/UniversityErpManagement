import { Component, DestroyRef, OnInit, inject } from "@angular/core";

import { Router, RouterLink, RouterLinkActive } from "@angular/router";

import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

import { MatIconModule } from "@angular/material/icon";

import { MatListModule } from "@angular/material/list";

import { NavigationItem } from "../../../../shared/models/navigation.model";

import { CurrentUserService } from "../../../../core/services/current-user.service";

import { SIDEBAR_MENU } from "../../../../core/constants/sidebar-menu.constants";

import { Role } from "../../../../shared/enums/role.enum";

import { output } from "@angular/core";

@Component({
  selector: "app-sidebar",

  standalone: true,

  imports: [RouterLink, RouterLinkActive, MatIconModule, MatListModule],

  templateUrl: "./sidebar.component.html",

  styleUrl: "./sidebar.component.scss",
})
export class SidebarComponent implements OnInit {
  // =====================================================
  // OUTPUT
  // =====================================================

  readonly navigationClick = output<void>();

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly currentUserService = inject(CurrentUserService);

  private readonly destroyRef = inject(DestroyRef);

  private readonly router = inject(Router);

  // =====================================================
  // STATE
  // =====================================================

  menus: NavigationItem[] = [];

  currentRole: Role | null = null;

  /**
   * Stores expanded state by menu label.
   */
  expandedMenus = new Set<string>();

  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {
    this.currentUserService.currentUser$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((user) => {
        // -----------------------------------------------
        // No logged-in user
        // -----------------------------------------------

        if (!user) {
          this.currentRole = null;

          this.menus = [];

          this.expandedMenus.clear();

          return;
        }

        // -----------------------------------------------
        // Current role
        // -----------------------------------------------

        const userRole = user.role as Role;

        this.currentRole = userRole;

        // -----------------------------------------------
        // Role-based menu filtering
        // -----------------------------------------------

        this.menus = SIDEBAR_MENU.map((menu) =>
          this.filterMenuByRole(menu, userRole),
        ).filter((menu): menu is NavigationItem => menu !== null);

        // -----------------------------------------------
        // Automatically open active parent menus
        // -----------------------------------------------

        this.expandActiveParents(this.menus);
      });
  }

  // =====================================================
  // ROLE FILTER
  // =====================================================

  private filterMenuByRole(
    menu: NavigationItem,
    role: Role,
  ): NavigationItem | null {
    const allowed = menu.roles.includes(role);

    // ---------------------------------------------------
    // Filter children recursively
    // ---------------------------------------------------

    const children = menu.children
      ?.map((child) => this.filterMenuByRole(child, role))
      .filter((child): child is NavigationItem => child !== null);

    // ---------------------------------------------------
    // Menu visible if:
    //
    // 1. Current role has permission
    // OR
    // 2. At least one child is accessible
    // ---------------------------------------------------

    if (!allowed && (!children || children.length === 0)) {
      return null;
    }

    return {
      ...menu,

      children: children && children.length > 0 ? children : undefined,
    };
  }

  // =====================================================
  // EXPAND / COLLAPSE
  // =====================================================

  toggleMenu(menu: NavigationItem): void {
    if (!this.hasChildren(menu)) {
      return;
    }

    if (this.isExpanded(menu)) {
      this.expandedMenus.delete(this.getMenuKey(menu));
    } else {
      this.expandedMenus.add(this.getMenuKey(menu));
    }
  }

  // =====================================================
  // IS EXPANDED
  // =====================================================

  isExpanded(menu: NavigationItem): boolean {
    return this.expandedMenus.has(this.getMenuKey(menu));
  }

  // =====================================================
  // HAS CHILDREN
  // =====================================================

  hasChildren(menu: NavigationItem): boolean {
    return !!(menu.children && menu.children.length > 0);
  }

  // =====================================================
  // MENU KEY
  // =====================================================

  private getMenuKey(menu: NavigationItem): string {
    return menu.route ?? `parent:${menu.label}`;
  }

  // =====================================================
  // ACTIVE ROUTE
  // =====================================================

  isMenuActive(menu: NavigationItem): boolean {
    if (!menu.route) {
      return false;
    }

    return this.router.isActive(menu.route, {
      paths: "subset",
      queryParams: "ignored",
      fragment: "ignored",
      matrixParams: "ignored",
    });
  }

  // =====================================================
  // ACTIVE PARENT AUTO EXPAND
  // =====================================================

  private expandActiveParents(menus: NavigationItem[]): void {
    for (const menu of menus) {
      if (this.hasActiveChild(menu)) {
        this.expandedMenus.add(this.getMenuKey(menu));
      }
    }
  }

  // =====================================================
  // HAS ACTIVE CHILD
  // =====================================================

  hasActiveChild(menu: NavigationItem): boolean {
    if (!this.hasChildren(menu)) {
      return false;
    }

    for (const child of menu.children!) {
      if (this.isMenuActive(child) || this.hasActiveChild(child)) {
        return true;
      }
    }

    return false;
  }

  // =====================================================
  // NAVIGATION CLICK
  // =====================================================

  onNavigation(): void {
    this.navigationClick.emit();
  }

  // =====================================================
  // PARENT CLICK
  // =====================================================

  onParentClick(menu: NavigationItem): void {
    this.toggleMenu(menu);
  }

  // =====================================================
  // TRACK MENU
  // =====================================================

  trackMenu(_index: number, menu: NavigationItem): string {
    return this.getMenuKey(menu);
  }
}
