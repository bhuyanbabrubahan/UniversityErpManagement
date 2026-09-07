import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  CourseFormComponent,
  DepartmentOption
} from '../../components/course-form/course-form.component';

import {
  CourseRequest
} from '../../models/course.model';

import {
  CourseService
} from '../../services/course.service';

import {
  DepartmentService
} from '../../../department/services/department.service';
import { ToastService } from '@app/shared/services/toast.service';

@Component({
  selector: 'app-add-course',
  standalone: true,
  imports: [CourseFormComponent],
  templateUrl: './add-course.component.html',
  styleUrl: './add-course.component.scss'
})
export class AddCourseComponent
  implements OnInit {

  private readonly courseService = inject(CourseService);

  private readonly departmentService = inject(DepartmentService);

  private readonly router = inject(Router);

  private readonly cdr = inject(ChangeDetectorRef);
  private readonly toast = inject(ToastService);

  departments: DepartmentOption[] = [];

  loading = false;
  loadingDepartments = true;

  errorMessage = '';

  ngOnInit(): void {
    this.loadDepartments();
  }

  loadDepartments(): void {

    this.loadingDepartments = true;

    this.departmentService
      .getAllDepartments(0, 100, 'departmentName', 'asc')
      .subscribe({

        next: response => {

          if (response.success && response.data) {

            this.departments =
              response.data.content.map(item => ({
                id: item.id,
                departmentName: item.departmentName
              }));

          } else {

            this.errorMessage =
              response.message ||
              'Unable to load departments.';
          }

          this.loadingDepartments = false;
          this.cdr.markForCheck();
        },

        error: error => {

          this.loadingDepartments = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to load departments.';

          this.cdr.markForCheck();
        }
      });
  }

  save(request: CourseRequest): void {

  if (this.loading) {
    return;
  }

  this.loading = true;

  this.courseService
    .createCourse(request)
    .subscribe({

      next: response => {

        this.loading = false;

        if (response.success) {

          this.toast.success(
            response.message ||
            'Course created successfully.'
          );

          this.router.navigate([
            '/dashboard/course'
          ]);

        } else {

          this.toast.error(
            response.message ||
            'Unable to create course.'
          );
        }
      },

      error: error => {

        this.loading = false;

        this.toast.error(
          error?.error?.message ||
          error?.message ||
          'Unable to create course.'
        );
      }
    });
}

  cancel(): void {
    this.router.navigate([
      '/dashboard/course'
    ]);
  }
}