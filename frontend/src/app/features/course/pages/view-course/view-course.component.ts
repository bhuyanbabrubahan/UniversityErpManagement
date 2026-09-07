import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import {
  CourseResponse
} from '../../models/course.model';

import {
  CourseService
} from '../../services/course.service';

@Component({
  selector: 'app-view-course',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-course.component.html',
  styleUrl: './view-course.component.scss'
})
export class ViewCourseComponent
  implements OnInit {

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly service =
    inject(CourseService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  course: CourseResponse | null = null;

  loading = true;

  errorMessage = '';

  ngOnInit(): void {

    const id = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (!id) {

      this.errorMessage =
        'Invalid course ID.';

      this.loading = false;

      return;
    }

    this.loadCourse(id);
  }

  private loadCourse(id: number): void {

    this.loading = true;

    this.service
      .getCourseById(id)
      .subscribe({

        next: response => {

          if (response.success) {

            this.course = response.data;

          } else {

            this.errorMessage =
              response.message ||
              'Unable to load course.';
          }

          this.loading = false;
          this.cdr.markForCheck();
        },

        error: error => {

          this.loading = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to load course.';

          this.cdr.markForCheck();
        }
      });
  }

  editCourse(): void {

    if (!this.course) {
      return;
    }

    this.router.navigate([
      '/dashboard/course/edit',
      this.course.id
    ]);
  }

  goBack(): void {

    this.router.navigate([
      '/dashboard/course'
    ]);
  }
}