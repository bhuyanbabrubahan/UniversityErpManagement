import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  DepartmentRequest,
  DepartmentResponse,
  RecordStatus
} from '../../models/department.model';

@Component({
  selector: 'app-department-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './department-form.component.html',
  styleUrl: './department-form.component.scss'
})
export class DepartmentFormComponent implements OnChanges {

  @Input() department: DepartmentResponse | null = null;
  @Input() loading = false;
  @Input() submitLabel = 'Save Department';

  @Output() formSubmit =
    new EventEmitter<DepartmentRequest>();

  @Output() cancel =
    new EventEmitter<void>();

  departmentForm: FormGroup;
  description: any;


  constructor(
    private readonly fb: FormBuilder
  ) {
    this.departmentForm = this.fb.group({

      departmentName: ['', [Validators.required, Validators.maxLength(100)]],
      departmentCode: ['', [Validators.required, Validators.maxLength(30)]],
      description: ['', [Validators.maxLength(500)]],
      status: ['ACTIVE', Validators.required]

    });
  }

  ngOnChanges(changes: SimpleChanges): void {

    if (
      changes['department'] &&
      this.department
    ) {

      this.departmentForm.patchValue({

        departmentName:
          this.department.departmentName ?? '',

        departmentCode:
          this.department.departmentCode ?? '',
        description:
          this.department.description ?? '',

        status:
          this.department.status ?? 'ACTIVE'

      });

    }

  }

  get departmentName() {
    return this.departmentForm.get('departmentName');
  }

  get departmentCode() {
    return this.departmentForm.get('departmentCode');
  }

  get status() {
    return this.departmentForm.get('status');
  }

  isInvalid(controlName: string): boolean {

    const control =
      this.departmentForm.get(controlName);

    return !!control &&
      control.invalid &&
      (
        control.dirty ||
        control.touched
      );
  }

  onSubmit(): void {

    if (this.loading) {
      return;
    }

    if (this.departmentForm.invalid) {

      this.departmentForm.markAllAsTouched();

      return;
    }

    const raw =
      this.departmentForm.getRawValue();

    const request: DepartmentRequest = {

      departmentName:
        String(raw.departmentName)
          .trim(),

      departmentCode:
        String(raw.departmentCode)
          .trim()
          .toUpperCase(),
      description:
        String(raw.description)
          .trim(),


      status:
        raw.status as RecordStatus

    };

    this.formSubmit.emit(request);
  }

  onCancel(): void {

    if (this.loading) {
      return;
    }

    this.cancel.emit();
  }

}