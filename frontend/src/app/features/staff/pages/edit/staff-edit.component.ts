import { Component, inject, signal } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { StaffService } from "../../services/staff.service";
import { Staff } from "../../models/staff.model";
import { StaffRequest } from "../../models/staff-request.model";
import { StaffFormComponent } from "../../components/staff-form.component";
@Component({
  standalone: true,
  imports: [StaffFormComponent],
  templateUrl: "./staff-edit.component.html",
  styleUrl: "./staff-edit.component.scss",
})
export class StaffEditComponent {
  private readonly service = inject(StaffService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  readonly model = signal<Partial<Staff>>({});
  readonly id = Number(this.route.snapshot.paramMap.get("id"));
  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.service.get(id).subscribe((r) => this.model.set(r));
  }
  save(value: Partial<Staff>) {
    const request = value as StaffRequest;
    const call = this.service.update(this.id, request);
    call.subscribe(() =>
      this.router.navigate(["../"], { relativeTo: this.route }),
    );
  }
  cancel() {
    this.router.navigate(["../"], { relativeTo: this.route });
  }
}
