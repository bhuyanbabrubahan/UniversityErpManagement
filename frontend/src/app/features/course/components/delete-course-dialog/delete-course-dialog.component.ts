import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  CourseResponse
} from '../../models/course.model';


@Component({
  selector: 'app-delete-course-dialog',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './delete-course-dialog.component.html',
  styleUrl: './delete-course-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DeleteCourseDialogComponent {

  @Input()
  course: CourseResponse | null = null;

  @Input()
  loading = false;


  @Output()
  confirmed =
    new EventEmitter<number>();

  @Output()
  cancelled =
    new EventEmitter<void>();


  // =====================================================
  // CONFIRM DELETE
  // =====================================================

  confirmDelete(): void {

    if (
      !this.course ||
      this.loading
    ) {
      return;
    }

    this.confirmed.emit(
      this.course.id
    );
  }


  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {

    if (!this.loading) {

      this.cancelled.emit();
    }
  }

}