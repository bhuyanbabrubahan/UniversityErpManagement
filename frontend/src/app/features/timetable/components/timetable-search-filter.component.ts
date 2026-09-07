import { Component, EventEmitter, Output } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { TimetableSearch } from "../models/timetable-search.model";
import { TIMETABLE_STATUS_OPTIONS } from "../shared/timetable.constants";

@Component({
  selector: "app-timetable-search-filter",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./timetable-search-filter.component.html",
  styleUrl: "./timetable-search-filter.component.scss",
})
export class TimetableSearchFilterComponent {
  @Output() search = new EventEmitter<TimetableSearch>();
  @Output() reset = new EventEmitter<void>();

  keyword = "";
  code = "";
  status = "";

  readonly statusOptions = TIMETABLE_STATUS_OPTIONS;

  submit(): void {
    const request: TimetableSearch = {
      keyword: this.keyword.trim() || undefined,
      code: this.code.trim() || undefined,
      status: this.status || undefined,
    };

    this.search.emit(request);
  }

  clear(): void {
    // Clear all filter values first
    this.keyword = "";
    this.code = "";
    this.status = "";

    // Tell parent to reload the complete list
    this.reset.emit();
  }
}
