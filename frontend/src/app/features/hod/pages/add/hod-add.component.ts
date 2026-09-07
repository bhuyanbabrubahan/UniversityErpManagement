import { Component, inject, signal } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";

import { HodService } from "../../services/hod.service";
import { Hod } from "../../models/hod.model";
import { HodRequest } from "../../models/hod-request.model";

import { HodFormComponent } from "../../components/hod-form.component";

import { ToastService } from "@app/shared/services/toast.service";

@Component({
  selector: "app-hod-add",
  standalone: true,
  imports: [HodFormComponent],
  templateUrl: "./hod-add.component.html",
  styleUrl: "./hod-add.component.scss",
})
export class HodAddComponent {
  private readonly service = inject(HodService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly toast = inject(ToastService);

  model = signal<Partial<Hod>>({
    status: "",
  });

  readonly saving = signal(false);

  save(value: Partial<Hod>): void {
    if (this.saving()) {
      return;
    }

    const request = value as HodRequest;

    this.saving.set(true);

    this.service.create(request).subscribe({
      next: () => {
        this.saving.set(false);

        this.toast.success("HOD created successfully.");

        this.router.navigate(["../"], { relativeTo: this.route });
      },

      error: (error) => {
        console.error("Failed to create HOD:", error);

        this.saving.set(false);

        this.toast.error(error?.error?.message || "Failed to create HOD.");
      },
    });
  }

  cancel(): void {
    this.router.navigate(["../"], { relativeTo: this.route });
  }
}
