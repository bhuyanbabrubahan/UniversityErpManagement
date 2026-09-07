import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject
} from '@angular/core';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { CommonModule } from '@angular/common';

import {
  DocumentType,
  StudentDocument
} from '../../models/student-document.model';

import { ToastService } from '../../../../shared/services/toast.service';


@Component({
  selector: 'app-student-document-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './student-document-form.component.html',
  styleUrl: './student-document-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StudentDocumentFormComponent implements OnChanges {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly fb = inject(FormBuilder);

  private readonly toast = inject(ToastService);


  // ============================================================
  // INPUTS
  // ============================================================

  @Input() document: StudentDocument | null = null;

  @Input() loading = false;

  @Input() editMode = false;


  // ============================================================
  // OUTPUTS
  // ============================================================

  @Output() submitted = new EventEmitter<{
    studentId: number;
    documentType: DocumentType;
    file: File | null;
  }>();

  @Output() cancelled = new EventEmitter<void>();


  // ============================================================
  // FORM
  // ============================================================

  form: FormGroup;


  // ============================================================
  // FILE
  // ============================================================

  selectedFile: File | null = null;

  fileError = '';

  readonly maxFileSize = 5 * 1024 * 1024;

  readonly documentTypes = Object.values(DocumentType);


  // ============================================================
  // CONSTRUCTOR
  // ============================================================

  constructor() {

    this.form = this.fb.group({

      studentId: [
        null,
        [
          Validators.required,
          Validators.min(1)
        ]
      ],

      documentType: [
        null,
        Validators.required
      ]

    });

  }


  // ============================================================
  // ON CHANGES
  // ============================================================

  ngOnChanges(changes: SimpleChanges): void {

    if (
      changes['document'] &&
      this.document
    ) {

      this.form.patchValue({

        studentId:
          this.document.studentId,

        documentType:
          this.document.documentType

      });

      this.selectedFile = null;

      this.fileError = '';

    }

  }


  // ============================================================
  // FILE SELECT
  // ============================================================

  onFileSelected(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    this.fileError = '';


    if (
      !input.files ||
      input.files.length === 0
    ) {

      this.selectedFile = null;

      return;

    }


    const file = input.files[0];


    if (!this.isAllowedFile(file)) {

      input.value = '';

      this.selectedFile = null;

      return;

    }


    this.selectedFile = file;

    this.toast.success(
      'Document file selected successfully.'
    );

  }


  // ============================================================
  // FILE VALIDATION
  // ============================================================

  private isAllowedFile(file: File): boolean {

    const allowedTypes = [
      'image/jpeg',
      'image/png',
      'application/pdf'
    ];


    if (!allowedTypes.includes(file.type)) {

      this.fileError =
        'Only JPG, PNG and PDF files are allowed.';

      this.toast.error(
        'Only JPG, PNG and PDF files are allowed.'
      );

      return false;

    }


    if (file.size > this.maxFileSize) {

      this.fileError =
        'File size must be less than 5 MB.';

      this.toast.error(
        'File size must be less than 5 MB.'
      );

      return false;

    }


    return true;

  }


  // ============================================================
  // SUBMIT
  // ============================================================

    submit(): void {

    // ----------------------------------------------------------
    // FORM VALIDATION
    // ----------------------------------------------------------

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      this.toast.error(
        'Please fill all required fields.'
      );

      return;

    }


    // ----------------------------------------------------------
    // FILE REQUIRED
    // ----------------------------------------------------------

    if (!this.selectedFile) {

      this.fileError =
        'Please select a document file.';

      this.toast.error(
        'Please select a document file.'
      );

      return;

    }


    // ----------------------------------------------------------
    // SUBMIT
    // ----------------------------------------------------------

    this.submitted.emit({

      studentId:
        this.form.value.studentId,

      documentType:
        this.form.value.documentType,

      file:
        this.selectedFile

    });

  }



  // ============================================================
  // CANCEL
  // ============================================================

  cancel(): void {

    this.cancelled.emit();

  }

}