import {
  Component,
  EventEmitter,
  Output,
  inject
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule
} from '@angular/forms';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { FacultySearchRequest } from '../../model/faculty-search-request.model';

import { Designation } from '../../../../shared/enums/designation.enum';
import { Qualification } from '../../../../shared/enums/qualification.enum';
import { RecordStatus } from '../../../../shared/enums/record-status.enum';

@Component({
  selector: 'app-faculty-search-filter',
  standalone: true,

  imports: [
    ReactiveFormsModule,
    ...MATERIAL_MODULES
  ],

  templateUrl: './faculty-search-filter.component.html',
  styleUrl: './faculty-search-filter.component.scss'
})
export class FacultySearchFilterComponent {

  private readonly fb = inject(FormBuilder);

  @Output()
  search = new EventEmitter<FacultySearchRequest>();

  @Output()
  reset = new EventEmitter<void>();


  // =====================================================
  // ENUMS
  // =====================================================

  readonly designations = Object.values(Designation);

  readonly qualifications = Object.values(Qualification);

  readonly statuses: RecordStatus[] = [
    RecordStatus.ACTIVE,
    RecordStatus.INACTIVE
  ];


  // =====================================================
  // FORM
  // =====================================================

  readonly form = this.fb.nonNullable.group({

    id: [''],

    employeeCode: [''],

    firstName: [''],

    lastName: [''],

    fullName: [''],

    departmentId: [''],

    designation: [''],

    qualification: [''],

    status: ['']

  });


  // =====================================================
  // SEARCH
  // =====================================================

  onSearch(): void {

    const value = this.form.getRawValue();

    const request: FacultySearchRequest = {};

    if (value.id.trim()) {
      request.id = Number(value.id);
    }

    if (value.employeeCode.trim()) {
      request.employeeCode =
        value.employeeCode.trim();
    }

    if (value.firstName.trim()) {
      request.firstName =
        value.firstName.trim();
    }

    if (value.lastName.trim()) {
      request.lastName =
        value.lastName.trim();
    }

    if (value.fullName.trim()) {
      request.fullName =
        value.fullName.trim();
    }

    if (value.departmentId.trim()) {
      request.departmentId =
        Number(value.departmentId);
    }

    if (value.designation) {
      request.designation =
        value.designation as Designation;
    }

    if (value.qualification) {
      request.qualification =
        value.qualification as Qualification;
    }

    if (value.status) {
      request.status =
        value.status as RecordStatus;
    }

    this.search.emit(request);
  }


  // =====================================================
  // RESET
  // =====================================================

  onReset(): void {

    this.form.reset();

    this.reset.emit();
  }
}