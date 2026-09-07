import { CommonModule } from '@angular/common';

import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  ActivatedRoute,
  Router,
  RouterModule
} from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';


import {
  StudentDocumentService
} from '../../services/student-document.service';

import {
  StudentDocument,
  StudentDocumentApiResponse
} from '../../models/student-document.model';

import { ToastService } from '@app/shared/services/toast.service';

import { DocumentType } from '@app/shared/enums/document-type.enum';


@Component({
  selector: 'app-edit-document',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,

    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],

  templateUrl: './edit-document.component.html',

  styleUrl: './edit-document.component.scss'
})
export class EditDocumentComponent
  implements OnInit {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly fb =
    inject(FormBuilder);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly documentService =
    inject(StudentDocumentService);

  private readonly toastService =
    inject(ToastService);


  // ============================================================
  // DOCUMENT
  // ============================================================

  documentId!: number;

  document: StudentDocument | null = null;


  // ============================================================
  // FILE
  // ============================================================

  selectedFile: File | null = null;

  readonly maxFileSize =
    5 * 1024 * 1024;

  readonly allowedExtensions = [
    '.pdf',
    '.jpg',
    '.jpeg',
    '.png'
  ];

  readonly allowedMimeTypes = [
    'application/pdf',
    'image/jpeg',
    'image/png'
  ];


  // ============================================================
  // UI STATE
  // ============================================================

  loading = false;

  saving = false;


  // ============================================================
  // DOCUMENT TYPES
  // ============================================================

  readonly documentTypes =
    Object.values(DocumentType);


  // ============================================================
  // FORM
  // ============================================================

  readonly form =
    this.fb.nonNullable.group({

      file: [
        null as File | null,
        Validators.required
      ]

    });


  // ============================================================
  // INIT
  // ============================================================

  ngOnInit(): void {

    const id =
      Number(
        this.route.snapshot.paramMap.get('id')
      );


    if (!Number.isInteger(id) || id <= 0) {

      this.toastService.error(
        'Invalid document ID.'
      );

      this.goBack();

      return;
    }


    this.documentId = id;

    this.loadDocument();
  }


  // ============================================================
  // LOAD DOCUMENT
  // ============================================================

  private loadDocument(): void {

    this.loading = true;

    this.documentService
      .getDocumentById(this.documentId)
      .subscribe({

        // ======================================================
        // SUCCESS
        // ======================================================

        next: (
          response:
            StudentDocumentApiResponse<StudentDocument>
        ) => {

          this.document =
            response?.data ?? null;

          this.loading = false;


          if (!this.document) {

            this.toastService.error(
              'Document not found.'
            );

            this.goBack();
          }

        },


        // ======================================================
        // ERROR
        // ======================================================

        error: (error: unknown) => {

          console.error(
            'Failed to load document:',
            error
          );

          this.loading = false;

          this.toastService.error(
            this.getErrorMessage(
              error,
              'Failed to load document.'
            )
          );

          this.goBack();

        }

      });

  }


  // ============================================================
  // FILE SELECT
  // ============================================================

  onFileSelected(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;


    if (
      !input.files ||
      input.files.length === 0
    ) {

      this.clearSelectedFile();

      return;
    }


    const file =
      input.files[0];


    // ==========================================================
    // VALIDATE FILE
    // ==========================================================

    const validationError =
      this.validateFile(file);


    if (validationError) {

      this.clearSelectedFile();

      this.toastService.error(
        validationError
      );

      return;
    }


    // ==========================================================
    // SET FILE
    // ==========================================================

    this.selectedFile =
      file;

    this.form.patchValue({
      file
    });

    this.form
      .get('file')
      ?.markAsDirty();

    this.form
      .get('file')
      ?.markAsTouched();

    this.form
      .get('file')
      ?.updateValueAndValidity();

  }


  // ============================================================
  // VALIDATE FILE
  // ============================================================

  private validateFile(
    file: File
  ): string | null {

    if (!file) {

      return 'Please select a document file.';
    }


    if (file.size <= 0) {

      return 'The selected file is empty.';
    }


    if (file.size > this.maxFileSize) {

      return 'File size must not exceed 5 MB.';
    }


    const fileName =
      file.name.toLowerCase();

    const hasValidExtension =
      this.allowedExtensions.some(
        extension =>
          fileName.endsWith(extension)
      );


    if (!hasValidExtension) {

      return 'Only PDF, JPG, JPEG and PNG files are allowed.';
    }


    /*
     * Some browsers may return an empty MIME type.
     * In that case the extension validation above is enough.
     */
    if (
      file.type &&
      !this.allowedMimeTypes.includes(file.type)
    ) {

      return 'Invalid file type. Please select a PDF, JPG or PNG file.';
    }


    return null;
  }


  // ============================================================
  // REMOVE SELECTED FILE
  // ============================================================

  removeSelectedFile(): void {

    this.clearSelectedFile();

  }


  // ============================================================
  // CLEAR SELECTED FILE
  // ============================================================

  private clearSelectedFile(): void {

    this.selectedFile = null;

    this.form.patchValue({
      file: null
    });

    this.form
      .get('file')
      ?.markAsUntouched();

    this.form
      .get('file')
      ?.markAsPristine();

    this.form
      .get('file')
      ?.updateValueAndValidity();

  }


  // ============================================================
  // SUBMIT
  // ============================================================

  submit(): void {

    if (this.saving) {
      return;
    }


    if (!this.selectedFile) {

      this.form.markAllAsTouched();

      this.toastService.warning(
        'Please select a new document file.'
      );

      return;
    }


    const validationError =
      this.validateFile(
        this.selectedFile
      );


    if (validationError) {

      this.toastService.error(
        validationError
      );

      return;
    }


    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;
    }


    this.saving = true;


    this.documentService
      .updateDocument(
        this.documentId,
        this.selectedFile
      )
      .subscribe({

        // ======================================================
        // SUCCESS
        // ======================================================

        next: (
          response:
            StudentDocumentApiResponse<StudentDocument>
        ) => {

          this.saving = false;

          this.toastService.success(
            'Document replaced successfully.'
          );

          this.router.navigate([
            '/dashboard/student-documents'
          ]);

        },


        // ======================================================
        // ERROR
        // ======================================================

        error: (error: unknown) => {

          console.error(
            'Document update failed:',
            error
          );

          this.saving = false;

          this.toastService.error(
            this.getErrorMessage(
              error,
              'Document update failed. Please try again.'
            )
          );

        }

      });

  }


  // ============================================================
  // CANCEL
  // ============================================================

  cancel(): void {

    if (this.saving) {
      return;
    }

    this.goBack();

  }


  // ============================================================
  // BACK TO LIST
  // ============================================================

  private goBack(): void {

    this.router.navigate([
      '/dashboard/student-documents'
    ]);

  }


  // ============================================================
  // DOCUMENT TYPE FORMAT
  // ============================================================

  formatDocumentType(
    type?: string | null
  ): string {

    if (!type) {
      return '—';
    }

    return type
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(
        /\b\w/g,
        char => char.toUpperCase()
      );

  }


  // ============================================================
  // FILE SIZE FORMAT
  // ============================================================

  formatFileSize(
    size?: number | null
  ): string {

    if (
      size === null ||
      size === undefined ||
      size < 0
    ) {

      return '—';
    }


    if (size < 1024) {

      return `${size} B`;
    }


    if (size < 1024 * 1024) {

      return `${(
        size / 1024
      ).toFixed(1)} KB`;
    }


    if (size < 1024 * 1024 * 1024) {

      return `${(
        size / (1024 * 1024)
      ).toFixed(2)} MB`;
    }


    return `${(
      size / (1024 * 1024 * 1024)
    ).toFixed(2)} GB`;

  }


  // ============================================================
  // ERROR MESSAGE
  // ============================================================

  private getErrorMessage(
    error: unknown,
    fallback: string
  ): string {

    const apiError =
      error as {
        error?: {
          message?: string;
          error?: string;
        };
        message?: string;
      };


    return (
      apiError?.error?.message ||
      apiError?.error?.error ||
      apiError?.message ||
      fallback
    );

  }

}