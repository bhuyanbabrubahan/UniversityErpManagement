import { Component, inject, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ActivatedRoute, RouterLink } from "@angular/router";

import { HodService } from "../../services/hod.service";
import { Hod } from "../../models/hod.model";

import { ToastService } from "@app/shared/services/toast.service";

@Component({
  selector: "app-hod-view",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./hod-view.component.html",
  styleUrl: "./hod-view.component.scss",
})
export class HodViewComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly service = inject(HodService);
  private readonly toast = inject(ToastService);

  readonly item = signal<Hod | null>(null);
  readonly loading = signal(false);

  private readonly id = Number(this.route.snapshot.paramMap.get("id"));

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    if (!Number.isInteger(this.id) || this.id <= 0) {
      this.toast.error("Invalid HOD ID.");
      return;
    }

    this.loading.set(true);

    this.service.get(this.id).subscribe({
      next: (response: any) => {
        /*
         * Supports both:
         *
         * ApiResponseDto<Hod>
         * and direct Hod response.
         */
        const hod: Hod = response?.data ?? response;

        this.item.set(hod);
        this.loading.set(false);
      },

      error: (error) => {
        console.error("Failed to load HOD:", error);

        this.item.set(null);
        this.loading.set(false);

        this.toast.error(
          error?.error?.message || "Unable to load HOD details.",
        );
      },
    });
  }
}
