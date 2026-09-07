import { Injectable } from '@angular/core';

import { STORAGE_KEYS } from '../constants/storage.constants';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  set(key: string, value: string): void {
    localStorage.setItem(key, value);
  }

  get(key: string): string | null {
    return localStorage.getItem(key);
  }

  remove(key: string): void {
    localStorage.removeItem(key);
  }

  clear(): void {

  console.log("STORAGE CLEAR CALLED");

  console.trace();

  Object.values(STORAGE_KEYS)
    .forEach(key => {

      console.log("REMOVING :", key);

      localStorage.removeItem(key);

    });

}

  // -----------------------------
  // Authentication Methods
  // -----------------------------

  saveAccessToken(token: string): void {
    this.set(STORAGE_KEYS.ACCESS_TOKEN, token);
  }

  getAccessToken(): string | null {
    return this.get(STORAGE_KEYS.ACCESS_TOKEN);
  }

  saveRefreshToken(token: string): void {
    this.set(STORAGE_KEYS.REFRESH_TOKEN, token);
  }

  getRefreshToken(): string | null {
    return this.get(STORAGE_KEYS.REFRESH_TOKEN);
  }

  saveUserEmail(email: string): void {
    this.set(STORAGE_KEYS.USER_EMAIL, email);
  }

  getUserEmail(): string | null {
    return this.get(STORAGE_KEYS.USER_EMAIL);
  }

  saveUserRole(role: string): void {
    this.set(STORAGE_KEYS.USER_ROLE, role);
  }

  getUserRole(): string | null {
    return this.get(STORAGE_KEYS.USER_ROLE);
  }

}