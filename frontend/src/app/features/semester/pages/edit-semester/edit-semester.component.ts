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
  SemesterFormComponent
} from '../../components/semester-form/semester-form.component';

import {
  SemesterRequest,
  SemesterResponse
} from '../../models/semester.model';

import {
  SemesterService
} from '../../services/semester.service';

import {
  ToastService
} from '@app/shared/services/toast.service';


@Component({
  selector: 'app-edit-semester',
  standalone: true,
  imports: [
    SemesterFormComponent
  ],
  templateUrl: './edit-semester.component.html',
  styleUrl: './edit-semester.component.scss'
})
export class EditSemesterComponent implements OnInit {

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly semesterService =
    inject(SemesterService);

  private readonly toastService =
    inject(ToastService);


  semester: SemesterResponse | null = null;

  loadingData = true;
  loading = false;

  successMessage = '';
  errorMessage = '';

  private semesterId!: number;


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    const id = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (!id || id <= 0) {

      this.errorMessage =
        'Invalid semester id.';

      this.loadingData = false;

      return;
    }

    this.semesterId = id;

    this.loadSemester();
  }


  // =====================================================
  // LOAD SEMESTER
  // =====================================================

  private loadSemester(): void {

    this.loadingData = true;

    this.semesterService
      .getSemesterById(this.semesterId)
      .subscribe({

        next: (response: SemesterResponse) => {

          this.semester = response;

          this.loadingData = false;
        },

        error: (error: any) => {

          this.loadingData = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to load semester.';

          this.toastService.error(
            this.errorMessage
          );
        }

      });
  }


  // =====================================================
  // UPDATE
  // =====================================================

  updateSemester(
    request: SemesterRequest
  ): void {

    this.clearMessages();

    this.loading = true;

    this.semesterService
      .updateSemester(
        this.semesterId,
        request
      )
      .subscribe({

        next: () => {

          this.loading = false;

          this.toastService.success(
            'Semester updated successfully.'
          );

          this.router.navigate([
            '/dashboard/semester'
          ]);
        },

        error: (error: any) => {

          this.loading = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to update semester.';

          this.toastService.error(
            this.errorMessage
          );
        }

      });
  }


  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {

    this.router.navigate([
      '/dashboard/semester'
    ]);
  }


  // =====================================================
  // CLEAR MESSAGES
  // =====================================================

  private clearMessages(): void {

    this.successMessage = '';
    this.errorMessage = '';
  }

}