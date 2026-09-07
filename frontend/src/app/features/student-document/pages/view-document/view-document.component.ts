import { CommonModule } from '@angular/common';

import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  ActivatedRoute,
  Router,
  RouterModule
} from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { StudentDocumentService } from '../../services/student-document.service';

import {
  StudentDocument,
  StudentDocumentApiResponse
} from '../../models/student-document.model';

import { ToastService } from '@app/shared/services/toast.service';


@Component({
  selector: 'app-view-document',

  standalone: true,

  imports: [
    CommonModule,
    RouterModule,

    MatButtonModule,
    MatCardModule,
    MatDividerModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],

  templateUrl: './view-document.component.html',

  styleUrl: './view-document.component.scss'
})
export class ViewDocumentComponent
  implements OnInit {


  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly documentService =
    inject(StudentDocumentService);

  private readonly toast =
    inject(ToastService);


  // ============================================================
  // DOCUMENT
  // ============================================================

  documentId!: number;

  document: StudentDocument | null = null;


  // ============================================================
  // UI STATE
  // ============================================================

  loading = false;

  downloading = false;


  // ============================================================
  // INIT
  // ============================================================

  ngOnInit(): void {

    const id =
      Number(
        this.route.snapshot.paramMap.get('id')
      );


    if (!Number.isInteger(id) || id <= 0) {

      this.toast.error(
        'Invalid document ID.'
      );

      this.back();

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

        next: (
          response:
            StudentDocumentApiResponse<StudentDocument>
        ) => {

          this.document =
            response?.data ?? null;

          this.loading = false;


          if (!this.document) {

            this.toast.error(
              'Document not found.'
            );

            this.back();
          }
        },


        error: (error: unknown) => {

          console.error(
            'Failed to load document:',
            error
          );

          this.loading = false;

          this.toast.error(
            this.getErrorMessage(
              error,
              'Failed to load document.'
            )
          );

          this.back();
        }

      });
  }


  // ============================================================
  // DOWNLOAD
  // ============================================================

  download(): void {

    if (
      !this.document ||
      this.downloading
    ) {
      return;
    }


    this.downloading = true;

    this.documentService
      .downloadDocument(
        this.document.id
      )
      .subscribe({

        next: (blob: Blob) => {

          if (
            !blob ||
            blob.size === 0
          ) {

            this.downloading = false;

            this.toast.error(
              'Downloaded document is empty.'
            );

            return;
          }


          const url =
            window.URL.createObjectURL(
              blob
            );

          const anchor =
            window.document.createElement(
              'a'
            );


          anchor.href = url;

          anchor.download =
            this.document?.originalFileName?.trim()
            || 'student-document';

          anchor.style.display =
            'none';


          window.document.body.appendChild(
            anchor
          );

          anchor.click();

          window.document.body.removeChild(
            anchor
          );

          window.URL.revokeObjectURL(
            url
          );


          this.downloading = false;

          this.toast.success(
            'Document downloaded successfully.'
          );
        },


        error: (error: unknown) => {

          console.error(
            'Document download failed:',
            error
          );

          this.downloading = false;

          this.toast.error(
            this.getErrorMessage(
              error,
              'Failed to download document.'
            )
          );
        }

      });
  }


  // ============================================================
  // EDIT
  // ============================================================

  edit(): void {

    if (!this.document) {
      return;
    }


    this.router.navigate([
      '/dashboard/student-documents/edit',
      this.document.id
    ]);
  }


  // ============================================================
  // BACK
  // ============================================================

  back(): void {

    if (this.loading || this.downloading) {
      return;
    }


    this.router.navigate([
      '/dashboard/student-documents'
    ]);
  }


  // ============================================================
  // FILE SIZE
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


    if (
      size <
      1024 * 1024
    ) {

      return `${(
        size / 1024
      ).toFixed(1)} KB`;
    }


    if (
      size <
      1024 *
      1024 *
      1024
    ) {

      return `${(
        size /
        (1024 * 1024)
      ).toFixed(2)} MB`;
    }


    return `${(
      size /
      (1024 * 1024 * 1024)
    ).toFixed(2)} GB`;
  }


  // ============================================================
  // DATE
  // ============================================================

  formatDate(
    value?: string | null
  ): string {

    if (!value) {
      return '—';
    }


    const date =
      new Date(value);


    if (
      Number.isNaN(
        date.getTime()
      )
    ) {
      return '—';
    }


    return date.toLocaleString(
      undefined,
      {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      }
    );
  }


  // ============================================================
  // DOCUMENT TYPE
  // ============================================================

  formatDocumentType(
    value?: string | null
  ): string {

    if (!value) {
      return '—';
    }


    return value
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(
        /\b\w/g,
        char => char.toUpperCase()
      );
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