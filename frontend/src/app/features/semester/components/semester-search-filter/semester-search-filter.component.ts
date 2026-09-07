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

import {
  SemesterSearchRequest
} from '../../models/semester.model';

import {
  RecordStatus
} from '../../../../shared/enums/record-status.enum';

@Component({
  selector: 'app-semester-search-filter',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './semester-search-filter.component.html',
  styleUrl: './semester-search-filter.component.scss'
})
export class SemesterSearchFilterComponent {

  private readonly fb = inject(FormBuilder);

  @Output()
  search =
    new EventEmitter<SemesterSearchRequest>();

  @Output()
  reset =
    new EventEmitter<void>();

  readonly recordStatuses = [
    RecordStatus.ACTIVE,
    RecordStatus.INACTIVE
  ];

  readonly form = this.fb.nonNullable.group({

    semesterId:
      this.fb.control<number | null>(null),

    semesterName:
      this.fb.control(''),

    semesterNumber:
      this.fb.control<number | null>(null),

    courseId:
      this.fb.control<number | null>(null),

    courseCode:
      this.fb.control(''),

    courseName:
      this.fb.control(''),

    startDateFrom:
      this.fb.control<string | null>(null),

    startDateTo:
      this.fb.control<string | null>(null),

    endDateFrom:
      this.fb.control<string | null>(null),

    endDateTo:
      this.fb.control<string | null>(null),

    minWorkingDays:
      this.fb.control<number | null>(null),

    maxWorkingDays:
      this.fb.control<number | null>(null),

    status:
      this.fb.control<RecordStatus | null>(null)

  });

  onSearch(): void {

    const value =
      this.form.getRawValue();

    const request: SemesterSearchRequest = {

      semesterId:
        value.semesterId,

      semesterName:
        value.semesterName?.trim() || null,

      semesterNumber:
        value.semesterNumber,

      courseId:
        value.courseId,

      courseCode:
        value.courseCode?.trim() || null,

      courseName:
        value.courseName?.trim() || null,

      startDateFrom:
        value.startDateFrom || null,

      startDateTo:
        value.startDateTo || null,

      endDateFrom:
        value.endDateFrom || null,

      endDateTo:
        value.endDateTo || null,

      minWorkingDays:
        value.minWorkingDays,

      maxWorkingDays:
        value.maxWorkingDays,

      status:
        value.status

    };

    this.search.emit(request);
  }

  onReset(): void {

    this.form.reset({

      semesterId: null,

      semesterName: '',

      semesterNumber: null,

      courseId: null,

      courseCode: '',

      courseName: '',

      startDateFrom: null,

      startDateTo: null,

      endDateFrom: null,

      endDateTo: null,

      minWorkingDays: null,

      maxWorkingDays: null,

      status: null

    });

    this.reset.emit();
  }
}