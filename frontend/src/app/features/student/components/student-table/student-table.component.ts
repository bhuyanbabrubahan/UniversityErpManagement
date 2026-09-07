import {
  Component,
  input,
  output,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';

import { MatDialog } from '@angular/material/dialog';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { StudentResponse } from '../../models/student-response.model';
import { API } from '../../../../core/constants/api.constants';

import { StudentImageDialogComponent } from '../student-image-dialog/student-image-dialog.component';


@Component({
  selector: 'app-student-table',

  standalone: true,

  imports: [
    CommonModule,
    ...MATERIAL_MODULES
  ],

  templateUrl: './student-table.component.html',

  styleUrl: './student-table.component.scss'
})
export class StudentTableComponent {

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly dialog =
    inject(MatDialog);


  // =====================================================
  // STUDENT DATA
  // =====================================================

  readonly students =
    input.required<StudentResponse[]>();


  // =====================================================
  // LOADING
  // =====================================================

  readonly loading =
    input(false);


  // =====================================================
  // PAGINATION
  // =====================================================

  readonly totalElements =
    input(0);

  readonly pageSize =
    input(10);

  readonly pageIndex =
    input(0);


  // =====================================================
  // EVENTS
  // =====================================================

  readonly view =
    output<number>();

  readonly edit =
    output<number>();

  readonly delete =
    output<number>();

  readonly pageChange =
    output<PageEvent>();

  readonly sortChange =
    output<Sort>();


  // =====================================================
  // TABLE COLUMNS
  // =====================================================

  readonly displayedColumns = [
    'rollNumber',
    'student',
    'department',
    'gender',
    'status',
    'actions'
  ];


  // =====================================================
  // STUDENT NAME
  // =====================================================

  getStudentName(
    student: StudentResponse
  ): string {

    const data = student as any;

    /*
     * First priority:
     * Backend already provides fullName.
     */

    if (
      typeof data.fullName === 'string' &&
      data.fullName.trim()
    ) {

      return data.fullName.trim();
    }


    /*
     * Otherwise build name manually.
     */

    const firstName =
      typeof data.firstName === 'string'
        ? data.firstName.trim()
        : '';

    const middleName =
      typeof data.middleName === 'string'
        ? data.middleName.trim()
        : '';

    const lastName =
      typeof data.lastName === 'string'
        ? data.lastName.trim()
        : '';


    return [
      firstName,
      middleName,
      lastName
    ]
      .filter(Boolean)
      .join(' ') || '-';
  }


  // =====================================================
  // STUDENT EMAIL
  // =====================================================

  getStudentEmail(
    student: StudentResponse
  ): string {

    const data = student as any;

    /*
     * Try all commonly used backend structures.
     */

    const email =
      data.email ??
      data.emailAddress ??
      data.userEmail ??
      data.user?.email ??
      data.user?.emailAddress ??
      data.account?.email ??
      data.profile?.email;


    if (
      typeof email === 'string' &&
      email.trim()
    ) {

      return email.trim();
    }


    return '-';
  }


  // =====================================================
  // STUDENT IMAGE
  // =====================================================

  getProfileImage(
    student: StudentResponse
  ): string | null {

    const data = student as any;

    /*
     * Try all possible profile image fields.
     */

    const profileImage =
      data.profileImage ??
      data.profilePhoto ??
      data.profilePicture ??
      data.image ??
      data.imageUrl ??
      data.photo ??
      data.photoUrl ??
      data.user?.profileImage ??
      data.user?.profilePhoto ??
      data.user?.profilePicture ??
      data.user?.image ??
      data.user?.imageUrl ??
      data.account?.profileImage ??
      data.profile?.profileImage;


    if (
      typeof profileImage === 'string' &&
      profileImage.trim()
    ) {

      return profileImage.trim();
    }


    return null;
  }


  // =====================================================
  // PROFILE IMAGE URL
  // SAME AS FACULTY
  // =====================================================

  getProfileImageUrl(
    profileImage: string | null | undefined
  ): string {

    /*
     * No image.
     */

    if (!profileImage) {

      return 'images/default-avatar.png';
    }


    const image =
      profileImage.trim();


    /*
     * Already complete URL.
     */

    if (
      image.startsWith('http://') ||
      image.startsWith('https://') ||
      image.startsWith('data:image/')
    ) {

      return image;
    }


    /*
     * Backend may return a path.
     *
     * Example:
     *
     * /api/profile/photo/abc.jpg
     */

    if (
      image.startsWith('/api/profile/photo/')
    ) {

      return `${window.location.origin}${image}`;
    }


    /*
     * Backend may return:
     *
     * api/profile/photo/abc.jpg
     */

    if (
      image.startsWith('api/profile/photo/')
    ) {

      return `${window.location.origin}/${image}`;
    }


    /*
     * Backend may return only file name:
     *
     * abc.jpg
     *
     * Same endpoint used by Faculty.
     */

    return `${API.PROFILE}/photo/${encodeURIComponent(image)}`;
  }


  // =====================================================
  // STUDENT IMAGE URL
  // =====================================================

  getStudentImageUrl(
    student: StudentResponse
  ): string {

    const profileImage =
      this.getProfileImage(student);


    return this.getProfileImageUrl(
      profileImage
    );
  }


  // =====================================================
  // IMAGE ERROR FALLBACK
  // =====================================================

  onImageError(
    event: Event
  ): void {

    const image =
      event.target as HTMLImageElement;


    /*
     * Prevent infinite image-error loop.
     */

    if (
      image.dataset['fallback'] === 'true'
    ) {

      return;
    }


    image.dataset['fallback'] = 'true';

    image.src =
      'images/default-avatar.png';
  }


  // =====================================================
  // STUDENT INITIAL
  // =====================================================

  getStudentInitial(
    student: StudentResponse
  ): string {

    const name =
      this.getStudentName(student);


    if (
      name &&
      name !== '-'
    ) {

      return name
        .charAt(0)
        .toUpperCase();
    }


    return 'S';
  }


  // =====================================================
  // OPEN STUDENT IMAGE
  // =====================================================

  openStudentImage(
    student: StudentResponse
  ): void {

    const imageUrl =
      this.getStudentImageUrl(student);


    this.dialog.open(
      StudentImageDialogComponent,
      {

        width: '100%',

        maxWidth: '720px',

        maxHeight: '90vh',

        autoFocus: false,

        restoreFocus: false,

        data: {

          imageUrl,

          studentName:
            this.getStudentName(student)

        }

      }
    );
  }


  // =====================================================
  // DEPARTMENT
  // =====================================================

  getDepartmentName(
    student: StudentResponse
  ): string {

    const data = student as any;


    /*
     * Direct departmentName.
     */

    if (
      typeof data.departmentName === 'string' &&
      data.departmentName.trim()
    ) {

      return data.departmentName.trim();
    }


    /*
     * Sometimes backend uses:
     *
     * departmentName: { name: 'CSE' }
     */

    if (
      data.departmentName &&
      typeof data.departmentName === 'object'
    ) {

      const departmentName =
        data.departmentName.departmentName ??
        data.departmentName.name ??
        data.departmentName.title;


      if (
        typeof departmentName === 'string' &&
        departmentName.trim()
      ) {

        return departmentName.trim();
      }
    }


    /*
     * Department object.
     */

    const department =
      data.department;


    if (
      department === null ||
      department === undefined
    ) {

      return '-';
    }


    /*
     * Department already string.
     */

    if (
      typeof department === 'string'
    ) {

      return department.trim() || '-';
    }


    /*
     * Department object.
     */

    if (
      typeof department === 'object'
    ) {

      const name =
        department.departmentName ??
        department.name ??
        department.title ??
        department.department?.departmentName ??
        department.department?.name ??
        department.department?.title;


      if (
        typeof name === 'string' &&
        name.trim()
      ) {

        return name.trim();
      }
    }


    return '-';
  }


  // =====================================================
  // GENDER
  // =====================================================

  getGender(
    student: StudentResponse
  ): string {

    const data = student as any;


    const gender =
      data.gender ??
      data.genderName;


    if (
      typeof gender === 'string' &&
      gender.trim()
    ) {

      return gender.trim();
    }


    return '-';
  }


  // =====================================================
  // STATUS
  // =====================================================

  getStatus(
    student: StudentResponse
  ): string {

    const data = student as any;


    const status =
      data.status ??
      data.studentStatus ??
      data.activeStatus;


    return status || '-';
  }


  // =====================================================
  // STATUS ACTIVE CHECK
  // =====================================================

  isActive(
    student: StudentResponse
  ): boolean {

    const status =
      this.getStatus(student)
        .toUpperCase();


    return (
      status === 'ACTIVE' ||
      status === 'ACTIVATED' ||
      status === 'ENABLED'
    );
  }


  // =====================================================
  // VIEW
  // =====================================================

  viewStudent(
    id: number
  ): void {

    this.view.emit(id);
  }


  // =====================================================
  // EDIT
  // =====================================================

  editStudent(
    id: number
  ): void {

    this.edit.emit(id);
  }


  // =====================================================
  // DELETE
  // =====================================================

  deleteStudent(
    id: number
  ): void {

    this.delete.emit(id);
  }


  // =====================================================
  // PAGE
  // =====================================================

  onPageChange(
    event: PageEvent
  ): void {

    this.pageChange.emit(event);
  }


  // =====================================================
  // SORT
  // =====================================================

  onSortChange(
    event: Sort
  ): void {

    this.sortChange.emit(event);
  }

}