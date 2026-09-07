import { Component, computed, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { ProfileResponse } from '@app/features/profile/models/profile-response.model';



@Component({
  selector: 'app-profile-card',
  standalone: true,
  imports: [
    MatCardModule,
    MatDividerModule,
    MatIconModule
  ],
  templateUrl: './profile-card.component.html',
  styleUrl: './profile-card.component.scss'
})
export class ProfileCardComponent {

  readonly profile = input<ProfileResponse | null>(null);

  readonly initials = computed(() => {
    const name = this.profile()?.fullName?.trim();

    if (!name) {
      return 'U';
    }

    return name
      .split(/\s+/)
      .slice(0, 2)
      .map(part => part.charAt(0).toUpperCase())
      .join('');
  });

}