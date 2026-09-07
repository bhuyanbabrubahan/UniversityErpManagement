import { Component, EventEmitter, Output } from "@angular/core";

import { FormsModule } from "@angular/forms";

import { CurriculumSearch } from "../models/curriculum-search.model";

@Component({
  selector: "app-curriculum-search-filter",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./curriculum-search-filter.component.html",
  styleUrl: "./curriculum-search-filter.component.scss",
})
export class CurriculumSearchFilterComponent {
  @Output()
  search = new EventEmitter<CurriculumSearch>();

  @Output()
  reset = new EventEmitter<void>();

  keyword = "";

  status = "";

  submit(): void {
    this.search.emit({
      keyword: this.keyword.trim(),
      status: this.status,
    });
  }

  clear(): void {
    this.keyword = "";

    this.status = "";

    this.reset.emit();
  }
}
