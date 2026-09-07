import {
  Component,
  inject,
  signal
} from '@angular/core';

import { Router } from '@angular/router';

import { PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { StudentService } from '../../services/student.service';
import { StudentResponse } from '../../models/student-response.model';
import { StudentSearchRequest } from '../../models/student-search-request.model';

import { StudentTableComponent } from '../../components/student-table/student-table.component';
import { StudentSearchFilterComponent } from '../../components/student-search-filter/student-search-filter.component';

import { RouterLink } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

import { DeleteStudentDialogComponent } from '../../components/delete-student-dialog/delete-student-dialog.component';

import { ToastService } from '@app/shared/services/toast.service';


@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [
    RouterLink,
    ...MATERIAL_MODULES,
    StudentTableComponent,
    StudentSearchFilterComponent
  ],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.scss'
})
export class StudentListComponent {

  // ==========================================================
  // SERVICES
  // ==========================================================

  private readonly studentService =
    inject(StudentService);

  private readonly router =
    inject(Router);

  private readonly dialog =
    inject(MatDialog);

  private readonly toast =
    inject(ToastService);


  // ==========================================================
  // STUDENT LIST DATA
  // ==========================================================

  readonly students =
    signal<StudentResponse[]>([]);


  // ==========================================================
  // LOADING STATE
  // ==========================================================

  readonly loading =
    signal(false);


  // ==========================================================
  // TOTAL RECORDS
  // ==========================================================

  readonly totalElements =
    signal(0);


  // ==========================================================
  // PAGINATION
  // ==========================================================

  readonly pageNumber =
    signal(0);

  readonly pageSize =
    signal(5);


  // ==========================================================
  // SORTING
  // ==========================================================

  readonly sortBy =
    signal('id');

  readonly direction =
    signal('asc');


  // ==========================================================
  // SEARCH
  // ==========================================================

  readonly searchRequest =
    signal<StudentSearchRequest | null>(null);


  // ==========================================================
  // INIT
  // ==========================================================

  ngOnInit(): void {
    this.loadStudents();
  }


  // ==========================================================
  // LOAD NORMAL STUDENT LIST
  // ==========================================================

  loadStudents(): void {

    this.loading.set(true);

    this.studentService
      .getAllStudents(
        this.pageNumber(),
        this.pageSize(),
        this.sortBy(),
        this.direction()
      )
      .subscribe({

        next: response => {

          this.students.set(
            response.data.content
          );

          this.totalElements.set(
            response.data.totalElements
          );

          this.loading.set(false);

        },

        error: error => {

          this.loading.set(false);

          console.error(
            'Unable to load students:',
            error
          );

          this.toast.error(
            error?.error?.message ??
            'Unable to load students.'
          );

        }

      });

  }


  // ==========================================================
  // LOAD SEARCHED STUDENTS
  // ==========================================================

  loadSearchStudents(): void {

    this.loading.set(true);

    this.studentService
      .searchStudents(
        this.searchRequest()!,
        this.pageNumber(),
        this.pageSize(),
        this.sortBy(),
        this.direction()
      )
      .subscribe({

        next: response => {

          this.students.set(
            response.data.content
          );

          this.totalElements.set(
            response.data.totalElements
          );

          this.loading.set(false);

        },

        error: error => {

          this.loading.set(false);

          console.error(
            'Unable to search students:',
            error
          );

          this.toast.error(
            error?.error?.message ??
            'Unable to search students.'
          );

        }

      });

  }


  // ==========================================================
  // SEARCH
  // ==========================================================

  onSearch(
    request: StudentSearchRequest
  ): void {

    this.searchRequest.set(request);

    this.pageNumber.set(0);

    this.loadSearchStudents();

  }


  // ==========================================================
  // RESET SEARCH
  // ==========================================================

  onReset(): void {

    this.searchRequest.set(null);

    this.pageNumber.set(0);

    this.loadStudents();

  }


  // ==========================================================
  // PAGINATION
  // ==========================================================

  onPageChange(
    event: PageEvent
  ): void {

    this.pageNumber.set(
      event.pageIndex
    );

    this.pageSize.set(
      event.pageSize
    );

    if (this.searchRequest()) {

      this.loadSearchStudents();

    } else {

      this.loadStudents();

    }

  }


  // ==========================================================
  // SORTING
  // ==========================================================

  onSortChange(
    event: Sort
  ): void {

    this.sortBy.set(
      event.active
    );

    this.direction.set(
      event.direction || 'asc'
    );

    if (this.searchRequest()) {

      this.loadSearchStudents();

    } else {

      this.loadStudents();

    }

  }


  // ==========================================================
  // VIEW STUDENT
  // ==========================================================

  viewStudent(
    id: number
  ): void {

    if (!id || id <= 0) {
      return;
    }

    this.router.navigate([
      '/dashboard/students',
      id
    ]);

  }


  // ==========================================================
  // EDIT STUDENT
  // ==========================================================

  editStudent(
    id: number
  ): void {

    if (!id || id <= 0) {
      return;
    }

    this.router.navigate([
      '/dashboard/students/edit',
      id
    ]);

  }


  // ==========================================================
  // DELETE STUDENT
  // ==========================================================

  deleteStudent(
    id: number
  ): void {

    if (!id || id <= 0) {
      return;
    }


    const dialogRef =
      this.dialog.open(
        DeleteStudentDialogComponent,
        {
          width: '400px'
        }
      );


    dialogRef.afterClosed()
      .subscribe(result => {

        if (!result) {
          return;
        }


        this.studentService
          .deleteStudent(id)
          .subscribe({

            // ==================================================
            // SUCCESS
            // ==================================================

            next: response => {

              this.toast.success(
                response?.message ??
                'Student deleted successfully.'
              );

              if (this.searchRequest()) {

                this.loadSearchStudents();

              } else {

                this.loadStudents();

              }

            },


            // ==================================================
            // ERROR
            // ==================================================

            error: error => {

              console.error(
                'Student Delete Error:',
                error
              );

              this.toast.error(
                error?.error?.message ??
                'Unable to delete student.'
              );

            }

          });

      });

  }

}