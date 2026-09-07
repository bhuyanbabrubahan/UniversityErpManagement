import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule
} from '@angular/forms';

import {
  DepartmentSearchRequest
} from '../../models/department.model';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';

@Component({
  selector: 'app-department-search-filter',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './department-search-filter.component.html',
  styleUrl: './department-search-filter.component.scss'
})
export class DepartmentSearchFilterComponent {

  @Output()
  search =
    new EventEmitter<DepartmentSearchRequest>();

  @Output()
  reset =
    new EventEmitter<void>();

  searchForm: FormGroup;

  /**
   * Centralized status options.
   *
   * Template mein ACTIVE / INACTIVE / DELETED
   * hard-code nahi karne padenge.
   */
  readonly statusOptions: RecordStatus[] = [
    RecordStatus.ACTIVE,
    RecordStatus.INACTIVE,
    RecordStatus.DELETED
  ];

  constructor(
    private readonly fb: FormBuilder
  ) {

    this.searchForm =
      this.fb.group({

        departmentName: [''],

        departmentCode: [''],

        status: ['']

      });

  }

  // =====================================================
  // SEARCH
  // =====================================================

  onSearch(): void {

    const value =
      this.searchForm.getRawValue();

    const request: DepartmentSearchRequest = {

      departmentName:
        this.trimValue(
          value.departmentName
        ),

      departmentCode:
        this.trimValue(
          value.departmentCode
        ),

      status:
        value.status ||
        undefined

    };

    this.search.emit(request);

  }

  // =====================================================
  // RESET
  // =====================================================

  onReset(): void {

    this.searchForm.reset({

      departmentName: '',

      departmentCode: '',

      status: ''

    });

    this.reset.emit();

  }

  // =====================================================
  // TRIM
  // =====================================================

  private trimValue(
    value: string | null
  ): string | undefined {

    const trimmed =
      value?.trim();

    return trimmed
      ? trimmed
      : undefined;

  }

}