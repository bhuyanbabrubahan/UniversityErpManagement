import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import {
  SubjectResponse
} from '../../models/subject.model';

import {
  formatSubjectDate,
  getMarksDisplay
} from '../../shared/subject.utils';


@Component({
  selector: 'app-subject-table',
  standalone: true,
  imports: [],
  templateUrl: './subject-table.component.html',
  styleUrl: './subject-table.component.scss'
})
export class SubjectTableComponent {

  // =====================================================
  // INPUTS
  // =====================================================

  @Input()
  subjects: SubjectResponse[] = [];

  @Input()
  loading = false;

  @Input()
  page = 0;

  @Input()
  size = 10;

  @Input()
  totalElements = 0;

  @Input()
  totalPages = 0;

  @Input()
  last = true;


  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  view =
    new EventEmitter<SubjectResponse>();

  @Output()
  edit =
    new EventEmitter<SubjectResponse>();

  @Output()
  delete =
    new EventEmitter<SubjectResponse>();

  @Output()
  pageChange =
    new EventEmitter<number>();

  @Output()
  pageSizeChange =
    new EventEmitter<number>();

  @Output()
  sortChange =
    new EventEmitter<{
      sortBy: string;
      direction: 'asc' | 'desc';
    }>();


  // =====================================================
  // CURRENT SORT
  // =====================================================

  currentSort = 'id';

  currentDirection: 'asc' | 'desc' = 'desc';


  // =====================================================
  // PAGE SIZE OPTIONS
  // =====================================================

  pageSizeOptions: number[] = [
    10,
    20,
    50,
    100
  ];


  // =====================================================
  // HELPERS
  // =====================================================

  getMarksDisplay =
    getMarksDisplay;

  formatSubjectDate =
    formatSubjectDate;


  // =====================================================
  // SORT
  // =====================================================

  onSort(
    sortBy: string
  ): void {

    if (this.currentSort === sortBy) {

      this.currentDirection =
        this.currentDirection === 'asc'
          ? 'desc'
          : 'asc';

    } else {

      this.currentSort =
        sortBy;

      this.currentDirection =
        'asc';

    }

    this.sortChange.emit({

      sortBy:
        this.currentSort,

      direction:
        this.currentDirection

    });

  }


  // =====================================================
  // SORT STATE
  // =====================================================

  isSorted(
    sortBy: string
  ): boolean {

    return this.currentSort === sortBy;

  }


  // =====================================================
  // SORT ICON
  // =====================================================

  getSortIcon(
    sortBy: string
  ): string {

    if (
      this.currentSort !== sortBy
    ) {

      return '↕';

    }

    return this.currentDirection === 'asc'
      ? '↑'
      : '↓';

  }


  // =====================================================
  // PAGINATION
  // =====================================================

  goToPage(
    page: number
  ): void {

    if (page < 0) {
      return;
    }

    if (
      this.totalPages > 0 &&
      page >= this.totalPages
    ) {
      return;
    }

    if (page === this.page) {
      return;
    }

    this.pageChange.emit(page);

  }


  // =====================================================
  // PAGE SIZE
  // =====================================================

  changePageSize(
    size: number
  ): void {

    if (size <= 0) {
      return;
    }

    if (size === this.size) {
      return;
    }

    this.pageSizeChange.emit(size);

  }


  // =====================================================
  // PREVIOUS
  // =====================================================

  previousPage(): void {

    if (
      this.page > 0 &&
      !this.loading
    ) {

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
      this.page < this.totalPages - 1 &&
      !this.loading
    ) {

      this.pageChange.emit(
        this.page + 1
      );

    }

  }


  // =====================================================
  // FIRST PAGE
  // =====================================================

  firstPage(): void {

    if (
      this.page > 0 &&
      !this.loading
    ) {

      this.pageChange.emit(0);

    }

  }


  // =====================================================
  // LAST PAGE
  // =====================================================

  lastPage(): void {

    if (
      this.totalPages > 0 &&
      this.page < this.totalPages - 1 &&
      !this.loading
    ) {

      this.pageChange.emit(
        this.totalPages - 1
      );

    }

  }


  // =====================================================
  // PAGE NUMBERS
  // =====================================================

  get visiblePages(): number[] {

    const total =
      this.totalPages;

    const current =
      this.page;

    if (total <= 0) {
      return [];
    }

    if (total <= 7) {

      return Array.from(
        { length: total },
        (_, index) => index
      );

    }

    if (current <= 3) {

      return [
        0,
        1,
        2,
        3,
        4,
        -1,
        total - 1
      ];

    }

    if (current >= total - 4) {

      return [
        0,
        -1,
        total - 5,
        total - 4,
        total - 3,
        total - 2,
        total - 1
      ];

    }

    return [
      0,
      -1,
      current - 1,
      current,
      current + 1,
      -1,
      total - 1
    ];

  }


  // =====================================================
  // DISPLAY RANGE
  // =====================================================

  get displayStart(): number {

    if (
      this.totalElements === 0
    ) {
      return 0;
    }

    return (
      this.page * this.size
    ) + 1;

  }


  get displayEnd(): number {

    return Math.min(
      (this.page + 1) * this.size,
      this.totalElements
    );

  }


  // =====================================================
  // TRACK
  // =====================================================

  trackById(
    _: number,
    subject: SubjectResponse
  ): number {

    return subject.id;

  }

}