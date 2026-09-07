import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  SemesterFormComponent
} from '../../components/semester-form/semester-form.component';

import {
  SemesterRequest
} from '../../models/semester.model';

import {
  SemesterService
} from '../../services/semester.service';

import {
  ToastService
} from '@app/shared/services/toast.service';


@Component({
  selector: 'app-add-semester',
  standalone: true,
  imports: [
    SemesterFormComponent
  ],
  templateUrl: './add-semester.component.html',
  styleUrl: './add-semester.component.scss'
})
export class AddSemesterComponent implements OnInit {

  private readonly router =
    inject(Router);

  private readonly semesterService =
    inject(SemesterService);

  private readonly toastService =
    inject(ToastService);

  loading = false;

  successMessage = '';
  errorMessage = '';


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {
  }


  // =====================================================
  // CREATE SEMESTER
  // =====================================================

  createSemester(
    request: SemesterRequest
  ): void {

    this.clearMessages();

    this.loading = true;

    this.semesterService
      .createSemester(request)
      .subscribe({

        next: () => {

          this.loading = false;

          this.toastService.success(
            'Semester created successfully.'
          );

          this.router.navigate([
            '/dashboard/semester'
          ]);
        },

        error: (error: any) => {

          this.loading = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to create semester.';

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