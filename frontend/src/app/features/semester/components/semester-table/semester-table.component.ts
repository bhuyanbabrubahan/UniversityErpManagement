import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import {
  SemesterResponse
} from '../../models/semester.model';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';


@Component({
  selector: 'app-semester-table',
  standalone: true,
  imports: [],
  templateUrl: './semester-table.component.html',
  styleUrl: './semester-table.component.scss'
})
export class SemesterTableComponent {

  // =====================================================
  // INPUTS
  // =====================================================

  @Input()
  semesters: SemesterResponse[] = [];

  @Input()
  loading = false;

  @Input()
  page = 0;

  @Input()
  size = 10;

  @Input()
  totalPages = 0;

  @Input()
  totalElements = 0;

  @Input()
  last = true;

  @Input()
  canEdit = true;

  @Input()
  canDelete = true;

  // Current sorting
  @Input()
  sortBy = 'id';

  @Input()
  direction: 'asc' | 'desc' = 'desc';


  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  view = new EventEmitter<number>();

  @Output()
  edit = new EventEmitter<number>();

  @Output()
  delete = new EventEmitter<number>();

  @Output()
  pageChange = new EventEmitter<number>();

  @Output()
  sortChange = new EventEmitter<{
    sortBy: string;
    direction: 'asc' | 'desc';
  }>();


  // =====================================================
  // ENUM
  // =====================================================

  readonly RecordStatus = RecordStatus;


  // =====================================================
  // VIEW
  // =====================================================

  onView(
    semester: SemesterResponse
  ): void {

    this.view.emit(semester.id);

  }


  // =====================================================
  // EDIT
  // =====================================================

  onEdit(
    semester: SemesterResponse
  ): void {

    this.edit.emit(semester.id);

  }


  // =====================================================
  // DELETE
  // =====================================================

  onDelete(
    semester: SemesterResponse
  ): void {

    this.delete.emit(semester.id);

  }


  // =====================================================
  // SORT
  // =====================================================

  onSort(
    field: string
  ): void {

    let nextDirection: 'asc' | 'desc';

    if (this.sortBy === field) {

      nextDirection =
        this.direction === 'asc'
          ? 'desc'
          : 'asc';

    } else {

      nextDirection = 'asc';

    }

    this.sortChange.emit({
      sortBy: field,
      direction: nextDirection
    });

  }


  // =====================================================
  // SORT STATE
  // =====================================================

  getSortIcon(
    field: string
  ): string {

    if (this.sortBy !== field) {
      return '↕';
    }

    return this.direction === 'asc'
      ? '↑'
      : '↓';

  }


  isSorted(
    field: string
  ): boolean {

    return this.sortBy === field;

  }


  // =====================================================
  // PAGINATION
  // =====================================================

  onPageChange(
    page: number
  ): void {

    if (
      page < 0 ||
      page >= this.totalPages ||
      page === this.page
    ) {
      return;
    }

    this.pageChange.emit(page);

  }


  // =====================================================
  // PREVIOUS
  // =====================================================

  previousPage(): void {

    if (this.page > 0) {

      this.pageChange.emit(
        this.page - 1
      );

    }

  }


  // =====================================================
  // NEXT
  // =====================================================

  nextPage(): void {

    if (
      !this.last &&
      this.page < this.totalPages - 1
    ) {

      this.pageChange.emit(
        this.page + 1
      );

    }

  }


  // =====================================================
  // TRACK
  // =====================================================

  trackById(
    _: number,
    semester: SemesterResponse
  ): number {

    return semester.id;

  }


  // =====================================================
  // PAGE NUMBERS
  // =====================================================

  get pageNumbers(): number[] {

    return Array.from(
      { length: this.totalPages },
      (_, index) => index
    );

  }

}