import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { StaffService } from "../../services/staff.service";
import { Staff } from "../../models/staff.model";
@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./staff-view.component.html",
  styleUrl: "./staff-view.component.scss",
})
export class StaffViewComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly service = inject(StaffService);
  readonly item = signal<Staff | null>(null);
  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.service.get(id).subscribe((r) => this.item.set(r));
  }
}
