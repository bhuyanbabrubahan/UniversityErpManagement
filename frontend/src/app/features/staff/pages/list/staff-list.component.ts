import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { StaffService } from "../../services/staff.service";
import { Staff } from "../../models/staff.model";
import { StaffSearchFilterComponent } from "../../components/staff-search-filter.component";
import { StaffTableComponent } from "../../components/staff-table.component";
@Component({
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    StaffSearchFilterComponent,
    StaffTableComponent,
  ],
  templateUrl: "./staff-list.component.html",
  styleUrl: "./staff-list.component.scss",
})
export class StaffListComponent {
  private readonly service = inject(StaffService);
  readonly rows = signal<Staff[]>([]);
  loading = signal(false);
  ngOnInit() {
    this.load();
  }
  load(search?: any) {
    this.loading.set(true);
    const call = search ? this.service.search(search) : this.service.list();
    call.subscribe({
      next: (r) => {
        this.rows.set(r.content ?? r);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
  remove(id: number) {
    if (confirm("Delete this record?"))
      this.service.delete(id).subscribe(() => this.load());
  }
}
