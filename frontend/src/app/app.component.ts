import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CurrentUserService } from './core/services/current-user.service';
import { StorageService } from './core/services/storage.service';
import { ToastComponent } from './shared/components/toast/toast.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    ToastComponent
  ],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  // Shares logged-in user across application
  private readonly currentUserService =
    inject(CurrentUserService);

  // Reads authentication data from local storage
  private readonly storageService =
    inject(StorageService);

  // Initializes application
  ngOnInit(): void {

    this.initializeCurrentUser();

  }

  // Loads logged-in user if token exists
  private initializeCurrentUser(): void {

    const token =
      this.storageService.getAccessToken();

    if (!token) {

      return;

    }

    this.currentUserService.loadCurrentUser();

  }

}