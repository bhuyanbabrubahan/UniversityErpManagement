import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  inject
} from '@angular/core';

import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule
} from '@angular/forms';

import {
  DocumentType,
  StudentDocumentSearchRequest
} from '../../models/student-document.model';

import { ToastService } from '../../../../shared/services/toast.service';

type SearchFilterForm = {
  id: FormControl<number | null>;
  studentId: FormControl<number | null>;
  documentType: FormControl<DocumentType | null>;
  originalFileName: FormControl<string>;
  storedFileName: FormControl<string>;
  contentType: FormControl<string>;
  minFileSize: FormControl<number | null>;
  maxFileSize: FormControl<number | null>;
  uploadedFrom: FormControl<string>;
  uploadedTo: FormControl<string>;
};

@Component({
  selector: 'app-student-document-search-filter',
  standalone: true,

  imports: [
    ReactiveFormsModule
  ],

  templateUrl: './student-document-search-filter.component.html',

  styleUrl: './student-document-search-filter.component.scss',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StudentDocumentSearchFilterComponent {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly fb = inject(FormBuilder);

  private readonly toast = inject(ToastService);


  // ============================================================
  // OUTPUTS
  // ============================================================

  @Output()
  readonly searched =
    new EventEmitter<StudentDocumentSearchRequest>();

  @Output()
  readonly reset =
    new EventEmitter<void>();


  // ============================================================
  // DOCUMENT TYPES
  // ============================================================

  readonly documentTypes = Object.values(DocumentType);


  // ============================================================
  // FORM
  // ============================================================

  readonly form: FormGroup<SearchFilterForm> =
    this.fb.group({
      id: this.fb.control<number | null>(null),

      studentId: this.fb.control<number | null>(null),

      documentType:
        this.fb.control<DocumentType | null>(null),

      originalFileName:
        this.fb.nonNullable.control(''),

      storedFileName:
        this.fb.nonNullable.control(''),

      contentType:
        this.fb.nonNullable.control(''),

      minFileSize:
        this.fb.control<number | null>(null),

      maxFileSize:
        this.fb.control<number | null>(null),

      uploadedFrom:
        this.fb.nonNullable.control(''),

      uploadedTo:
        this.fb.nonNullable.control('')
    });


  // ============================================================
  // SEARCH
  // ============================================================

  search(): void {

    const raw = this.form.getRawValue();


    // ==========================================================
    // NUMBER VALIDATION
    // ==========================================================

    if (
      raw.id !== null &&
      raw.id <= 0
    ) {
      this.toast.error(
        'Document ID must be greater than 0.'
      );

      return;
    }


    if (
      raw.studentId !== null &&
      raw.studentId <= 0
    ) {
      this.toast.error(
        'Student ID must be greater than 0.'
      );

      return;
    }


    // ==========================================================
    // FILE SIZE VALIDATION
    // ==========================================================

    if (
      raw.minFileSize !== null &&
      raw.minFileSize < 0
    ) {
      this.toast.error(
        'Minimum file size cannot be negative.'
      );

      return;
    }


    if (
      raw.maxFileSize !== null &&
      raw.maxFileSize < 0
    ) {
      this.toast.error(
        'Maximum file size cannot be negative.'
      );

      return;
    }


    if (
      raw.minFileSize !== null &&
      raw.maxFileSize !== null &&
      raw.minFileSize > raw.maxFileSize
    ) {
      this.toast.error(
        'Minimum file size cannot be greater than maximum file size.'
      );

      return;
    }


    // ==========================================================
    // DATE VALIDATION
    // ==========================================================

    if (
      raw.uploadedFrom &&
      raw.uploadedTo
    ) {

      const from =
        new Date(raw.uploadedFrom);

      const to =
        new Date(raw.uploadedTo);


      if (
        Number.isNaN(from.getTime()) ||
        Number.isNaN(to.getTime())
      ) {

        this.toast.error(
          'Please enter valid upload dates.'
        );

        return;
      }


      if (from > to) {

        this.toast.error(
          'Uploaded from date cannot be later than uploaded to date.'
        );

        return;
      }

    }


    // ==========================================================
    // BUILD REQUEST
    // ==========================================================

    const request: StudentDocumentSearchRequest = {

      id:
        this.normalizeNumber(raw.id),

      studentId:
        this.normalizeNumber(raw.studentId),

      documentType:
        raw.documentType ?? null,

      originalFileName:
        this.normalizeText(raw.originalFileName),

      storedFileName:
        this.normalizeText(raw.storedFileName),

      contentType:
        this.normalizeText(raw.contentType),

      minFileSize:
        this.normalizeNumber(raw.minFileSize),

      maxFileSize:
        this.normalizeNumber(raw.maxFileSize),

      uploadedFrom:
        raw.uploadedFrom || null,

      uploadedTo:
        raw.uploadedTo || null
    };


    // ==========================================================
    // EMIT
    // ==========================================================

    this.searched.emit(request);
  }


  // ============================================================
  // CLEAR
  // ============================================================

  clear(): void {

    this.form.reset({
      id: null,
      studentId: null,
      documentType: null,
      originalFileName: '',
      storedFileName: '',
      contentType: '',
      minFileSize: null,
      maxFileSize: null,
      uploadedFrom: '',
      uploadedTo: ''
    });

    this.reset.emit();

    this.toast.success(
      'Search filters cleared.'
    );
  }


  // ============================================================
  // HELPERS
  // ============================================================

  private normalizeText(
    value: string | null | undefined
  ): string | null {

    const normalized =
      value?.trim() ?? '';

    return normalized || null;
  }


  private normalizeNumber(
    value: number | null | undefined
  ): number | null {

    return value === null ||
      value === undefined ||
      Number.isNaN(value)
      ? null
      : value;
  }

}