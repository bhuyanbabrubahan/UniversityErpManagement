import { Component, EventEmitter, Input, Output, inject } from "@angular/core";
import { FormsModule, NgForm } from "@angular/forms";

import { ToastService } from "@app/shared/services/toast.service";
import { RecordStatus } from "@app/shared/enums/record-status.enum";

import { Hod } from "../models/hod.model";

@Component({
  selector: "app-hod-form",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./hod-form.component.html",
  styleUrl: "./hod-form.component.scss",
})
export class HodFormComponent {
  private readonly toast = inject(ToastService);

  @Input()
  model: Partial<Hod> = {};

  @Input()
  submitLabel = "Save";

  @Input()
  loading = false;

  @Output()
  save = new EventEmitter<Partial<Hod>>();

  @Output()
  cancel = new EventEmitter<void>();

  submitted = false;

  /**
   * Status options available for normal HOD create/update.
   *
   * DELETED is intentionally excluded because it is
   * normally handled by the delete/restore flow.
   */
  readonly statuses: RecordStatus[] = [
    RecordStatus.ACTIVE,
    RecordStatus.INACTIVE,
  ];

  // =====================================================
  // SUBMIT
  // =====================================================

  onSubmit(form: NgForm): void {
    this.submitted = true;

    if (this.loading) {
      return;
    }

    if (form.invalid) {
      form.control.markAllAsTouched();

      this.toast.error("Please fill all required fields.");

      return;
    }

    const value: Partial<Hod> = {
      code: this.clean(this.model.code),
      employeeCode: this.clean(this.model.employeeCode),
      fullName: this.clean(this.model.fullName),
      department: this.clean(this.model.department),
      email: this.clean(this.model.email),
      phone: this.clean(this.model.phone),
      designation: this.clean(this.model.designation),
      status: this.clean(this.model.status) as RecordStatus,
      description: this.clean(this.model.description),
    };

    this.save.emit(value);
  }

  // =====================================================
  // CANCEL
  // =====================================================

  onCancel(): void {
    if (this.loading) {
      return;
    }

    this.cancel.emit();
  }

  // =====================================================
  // CLEAN VALUE
  // =====================================================

  private clean(value: unknown): string {
    if (value === null || value === undefined) {
      return "";
    }

    return String(value).trim();
  }
}
