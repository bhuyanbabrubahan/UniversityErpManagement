import {
  Component,
  DestroyRef,
  OnDestroy,
  OnInit,
  inject,
  output,
} from "@angular/core";

import { CommonModule } from "@angular/common";

import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

import { Router } from "@angular/router";

import { MatButtonModule } from "@angular/material/button";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { MatMenuModule } from "@angular/material/menu";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatDialog } from "@angular/material/dialog";

import { AuthService } from "../../../../features/auth/services/auth.service";
import { CurrentUserService } from "../../../../core/services/current-user.service";

import { ProfileResponse } from "../../../../features/profile/models/profile-response.model";

import { ChangePasswordDialogComponent } from "../../../../features/profile/components/change-password-dialog/change-password-dialog.component";

import { SIDEBAR_MENU } from "../../../../core/constants/sidebar-menu.constants";

import { NavigationItem } from "../../../../shared/models/navigation.model";

import { API } from "../../../../core/constants/api.constants";

@Component({
  selector: "app-navbar",
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    MatMenuModule,
    MatToolbarModule,
  ],

  templateUrl: "./navbar.component.html",
  styleUrl: "./navbar.component.scss",
})
export class NavbarComponent implements OnInit, OnDestroy {
  /* =======================================================
     OUTPUT
     ======================================================= */

  readonly menuClick = output<void>();

  /* =======================================================
     SERVICES
     ======================================================= */

  private readonly dialog = inject(MatDialog);

  private readonly auth = inject(AuthService);

  private readonly current = inject(CurrentUserService);

  readonly router = inject(Router);

  private readonly destroyRef = inject(DestroyRef);

  /* =======================================================
     STATE
     ======================================================= */

  profile?: ProfileResponse;

  currentDate = "";
  currentTime = "";

  notificationCount = 0;

  searchTerm = "";

  searchResults: NavigationItem[] = [];

  private timerId?: ReturnType<typeof setInterval>;

  private profileImageAvailable = true;

  /* =======================================================
     INIT
     ======================================================= */

  ngOnInit(): void {
    this.current.currentUser$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((user) => {
        this.profile = user ?? undefined;

        this.profileImageAvailable = !!this.profile?.profileImage;
      });

    this.updateDateTime();

    this.timerId = setInterval(() => this.updateDateTime(), 1000);
  }

  /* =======================================================
     DESTROY
     ======================================================= */

  ngOnDestroy(): void {
    if (this.timerId) {
      clearInterval(this.timerId);
    }
  }

  /* =======================================================
     USER
     ======================================================= */

  get displayName(): string {
    return this.profile?.fullName?.trim() || "User";
  }

  get displayRole(): string {
    return this.profile?.role?.trim() || "User";
  }

  get userInitials(): string {
    const name = this.displayName.split(/\s+/).filter(Boolean);

    if (!name.length) {
      return "U";
    }

    if (name.length === 1) {
      return name[0].slice(0, 2).toUpperCase();
    }

    return `${name[0][0]}${name[name.length - 1][0]}`.toUpperCase();
  }

  /* =======================================================
     PROFILE IMAGE
     ======================================================= */

  get profileImage(): string {
    if (!this.profile?.profileImage || !this.profileImageAvailable) {
      return "";
    }

    return `${API.PROFILE}/photo/${this.profile.profileImage}`;
  }

  onProfileImageError(): void {
    this.profileImageAvailable = false;
  }

  /* =======================================================
     DATE / TIME
     ======================================================= */

  private updateDateTime(): void {
    const now = new Date();

    this.currentDate = now.toLocaleDateString("en-IN", {
      weekday: "short",
      day: "2-digit",
      month: "short",
      year: "numeric",
    });

    this.currentTime = now.toLocaleTimeString("en-IN", {
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: true,
    });
  }

  /* =======================================================
     SIDEBAR
     ======================================================= */

  onMenuClick(): void {
    this.menuClick.emit();
  }

  /* =======================================================
     SEARCH
     ======================================================= */

  onSearchFocus(): void {
    if (!this.searchTerm.trim()) {
      this.searchResults = [];
    }
  }

  onSearch(value: string): void {
    this.searchTerm = value.trim().toLowerCase();

    if (!this.searchTerm) {
      this.searchResults = [];

      return;
    }

    const flattenMenu = (items: NavigationItem[]): NavigationItem[] => {
      return items.flatMap((item) => [
        item,

        ...(item.children ? flattenMenu(item.children) : []),
      ]);
    };

    const allItems = flattenMenu(SIDEBAR_MENU);

    const uniqueRoutes = new Set<string>();

    this.searchResults = allItems
      .filter((item) => {
        if (!item.route) {
          return false;
        }

        const label = item.label.toLowerCase();

        const route = item.route.toLowerCase();

        const matches =
          label.includes(this.searchTerm) || route.includes(this.searchTerm);

        if (!matches) {
          return false;
        }

        if (uniqueRoutes.has(item.route)) {
          return false;
        }

        uniqueRoutes.add(item.route);

        return true;
      })
      .slice(0, 8);
  }

  openResult(item: NavigationItem): void {
    if (!item.route) {
      return;
    }

    this.router.navigateByUrl(item.route).then(() => {
      this.clearSearch();
    });
  }

  clearSearch(): void {
    this.searchTerm = "";

    this.searchResults = [];
  }

  /* =======================================================
     CHANGE PASSWORD
     ======================================================= */

  openChangePasswordDialog(): void {
    this.dialog.open(ChangePasswordDialogComponent, {
      width: "100%",
      maxWidth: "440px",
      minWidth: "280px",
      maxHeight: "90vh",

      autoFocus: false,
      restoreFocus: false,

      disableClose: true,

      panelClass: "change-password-dialog",
    });
  }

  /* =======================================================
     LOGOUT
     ======================================================= */

  logout(): void {
    this.auth.logout();
  }
}
