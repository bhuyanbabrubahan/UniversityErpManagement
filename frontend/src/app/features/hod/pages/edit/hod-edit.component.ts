import { Component, inject, OnInit, signal } from "@angular/core";

import { ActivatedRoute, Router } from "@angular/router";

import { HodService } from "../../services/hod.service";
import { Hod } from "../../models/hod.model";
import { HodRequest } from "../../models/hod-request.model";

import { HodFormComponent } from "../../components/hod-form.component";

import { ToastService } from "@app/shared/services/toast.service";

@Component({
  selector: "app-hod-edit",
  standalone: true,
  imports: [HodFormComponent],
  templateUrl: "./hod-edit.component.html",
  styleUrl: "./hod-edit.component.scss",
})
export class HodEditComponent implements OnInit {
  private readonly service = inject(HodService);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  private readonly toast = inject(ToastService);

  // =====================================================
  // MODEL
  // =====================================================

  readonly model = signal<Partial<Hod>>({});

  // =====================================================
  // LOADING
  // =====================================================

  readonly loading = signal(false);

  // =====================================================
  // SAVING
  // =====================================================

  readonly saving = signal(false);

  // =====================================================
  // ID
  // =====================================================

  readonly id = Number(this.route.snapshot.paramMap.get("id"));

  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {
    if (!Number.isInteger(this.id) || this.id <= 0) {
      this.toast.error("Invalid HOD ID.");

      this.cancel();

      return;
    }

    this.load();
  }

  // =====================================================
  // LOAD HOD
  // =====================================================

  load(): void {
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

        this.model.set(hod);

        this.loading.set(false);
      },

      error: (error) => {
        console.error("Failed to load HOD:", error);

        this.loading.set(false);

        this.toast.error(
          error?.error?.message || "Unable to load HOD details.",
        );

        this.cancel();
      },
    });
  }

  // =====================================================
  // SAVE / UPDATE
  // =====================================================

  save(value: Partial<Hod>): void {
    // ---------------------------------------------------
    // PREVENT DOUBLE SUBMIT
    // ---------------------------------------------------

    if (this.saving()) {
      return;
    }

    // ---------------------------------------------------
    // BUILD REQUEST DTO
    // ---------------------------------------------------
    /*
     * IMPORTANT:
     *
     * Do NOT send the complete Hod object.
     *
     * Hod contains response/audit fields:
     *
     * id
     * createdByUserId
     * createdBy
     * createdAt
     * updatedByUserId
     * updatedBy
     * updatedAt
     *
     * These fields are NOT part of HodRequestDto.
     *
     * Only explicitly allowed update fields are sent.
     */

    const request: HodRequest = {
      code: value.code?.trim() ?? "",

      employeeCode: value.employeeCode?.trim() ?? "",

      fullName: value.fullName?.trim() ?? "",

      department: value.department?.trim() ?? "",

      email: value.email?.trim() ?? "",

      phone: value.phone?.trim() ?? "",

      designation: value.designation?.trim() ?? "",

      status: value.status!,

      description: value.description?.trim() ?? "",
    };

    // ---------------------------------------------------
    // START SAVING
    // ---------------------------------------------------

    this.saving.set(true);

    // ---------------------------------------------------
    // UPDATE API
    // ---------------------------------------------------
    /*
     * ID goes through URL:
     *
     * PUT /api/erp/hod/{id}
     *
     * Example:
     *
     * PUT /api/erp/hod/1
     *
     * Request body contains NO id.
     */

    this.service.update(this.id, request).subscribe({
      // ------------------------------------------------
      // SUCCESS
      // ------------------------------------------------

      next: () => {
        this.saving.set(false);

        this.toast.success("HOD updated successfully.");

        this.router.navigate(["../../"], {
          relativeTo: this.route,
        });
      },

      // ------------------------------------------------
      // ERROR
      // ------------------------------------------------

      error: (error) => {
        console.error("Failed to update HOD:", error);

        this.saving.set(false);

        this.toast.error(error?.error?.message || "Failed to update HOD.");
      },
    });
  }

  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {
    this.router.navigate(["../../"], {
      relativeTo: this.route,
    });
  }
}
