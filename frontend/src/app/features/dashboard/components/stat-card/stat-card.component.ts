import { Component, input } from '@angular/core';
import { DecimalPipe } from '@angular/common';

import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';

import { DashboardStat } from '../../models/dashboard-stat.model';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [
    DecimalPipe,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule
  ],
  templateUrl: './stat-card.component.html',
  styleUrl: './stat-card.component.scss'
})
export class StatCardComponent {

  readonly stat = input.required<DashboardStat>();

}