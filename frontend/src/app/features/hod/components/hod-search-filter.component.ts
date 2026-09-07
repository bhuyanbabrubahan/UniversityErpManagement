import { Component, EventEmitter, Output } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { HodSearch } from "../models/hod-search.model";

@Component({
  selector: "app-hod-search-filter",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./hod-search-filter.component.html",
  styleUrl: "./hod-search-filter.component.scss",
})
export class HodSearchFilterComponent {
  @Output() search = new EventEmitter<HodSearch>();

  @Output() reset = new EventEmitter<void>();

  keyword = "";
  status = "";
  code = "";

  submit(): void {
    this.search.emit({
      keyword: this.keyword.trim() || undefined,
      status: this.status || undefined,
      code: this.code.trim() || undefined,
    });
  }

  clear(): void {
    this.keyword = "";
    this.status = "";
    this.code = "";

    this.reset.emit();
  }
}
