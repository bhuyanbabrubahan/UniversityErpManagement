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
  SemesterResponse
} from '../../models/semester.model';

import {
  SemesterService
} from '../../services/semester.service';

import {
  ToastService
} from '@app/shared/services/toast.service';


@Component({
  selector: 'app-view-semester',
  standalone: true,
  imports: [],
  templateUrl: './view-semester.component.html',
  styleUrl: './view-semester.component.scss'
})
export class ViewSemesterComponent implements OnInit {

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly semesterService =
    inject(SemesterService);

  private readonly toastService =
    inject(ToastService);


  semester: SemesterResponse | null = null;

  loading = true;


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    const id = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (!id || id <= 0) {

      this.loading = false;

      this.toastService.error(
        'Invalid semester id.'
      );

      return;
    }

    this.loadSemester(id);
  }


  // =====================================================
  // LOAD SEMESTER
  // =====================================================

  private loadSemester(
    id: number
  ): void {

    this.loading = true;

    this.semesterService
      .getSemesterById(id)
      .subscribe({

        next: (response: SemesterResponse) => {

          this.semester = response;

          this.loading = false;
        },

        error: (error: any) => {

          this.loading = false;

          this.toastService.error(
            error?.error?.message ||
            'Unable to load semester.'
          );
        }

      });
  }


  // =====================================================
  // EDIT
  // =====================================================

  edit(): void {

    if (!this.semester) {
      return;
    }

    this.router.navigate([
      '/dashboard/semester/edit',
      this.semester.id
    ]);
  }


  // =====================================================
  // BACK
  // =====================================================

  back(): void {

    this.router.navigate([
      '/dashboard/semester'
    ]);
  }

}