import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { DepartmentFormComponent } from '../../components/department-form/department-form.component';
import { DepartmentRequest} from '../../models/department.model';
import { DepartmentService } from '../../services/department.service';
import { ToastService } from '@app/shared/services/toast.service';

@Component({
  selector: 'app-add-department',
  standalone: true,

  imports: [
    DepartmentFormComponent
  ],

  templateUrl: './add-department.component.html',
  styleUrl: './add-department.component.scss'
})
export class AddDepartmentComponent {

  // =====================================================
  // SERVICES
  // =====================================================

  private readonly departmentService = inject(DepartmentService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);

  // =====================================================
  // UI STATE
  // =====================================================

  loading = false;


  // =====================================================
  // SUBMIT
  // =====================================================

  submit(request: DepartmentRequest): void {

    if (this.loading) {
      return;
    }

    this.loading = true;


    this.departmentService
      .createDepartment(request)
      .subscribe({

        next: response => {

          this.loading = false;


          if (!response?.success) {

            this.toast.error(
              response?.message ||
              'Unable to create department.'
            );

            return;
          }


          this.toast.success(
            response?.message ||
            'Department created successfully.'
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
            'Unable to create department.'
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