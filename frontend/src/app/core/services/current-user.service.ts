import { Injectable, inject } from "@angular/core";

import { BehaviorSubject } from "rxjs";

import { ProfileService } from "../../features/profile/services/profile.service";
import { ProfileResponse } from "../../features/profile/models/profile-response.model";

@Injectable({
  providedIn: "root",
})
export class CurrentUserService {
  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly profileService = inject(ProfileService);

  // =====================================================
  // CURRENT USER STATE
  // =====================================================

  private readonly currentUserSubject =
    new BehaviorSubject<ProfileResponse | null>(null);

  readonly currentUser$ = this.currentUserSubject.asObservable();

  // LOAD CURRENT USER

  loadCurrentUser(): void {
    this.profileService.getProfile().subscribe({
      next: (response) => {
        if (!response?.data) {
          this.currentUserSubject.next(null);

          return;
        }

        const user = response.data;

        this.currentUserSubject.next(user);
      },

      error: (error) => {
        console.error("CURRENT USER → Profile API failed:", error);

        this.currentUserSubject.next(null);
      },
    });
  }

  // GET CURRENT USER

  getCurrentUser(): ProfileResponse | null {
    return this.currentUserSubject.value;
  }

  // GET CURRENT ROLE

  getCurrentRole(): string | null {
    return this.currentUserSubject.value?.role ?? null;
  }

  // SET CURRENT USER
  setCurrentUser(user: ProfileResponse): void {
    this.currentUserSubject.next(user);
  }

  // CLEAR CURRENT USER
  clearCurrentUser(): void {
    this.currentUserSubject.next(null);
  }
}
