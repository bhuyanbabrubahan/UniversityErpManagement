import { Component, inject, signal } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { StaffService } from "../../services/staff.service";
import { Staff } from "../../models/staff.model";
import { StaffRequest } from "../../models/staff-request.model";
import { StaffFormComponent } from "../../components/staff-form.component";
@Component({
  standalone: true,
  imports: [StaffFormComponent],
  templateUrl: "./staff-add.component.html",
  styleUrl: "./staff-add.component.scss",
})
export class StaffAddComponent {
  private readonly service = inject(StaffService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  readonly model = signal<Partial<Staff>>({});
  save(value: Partial<Staff>) {
    const request = value as StaffRequest;
    const call = this.service.create(request);
    call.subscribe(() =>
      this.router.navigate(["../"], { relativeTo: this.route }),
    );
  }
  cancel() {
    this.router.navigate(["../"], { relativeTo: this.route });
  }
}
