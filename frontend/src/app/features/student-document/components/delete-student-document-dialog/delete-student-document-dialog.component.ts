import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { MatIconModule } from '@angular/material/icon';

import {
  StudentDocument
} from '../../models/student-document.model';

@Component({
  selector: 'app-delete-student-document-dialog',
  standalone: true,

  imports: [
    CommonModule,
    MatIconModule
  ],

  templateUrl:
    './delete-student-document-dialog.component.html',

  styleUrl:
    './delete-student-document-dialog.component.scss',

  changeDetection:
    ChangeDetectionStrategy.OnPush
})
export class DeleteStudentDocumentDialogComponent {

  @Input()
  document: StudentDocument | null = null;

  @Input()
  loading = false;

  @Output()
  confirmed =
    new EventEmitter<StudentDocument>();

  @Output()
  cancelled =
    new EventEmitter<void>();


  confirm(): void {

    if (
      !this.document ||
      this.loading
    ) {
      return;
    }

    this.confirmed.emit(
      this.document
    );
  }


  cancel(): void {

    if (this.loading) {
      return;
    }

    this.cancelled.emit();
  }

}