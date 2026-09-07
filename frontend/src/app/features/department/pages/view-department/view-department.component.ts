import {
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
  DepartmentResponse
} from '../../models/department.model';

import {
  DepartmentService
} from '../../services/department.service';

@Component({
  selector: 'app-view-department',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './view-department.component.html',
  styleUrl: './view-department.component.scss'
})
export class ViewDepartmentComponent
  implements OnInit {

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly departmentService =
    inject(DepartmentService);

  department: DepartmentResponse | null = null;

  loading = true;

  errorMessage = '';

  ngOnInit(): void {

    const id =
      Number(
        this.route.snapshot.paramMap.get('id')
      );

    if (!id || id <= 0) {

      this.errorMessage =
        'Invalid department ID.';

      this.loading = false;

      return;
    }

    this.loadDepartment(id);
  }

  private loadDepartment(id: number): void {

    this.loading = true;
    this.errorMessage = '';

    this.departmentService
      .getDepartmentById(id)
      .subscribe({

        next: response => {

          if (
            response.success &&
            response.data
          ) {

            this.department =
              response.data;

          } else {

            this.errorMessage =
              response.message ||
              'Department not found.';
          }

          this.loading = false;
        },

        error: error => {

          this.errorMessage =
            error?.error?.message ||
            'Unable to load department details.';

          this.loading = false;
        }

      });
  }

  editDepartment(): void {

    if (!this.department?.id) {
      return;
    }

    this.router.navigate([
      '/dashboard/department/edit',
      this.department.id
    ]);
  }

  goBack(): void {

    this.router.navigate([
      '/dashboard/department'
    ]);
  }

}