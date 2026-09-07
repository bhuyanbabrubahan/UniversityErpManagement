import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { ToastService } from '../../../../shared/services/toast.service';
import { AdmissionService } from '../../services/admission.service';
import { AdmissionRequest } from '../../models/admission.model';
import { AdmissionFormComponent } from '../../components/admission-form/admission-form.component';

@Component({
  selector: 'app-add-admission',
  standalone: true,
  imports: [AdmissionFormComponent],
  templateUrl: './add-admission.component.html',
  styleUrl: './add-admission.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddAdmissionComponent {
  private readonly service = inject(AdmissionService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);

  submitting = false;

  submit(request: AdmissionRequest): void {
    this.submitting = true;

    this.service.create(request).subscribe({
      next: response => {
        this.submitting = false;
        this.toast.success(response.message || 'Admission created successfully.');
        this.router.navigate(['/dashboard/admission']);
      },
      error: error => {
        console.error('Failed to create admission:', error);
        this.submitting = false;
        this.toast.error('Failed to create admission.');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard/admission']);
  }
}
