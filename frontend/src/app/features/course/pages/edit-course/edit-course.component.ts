import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import {
  CourseFormComponent,
  DepartmentOption
} from '../../components/course-form/course-form.component';

import {
  CourseRequest,
  CourseResponse
} from '../../models/course.model';

import {
  CourseService
} from '../../services/course.service';

import {
  DepartmentService
} from '../../../department/services/department.service';

import {
  ToastService
} from '@app/shared/services/toast.service';


@Component({
  selector: 'app-edit-course',
  standalone: true,

  imports: [
    CourseFormComponent
  ],

  templateUrl: './edit-course.component.html',
  styleUrl: './edit-course.component.scss'
})
export class EditCourseComponent
  implements OnInit {

  // =====================================================
  // SERVICES
  // =====================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly courseService =
    inject(CourseService);

  private readonly departmentService =
    inject(DepartmentService);

  private readonly toast =
    inject(ToastService);


  // =====================================================
  // DATA
  // =====================================================

  course: CourseResponse | null = null;

  departments: DepartmentOption[] = [];

  courseId!: number;


  // =====================================================
  // UI STATE
  // =====================================================

  loading = false;

  loadingData = true;


  // =====================================================
  // LIFECYCLE
  // =====================================================

  ngOnInit(): void {

    const id = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (!id || id <= 0) {

      this.router.navigate([
        '/dashboard/course'
      ]);

      return;
    }

    this.courseId = id;

    this.loadData();
  }


  // =====================================================
  // LOAD COURSE + DEPARTMENTS
  // =====================================================

  private loadData(): void {

    this.loadingData = true;

    this.courseService
      .getCourseById(this.courseId)
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response?.data
          ) {

            this.course =
              response.data;

            this.loadDepartments();

          } else {

            this.loadingData = false;

            this.toast.error(
              response?.message ||
              'Course not found.'
            );
          }

        },

        error: error => {

          this.loadingData = false;

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to load course.'
          );
        }

      });
  }


  // =====================================================
  // LOAD DEPARTMENTS
  // =====================================================

  private loadDepartments(): void {

    this.departmentService
      .getAllDepartments(
        0,
        100,
        'departmentName',
        'asc'
      )
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response?.data
          ) {

            this.departments =
              response.data.content.map(
                item => ({
                  id: item.id,
                  departmentName:
                    item.departmentName
                })
              );

          } else {

            this.toast.error(
              response?.message ||
              'Unable to load departments.'
            );
          }

          this.loadingData = false;
        },

        error: error => {

          this.loadingData = false;

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to load departments.'
          );
        }

      });
  }


  // =====================================================
  // UPDATE COURSE
  // =====================================================

  update(
    request: CourseRequest
  ): void {

    if (
      this.loading ||
      !this.courseId
    ) {
      return;
    }

    this.loading = true;

    this.courseService
      .updateCourse(
        this.courseId,
        request
      )
      .subscribe({

        next: response => {

          this.loading = false;

          if (!response?.success) {

            this.toast.error(
              response?.message ||
              'Unable to update course.'
            );

            return;
          }

          this.toast.success(
            response?.message ||
            'Course updated successfully.'
          );

          this.router.navigate([
            '/dashboard/course'
          ]);
        },

        error: error => {

          this.loading = false;

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to update course.'
          );
        }

      });
  }


  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {

    if (this.loading) {
      return;
    }

    this.router.navigate([
      '/dashboard/course'
    ]);
  }

}