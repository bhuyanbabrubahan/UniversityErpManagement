import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  CourseSearchRequest
} from '../../models/course.model';

@Component({
  selector: 'app-course-search-filter',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './course-search-filter.component.html',
  styleUrl: './course-search-filter.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CourseSearchFilterComponent {

  @Input() departments: Array<{
    id: number;
    departmentName: string;
  }> = [];

  @Input() loading = false;

  @Output() searchChange =
    new EventEmitter<CourseSearchRequest>();

  @Output() clear =
    new EventEmitter<void>();

  search: CourseSearchRequest = {
    courseName: '',
    courseCode: '',
    departmentId: undefined,
    status: undefined,
    minDuration: undefined,
    maxDuration: undefined
  };

  onSearch(): void {

    console.log('SEARCH BUTTON CLICKED');

    const request: CourseSearchRequest = {

      courseName:
        this.search.courseName?.trim() || undefined,

      courseCode:
        this.search.courseCode?.trim() || undefined,

      departmentId:
        this.search.departmentId || undefined,

      status:
        this.search.status || undefined,

      minDuration:
        this.search.minDuration != null
          ? Number(this.search.minDuration) * 12
          : undefined,

      maxDuration:
        this.search.maxDuration != null
          ? Number(this.search.maxDuration) * 12
          : undefined
    };

    console.log('COURSE SEARCH REQUEST:', request);

    this.searchChange.emit(request);
  }

  onClear(): void {

    this.search = {
      courseName: '',
      courseCode: '',
      departmentId: undefined,
      status: undefined,
      minDuration: undefined,
      maxDuration: undefined
    };

    this.clear.emit();
  }

  onEnter(): void {

    if (!this.loading) {
      this.onSearch();
    }
  }
}