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
  selector: 'app-course-table',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './course-table.component.html',
  styleUrl: './course-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CourseTableComponent {

  @Input() courses: CourseResponse[] = [];

  @Input() loading = false;

  @Input() totalElements = 0;

  @Output() view =
    new EventEmitter<number>();

  @Output() edit =
    new EventEmitter<number>();

  @Output() delete =
    new EventEmitter<CourseResponse>();

  @Output() sortChange =
    new EventEmitter<string>();

  onView(id: number): void {
    this.view.emit(id);
  }

  onEdit(id: number): void {
    this.edit.emit(id);
  }

  onDelete(course: CourseResponse): void {
    this.delete.emit(course);
  }

  onSort(field: string): void {
    this.sortChange.emit(field);
  }

  getStatusClass(status: string): string {

    switch (status) {

      case 'ACTIVE':
        return 'status-active';

      case 'INACTIVE':
        return 'status-inactive';

      case 'DELETED':
        return 'status-deleted';

      default:
        return '';
    }
  }
}