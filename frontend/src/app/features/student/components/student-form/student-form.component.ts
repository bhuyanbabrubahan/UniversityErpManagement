import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { FormGroup, ReactiveFormsModule } from "@angular/forms";

import { MATERIAL_MODULES } from "../../../../shared/material/material";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatNativeDateModule } from "@angular/material/core";

import { LocationSelectorComponent } from "../../../location/components/location-selector/location-selector.component";
import { Gender } from "../../../../shared/enums/gender.enum";
import { StudentCategory } from "../../../../shared/enums/student-category.enum";
import { BloodGroup } from "../../../../shared/enums/blood-group.enum";
import { StudentType } from "../../../../shared/enums/student-type.enum";
import { StudentStatus } from "../../../../shared/enums/student-status.enum";
import { AddressType } from "../../../../shared/enums/address-type.enum";
import { Designation } from "../../../../shared/enums/designation.enum";
import { Qualification } from "../../../../shared/enums/qualification.enum";

@Component({
  selector: "app-student-form",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES,
    MatDatepickerModule,
    MatNativeDateModule,
    LocationSelectorComponent,
  ],
  templateUrl: "./student-form.component.html",
  styleUrl: "./student-form.component.scss",
})
export class StudentFormComponent {
  readonly genders = Object.values(Gender);
  readonly studentCategories = Object.values(StudentCategory);
  readonly bloodGroups = Object.values(BloodGroup);
  readonly studentTypes = Object.values(StudentType);
  readonly studentStatuses = Object.values(StudentStatus);
  readonly addressTypes = Object.values(AddressType);
  readonly designations = Object.values(Designation);
  readonly qualifications = Object.values(Qualification);

  @Input({ required: true })
  form!: FormGroup;

  @Input()
  isEditMode = false;

  @Input()
  loading = false;

  @Input()
  departments: any[] = [];

  @Output()
  cancel = new EventEmitter<void>();

  @Output()
  save = new EventEmitter<void>();

  get addressForm(): FormGroup {
    return this.form.get("address") as FormGroup;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.save.emit();
  }
}
