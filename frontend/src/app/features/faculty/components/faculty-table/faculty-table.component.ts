import {
  Component,
  EventEmitter,
  Input,
  Output,
  inject
} from '@angular/core';

import {
  MatDialog
} from '@angular/material/dialog';

import {
  MATERIAL_MODULES
} from '../../../../shared/material/material';

import {
  FacultyResponse
} from '../../model/faculty-response.model';

import {
  FacultyImageDialogComponent
} from '../faculty-image-dialog/faculty-image-dialog.component';

import {
  API
} from '@app/core/constants/api.constants';


// =====================================================
// SORT TYPES
// =====================================================

type SortDirection =
  | 'asc'
  | 'desc'
  | '';

interface FacultySort {
  active: string;
  direction: SortDirection;
}


@Component({
  selector: 'app-faculty-table',

  standalone: true,

  imports: [
    ...MATERIAL_MODULES
  ],

  templateUrl: './faculty-table.component.html',

  styleUrl: './faculty-table.component.scss'
})
export class FacultyTableComponent {

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly dialog =
    inject(MatDialog);


  // =====================================================
  // INPUTS
  // =====================================================

  @Input()
  faculties: FacultyResponse[] = [];


  @Input()
  loading = false;


  @Input()
  totalElements = 0;


  @Input()
  pageSize = 5;


  @Input()
  pageIndex = 0;


  @Input()
  deleting = false;


  // =====================================================
  // PAGINATION OPTIONS
  // =====================================================

  readonly pageSizeOptions: number[] = [
    5,
    10,
    20,
    50
  ];


  // =====================================================
  // SORT STATE
  // =====================================================

  sortActive = '';

  sortDirection: SortDirection = '';


  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  pageChange =
    new EventEmitter<{
      pageIndex: number;
      pageSize: number;
    }>();


  @Output()
  sortChange =
    new EventEmitter<FacultySort>();


  @Output()
  view =
    new EventEmitter<number>();


  @Output()
  edit =
    new EventEmitter<number>();


  @Output()
  delete =
    new EventEmitter<number>();


  // =====================================================
  // TABLE COLUMNS
  // =====================================================

  readonly displayedColumns: string[] = [
    'employeeCode',
    'faculty',
    'department',
    'designation',
    'qualification',
    'mobileNumber',
    'status',
    'actions'
  ];


  // =====================================================
  // SORT
  // =====================================================

  onSort(
    active: string
  ): void {

    if (
      this.sortActive !== active
    ) {

      this.sortActive = active;

      this.sortDirection = 'asc';

    } else {

      if (
        this.sortDirection === ''
      ) {

        this.sortDirection = 'asc';

      } else if (
        this.sortDirection === 'asc'
      ) {

        this.sortDirection = 'desc';

      } else {

        this.sortDirection = '';

        this.sortActive = '';
      }
    }


    this.sortChange.emit({
      active: this.sortActive,
      direction: this.sortDirection
    });


    // Sorting ke baad first page par jaana
    if (
      this.pageIndex !== 0
    ) {

      this.pageIndex = 0;

      this.pageChange.emit({
        pageIndex: 0,
        pageSize: this.pageSize
      });

    }

  }


  // =====================================================
  // SORT ICON
  // =====================================================

  getSortIcon(
    active: string
  ): string {

    if (
      this.sortActive !== active ||
      !this.sortDirection
    ) {

      return '↕';

    }


    if (
      this.sortDirection === 'asc'
    ) {

      return '↑';

    }


    if (
      this.sortDirection === 'desc'
    ) {

      return '↓';

    }


    return '↕';
  }


  // =====================================================
  // PAGE SIZE CHANGE
  // =====================================================

  onPageSizeChange(
    event: Event
  ): void {

    const select =
      event.target as HTMLSelectElement;


    const newPageSize =
      Number(select.value);


    if (
      !newPageSize ||
      newPageSize <= 0
    ) {

      return;
    }


    this.pageSize =
      newPageSize;

    this.pageIndex = 0;


    this.pageChange.emit({
      pageIndex: 0,
      pageSize: newPageSize
    });

  }


  // =====================================================
  // PREVIOUS PAGE
  // =====================================================

  previousPage(): void {

    if (
      this.pageIndex <= 0
    ) {

      return;
    }


    this.pageIndex--;

    this.pageChange.emit({
      pageIndex: this.pageIndex,
      pageSize: this.pageSize
    });

  }


  // =====================================================
  // NEXT PAGE
  // =====================================================

  nextPage(): void {

    const totalPages =
      this.getTotalPages();


    if (
      this.pageIndex >= totalPages - 1
    ) {

      return;
    }


    this.pageIndex++;


    this.pageChange.emit({
      pageIndex: this.pageIndex,
      pageSize: this.pageSize
    });

  }


  // =====================================================
  // TOTAL PAGES
  // =====================================================

  getTotalPages(): number {

    if (
      !this.totalElements ||
      this.totalElements <= 0
    ) {

      return 1;
    }


    return Math.ceil(
      this.totalElements /
      this.pageSize
    );
  }


  // =====================================================
  // VIEW
  // =====================================================

  onView(
    id: number
  ): void {

    if (
      !id ||
      id <= 0
    ) {

      return;
    }


    this.view.emit(id);

  }


  // =====================================================
  // EDIT
  // =====================================================

  onEdit(
    id: number
  ): void {

    if (
      !id ||
      id <= 0
    ) {

      return;
    }


    this.edit.emit(id);

  }


  // =====================================================
  // DELETE
  // =====================================================

  onDelete(
    id: number
  ): void {

    if (
      this.deleting ||
      !id ||
      id <= 0
    ) {

      return;
    }


    this.delete.emit(id);

  }


  // =====================================================
  // PROFILE IMAGE URL
  // =====================================================

  getProfileImageUrl(
    profileImage:
      string |
      null |
      undefined
  ): string {

    const defaultImage =
      'images/default-avatar.png';


    if (
      !profileImage
    ) {

      return defaultImage;
    }


    const image =
      profileImage.trim();


    if (!image) {

      return defaultImage;
    }


    // Already complete URL
    if (
      image.startsWith('http://') ||
      image.startsWith('https://') ||
      image.startsWith('data:image/')
    ) {

      return image;
    }


    // Backend profile photo endpoint
    return `${
      API.PROFILE
    }/photo/${
      encodeURIComponent(image)
    }`;
  }


  // =====================================================
  // IMAGE ERROR FALLBACK
  // =====================================================

  onImageError(
    event: Event
  ): void {

    const image =
      event.target as HTMLImageElement;


    if (!image) {

      return;
    }


    const defaultImage =
      'images/default-avatar.png';


    if (
      image.src.endsWith(
        defaultImage
      )
    ) {

      return;
    }


    image.src =
      defaultImage;
  }


  // =====================================================
  // OPEN FACULTY IMAGE
  // =====================================================

  openFacultyImage(
    faculty: FacultyResponse
  ): void {

    if (!faculty) {

      return;
    }


    const imageUrl =
      this.getProfileImageUrl(
        faculty.profileImage
      );


    this.dialog.open(
      FacultyImageDialogComponent,
      {

        width: '100%',

        maxWidth: '720px',

        maxHeight: '90vh',

        autoFocus: false,

        restoreFocus: false,

        panelClass:
          'faculty-image-dialog-panel',

        data: {

          imageUrl,

          facultyName:
            this.getFacultyName(
              faculty
            )
        }
      }
    );

  }


  // =====================================================
  // FACULTY INITIAL
  // =====================================================

  getFacultyInitial(
    faculty: FacultyResponse
  ): string {

    const name =
      this.getFacultyName(
        faculty
      );


    if (
      !name ||
      name === '-'
    ) {

      return 'F';
    }


    return name
      .charAt(0)
      .toUpperCase();
  }


  // =====================================================
  // FACULTY NAME
  // =====================================================

  getFacultyName(
    faculty: FacultyResponse
  ): string {

    if (!faculty) {

      return '-';
    }


    if (
      faculty.fullName &&
      faculty.fullName.trim()
    ) {

      return faculty.fullName.trim();
    }


    const name = [

      faculty.firstName,

      faculty.middleName,

      faculty.lastName

    ]

      .filter(
        value =>
          !!value &&
          value.trim()
      )

      .join(' ')

      .trim();


    return name || '-';
  }


  // =====================================================
  // EMAIL
  // =====================================================

  getFacultyEmail(
    faculty: FacultyResponse
  ): string {

    if (!faculty) {

      return '-';
    }


    const data =
      faculty as any;


    return (
      data.email ||
      data.emailAddress ||
      data.user?.email ||
      data.userEmail ||
      '-'
    );
  }


  // =====================================================
  // DEPARTMENT
  // =====================================================

  getFacultyDepartment(
    faculty: FacultyResponse
  ): string {

    if (!faculty) {

      return '-';
    }


    return (
      faculty.departmentName ||
      '-'
    );
  }


  // =====================================================
  // DESIGNATION
  // =====================================================

  getFacultyDesignation(
    faculty: FacultyResponse
  ): string {

    if (!faculty) {

      return '-';
    }


    return (
      faculty.designation ||
      '-'
    );
  }


  // =====================================================
  // QUALIFICATION
  // =====================================================

  getFacultyQualification(
    faculty: FacultyResponse
  ): string {

    if (!faculty) {

      return '-';
    }


    return (
      faculty.qualification ||
      '-'
    );
  }


  // =====================================================
  // MOBILE
  // =====================================================

  getFacultyMobile(
    faculty: FacultyResponse
  ): string {

    if (!faculty) {

      return '-';
    }


    const data =
      faculty as any;


    return (
      faculty.mobileNumber ||
      data.mobile ||
      data.phoneNumber ||
      data.contactNumber ||
      '-'
    );
  }


  // =====================================================
  // STATUS
  // =====================================================

  getFacultyStatus(
    faculty: FacultyResponse
  ): string {

    if (!faculty) {

      return '-';
    }


    return (
      faculty.status ||
      '-'
    );
  }


  // =====================================================
  // STATUS LABEL
  // =====================================================

  getFacultyStatusLabel(
    faculty: FacultyResponse
  ): string {

    const status =
      this.getFacultyStatus(
        faculty
      );


    if (
      !status ||
      status === '-'
    ) {

      return '-';
    }


    return status
      .toLowerCase()
      .replace(
        /^./,
        char =>
          char.toUpperCase()
      );
  }


  // =====================================================
  // ACTIVE CHECK
  // =====================================================

  isActive(
    faculty: FacultyResponse
  ): boolean {

    const status =
      this.getFacultyStatus(
        faculty
      )
      .toUpperCase();


    return (
      status === 'ACTIVE' ||
      status === 'ACTIVATED' ||
      status === 'ENABLED'
    );
  }


  // =====================================================
  // STATUS CLASS
  // =====================================================

  getStatusClass(
    faculty: FacultyResponse
  ): string {

    const status =
      this.getFacultyStatus(
        faculty
      )
      .toUpperCase();


    switch (status) {

      case 'ACTIVE':

      case 'ACTIVATED':

      case 'ENABLED':

        return 'status-active';


      case 'INACTIVE':

      case 'DISABLED':

        return 'status-inactive';


      case 'DELETED':

        return 'status-deleted';


      default:

        return 'status-default';
    }
  }

}