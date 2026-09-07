import { Component, EventEmitter, Output } from "@angular/core";
import { FormsModule } from "@angular/forms";
@Component({
  selector: "app-staff-search-filter",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./staff-search-filter.component.html",
  styleUrl: "./staff-search-filter.component.scss",
})
export class StaffSearchFilterComponent {
  @Output() search = new EventEmitter<any>();
  @Output() reset = new EventEmitter<void>();
  keyword = "";
  status = "";
  submit() {
    this.search.emit({ keyword: this.keyword, status: this.status });
  }
  clear() {
    this.keyword = "";
    this.status = "";
    this.reset.emit();
  }
}
