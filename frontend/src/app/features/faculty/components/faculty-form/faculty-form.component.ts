import { CommonModule } from "@angular/common";

import { Component, EventEmitter, Input, Output } from "@angular/core";

import { FormGroup, ReactiveFormsModule } from "@angular/forms";

import { MatDatepickerModule } from "@angular/material/datepicker";

import { MatNativeDateModule } from "@angular/material/core";

import { MATERIAL_MODULES } from "../../../../shared/material/material";

import { LocationSelectorComponent } from "../../../location/components/location-selector/location-selector.component";

import { Gender } from "../../../../shared/enums/gender.enum";
import { AddressType } from "../../../../shared/enums/address-type.enum";
import { Designation } from "../../../../shared/enums/designation.enum";
import { Qualification } from "../../../../shared/enums/qualification.enum";

@Component({
  selector: "app-faculty-form",

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,

    // =====================================================
    // MATERIAL DATEPICKER
    // =====================================================

    MatDatepickerModule,
    MatNativeDateModule,

    ...MATERIAL_MODULES,

    LocationSelectorComponent,
  ],

  templateUrl: "./faculty-form.component.html",
  styleUrl: "./faculty-form.component.scss",
})
export class FacultyFormComponent {
  // =====================================================
  // ENUMS
  // =====================================================

  readonly genders = Object.values(Gender);

  readonly designations = Object.values(Designation);

  readonly qualifications = Object.values(Qualification);

  readonly addressTypes = Object.values(AddressType);

  // =====================================================
  // INPUTS
  // =====================================================

  @Input({ required: true })
  form!: FormGroup;

  @Input()
  isEditMode = false;

  @Input()
  loading = false;

  @Input()
  departments: any[] = [];

  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  readonly cancel = new EventEmitter<void>();

  @Output()
  readonly save = new EventEmitter<void>();

  // =====================================================
  // ADDRESS FORM
  // =====================================================

  get addressForm(): FormGroup {
    return this.form.get("address") as FormGroup;
  }

  // =====================================================
  // SUBMIT
  // =====================================================

  submit(): void {
    if (this.loading) {
      return;
    }

    // ===================================================
    // VALIDATION
    // ===================================================

    if (this.form.invalid) {
      this.form.markAllAsTouched();

      return;
    }

    // ===================================================
    // NORMALIZE DATES
    // ===================================================

    this.normalizeDateControls();

    // ===================================================
    // EMIT SAVE
    // ===================================================

    this.save.emit();
  }

  // =====================================================
  // NORMALIZE DATE CONTROLS
  // =====================================================

  private normalizeDateControls(): void {
    const dateOfBirth = this.form.get("dateOfBirth");

    const joiningDate = this.form.get("joiningDate");

    // ===================================================
    // DATE OF BIRTH
    // ===================================================

    if (dateOfBirth) {
      const value = dateOfBirth.value;

      if (value instanceof Date) {
        dateOfBirth.setValue(this.formatDate(value), {
          emitEvent: false,
        });
      }
    }

    // ===================================================
    // JOINING DATE
    // ===================================================

    if (joiningDate) {
      const value = joiningDate.value;

      if (value instanceof Date) {
        joiningDate.setValue(this.formatDate(value), {
          emitEvent: false,
        });
      }
    }
  }

  // =====================================================
  // DATE → yyyy-MM-dd
  // =====================================================

  private formatDate(date: Date): string {
    const year = date.getFullYear();

    const month = String(date.getMonth() + 1).padStart(2, "0");

    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
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
}
