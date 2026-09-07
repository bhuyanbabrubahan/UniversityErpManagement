import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { RouterLink } from "@angular/router";

import { Timetable } from "../models/timetable.model";

@Component({
  selector: "app-timetable-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./timetable-table.component.html",
  styleUrl: "./timetable-table.component.scss",
})
export class TimetableTableComponent {
  @Input() rows: Timetable[] = [];

  @Input() loading = false;

  @Output() remove = new EventEmitter<number>();

  delete(row: Timetable): void {
    if (!Number.isInteger(row.id) || row.id <= 0) {
      console.error("Invalid timetable ID:", row.id);
      return;
    }

    this.remove.emit(row.id);
  }

  trackById(index: number, row: Timetable): number {
    return row.id;
  }
}
