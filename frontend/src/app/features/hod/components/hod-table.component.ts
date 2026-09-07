import { Component, EventEmitter, Input, Output } from "@angular/core";

import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";

import { Hod } from "../models/hod.model";

@Component({
  selector: "app-hod-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./hod-table.component.html",
  styleUrl: "./hod-table.component.scss",
})
export class HodTableComponent {
  @Input() rows: Hod[] = [];

  @Input() deletingId: number | null = null;

  @Output() remove = new EventEmitter<number>();
}
