import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import {
  DocumentType,
  RecordStatus,
  StudentDocument
} from '../../models/student-document.model';

@Component({
  selector: 'app-student-document-table',
  standalone: true,

  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatProgressSpinnerModule
  ],

  templateUrl:
    './student-document-table.component.html',

  styleUrl:
    './student-document-table.component.scss',

  changeDetection:
    ChangeDetectionStrategy.OnPush
})
export class StudentDocumentTableComponent {

  readonly RecordStatus = RecordStatus;
  // ============================================================
  // INPUTS
  // ============================================================

  @Input()
  documents: StudentDocument[] = [];

  @Input()
  loading = false;


  // ============================================================
  // OUTPUTS
  // ============================================================

  @Output()
  view = new EventEmitter<StudentDocument>();

  @Output()
  edit = new EventEmitter<StudentDocument>();

  @Output()
  download = new EventEmitter<StudentDocument>();

  @Output()
  delete = new EventEmitter<StudentDocument>();


  // ============================================================
  // DOCUMENT TYPE
  // ============================================================

  formatDocumentType(
    type: DocumentType | null | undefined
  ): string {

    if (!type) {
      return '—';
    }

    return String(type)
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(
        /\b\w/g,
        char => char.toUpperCase()
      );
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
  // VIEW
  // ============================================================

  onView(
    document: StudentDocument
  ): void {

    if (!document) {
      return;
    }

    this.view.emit(document);
  }


  // ============================================================
  // DOWNLOAD
  // ============================================================

  onDownload(
    document: StudentDocument
  ): void {

    if (!document) {
      return;
    }

    this.download.emit(document);
  }


  // ============================================================
  // EDIT
  // ============================================================

  onEdit(
    document: StudentDocument
  ): void {

    if (!document) {
      return;
    }

    this.edit.emit(document);
  }


  // ============================================================
  // DELETE
  // ============================================================

  onDelete(
    document: StudentDocument
  ): void {

    if (!document) {
      return;
    }

    this.delete.emit(document);
  }

}