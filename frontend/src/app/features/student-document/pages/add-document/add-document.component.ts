import { CommonModule } from '@angular/common';
import {
  Component,
  ElementRef,
  ViewChild,
  inject
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';

import { StudentDocumentService } from '../../services/student-document.service';
import { ToastService } from '@app/shared/services/toast.service';
import { DocumentType } from '@app/shared/enums/document-type.enum';

@Component({
  selector: 'app-add-document',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,

    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatProgressBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './add-document.component.html',
  styleUrl: './add-document.component.scss'
})
export class AddDocumentComponent {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly service = inject(StudentDocumentService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);

  // ============================================================
  // FILE INPUT
  // ============================================================

  @ViewChild('fileInput')
  fileInput!: ElementRef<HTMLInputElement>;

  // ============================================================
  // FORM DATA
  // ============================================================

  studentId: number | null = null;
  documentType: DocumentType | null = null;
  file: File | null = null;

  // ============================================================
  // UI STATE
  // ============================================================

  submitting = false;

  // ============================================================
  // DOCUMENT TYPES
  // ============================================================

  readonly documentTypes = Object.values(DocumentType);

  // ============================================================
  // FILE CONFIGURATION
  // ============================================================

  readonly maxFileSize = 5 * 1024 * 1024;

  readonly allowedTypes = [
    'application/pdf',
    'image/jpeg',
    'image/png'
  ];

  readonly allowedExtensions = [
    '.pdf',
    '.jpg',
    '.jpeg',
    '.png'
  ];

  // ============================================================
  // FILE SELECT
  // ============================================================

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selectedFile = input.files?.[0] ?? null;

    if (!selectedFile) {
      return;
    }

    if (!this.isAllowedFileType(selectedFile)) {
      this.clearFileInput();

      this.toast.error(
        'Only PDF, JPG and PNG files are allowed.'
      );

      return;
    }

    if (selectedFile.size > this.maxFileSize) {
      this.clearFileInput();

      this.toast.error(
        'File size must not exceed 5 MB.'
      );

      return;
    }

    this.file = selectedFile;
  }

  // ============================================================
  // FILE TYPE VALIDATION
  // ============================================================

  private isAllowedFileType(file: File): boolean {
    const mimeTypeValid = this.allowedTypes.includes(file.type);

    const fileName = file.name.toLowerCase();

    const extensionValid = this.allowedExtensions.some(
      extension => fileName.endsWith(extension)
    );

    return mimeTypeValid || extensionValid;
  }

  // ============================================================
  // CLEAR FILE INPUT
  // ============================================================

  private clearFileInput(): void {
    this.file = null;

    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }

  // ============================================================
  // OPEN FILE PICKER
  // ============================================================

  openFilePicker(): void {
    if (this.submitting || !this.fileInput) {
      return;
    }

    // Allows selecting the same file again.
    this.fileInput.nativeElement.value = '';
    this.fileInput.nativeElement.click();
  }

  // ============================================================
  // REMOVE FILE
  // ============================================================

  removeFile(): void {
    if (this.submitting) {
      return;
    }

    this.clearFileInput();
  }

  // ============================================================
  // FORMAT FILE SIZE
  // ============================================================

  formatFileSize(size: number): string {
    if (!Number.isFinite(size) || size <= 0) {
      return '0 Bytes';
    }

    if (size < 1024) {
      return `${size} Bytes`;
    }

    if (size < 1024 * 1024) {
      return `${(size / 1024).toFixed(1)} KB`;
    }

    return `${(size / (1024 * 1024)).toFixed(2)} MB`;
  }

  // ============================================================
  // FILE EXTENSION
  // ============================================================

  getFileExtension(): string {
    if (!this.file) {
      return '';
    }

    const index = this.file.name.lastIndexOf('.');

    if (index === -1) {
      return '';
    }

    return this.file.name
      .substring(index + 1)
      .toUpperCase();
  }

  // ============================================================
  // FILE ICON
  // ============================================================

  getFileIcon(): string {
    if (!this.file) {
      return 'description';
    }

    if (this.file.type === 'application/pdf') {
      return 'picture_as_pdf';
    }

    return 'image';
  }

  // ============================================================
  // FORM VALIDATION
  // ============================================================

  private isFormValid(): boolean {

    if (!this.studentId || this.studentId <= 0) {
      this.toast.error(
        'Please enter a valid student ID.'
      );

      return false;
    }

    if (!this.documentType) {
      this.toast.error(
        'Please select a document type.'
      );

      return false;
    }

    if (!this.file) {
      this.toast.error(
        'Please select a document file.'
      );

      return false;
    }

    return true;
  }

  // ============================================================
  // SUBMIT
  // ============================================================

  submit(): void {
    if (this.submitting) {
      return;
    }

    if (!this.isFormValid()) {
      return;
    }

    this.submitting = true;

    this.service
      .uploadDocument(
        this.studentId!,
        this.documentType!,
        this.file!
      )
      .subscribe({
        next: () => {
          this.submitting = false;

          this.toast.success(
            'Document uploaded successfully.'
          );

          this.router.navigate([
            '/dashboard/student-documents'
          ]);
        },

        error: (error: unknown) => {
          console.error(
            'Document upload failed:',
            error
          );

          this.submitting = false;

          const apiError = error as {
            error?: {
              message?: string;
              error?: string;
            };
          };

          const message =
            apiError?.error?.message ??
            apiError?.error?.error ??
            'Document upload failed. Please try again.';

          this.toast.error(message);
        }
      });
  }

  // ============================================================
  // CANCEL
  // ============================================================

  cancel(): void {
    if (this.submitting) {
      return;
    }

    this.router.navigate([
      '/dashboard/student-documents'
    ]);
  }
}