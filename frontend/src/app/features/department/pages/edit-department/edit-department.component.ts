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
  DepartmentFormComponent
} from '../../components/department-form/department-form.component';

import {
  DepartmentRequest,
  DepartmentResponse
} from '../../models/department.model';

import {
  DepartmentService
} from '../../services/department.service';

import {
  ToastService
} from '@app/shared/services/toast.service';


@Component({
  selector: 'app-edit-department',
  standalone: true,

  imports: [
    DepartmentFormComponent
  ],

  templateUrl: './edit-department.component.html',
  styleUrl: './edit-department.component.scss'
})
export class EditDepartmentComponent
  implements OnInit {

  // =====================================================
  // SERVICES
  // =====================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly departmentService =
    inject(DepartmentService);

  private readonly toast =
    inject(ToastService);


  // =====================================================
  // DATA
  // =====================================================

  departmentId!: number;

  department: DepartmentResponse | null = null;


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
        '/dashboard/department'
      ]);

      return;
    }

    this.departmentId = id;

    this.loadDepartment();
  }


  // =====================================================
  // LOAD DEPARTMENT
  // =====================================================

  private loadDepartment(): void {

    this.loadingData = true;

    this.departmentService
      .getDepartmentById(this.departmentId)
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response?.data
          ) {

            this.department =
              response.data;

          } else {

            this.loadingData = false;

            this.toast.error(
              response?.message ||
              'Department not found.'
            );
          }

          this.loadingData = false;
        },

        error: error => {

          this.loadingData = false;

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to load department.'
          );
        }

      });
  }


  // =====================================================
  // UPDATE DEPARTMENT
  // =====================================================

  submit(
    request: DepartmentRequest
  ): void {

    if (
      this.loading ||
      !this.departmentId
    ) {
      return;
    }

    this.loading = true;

    this.departmentService
      .updateDepartment(
        this.departmentId,
        request
      )
      .subscribe({

        next: response => {

          this.loading = false;

          if (!response?.success) {

            this.toast.error(
              response?.message ||
              'Unable to update department.'
            );

            return;
          }

          this.toast.success(
            response?.message ||
            'Department updated successfully.'
          );

          this.router.navigate([
            '/dashboard/department'
          ]);
        },

        error: error => {

          this.loading = false;

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to update department.'
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
      '/dashboard/department'
    ]);
  }

}