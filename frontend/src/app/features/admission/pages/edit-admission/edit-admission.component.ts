import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ToastService } from '../../../../shared/services/toast.service';
import { AdmissionService } from '../../services/admission.service';
import { AdmissionRequest, AdmissionResponse } from '../../models/admission.model';
import { AdmissionFormComponent } from '../../components/admission-form/admission-form.component';

@Component({
  selector: 'app-edit-admission',
  standalone: true,
  imports: [AdmissionFormComponent],
  templateUrl: './edit-admission.component.html',
  styleUrl: './edit-admission.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EditAdmissionComponent implements OnInit {
  private readonly service = inject(AdmissionService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);
  private readonly cdr = inject(ChangeDetectorRef);

  admission: AdmissionResponse | null = null;
  loading = false;
  submitting = false;

  private id: number | null = null;

  ngOnInit(): void {
    const rawId = Number(this.route.snapshot.paramMap.get('id'));

    if (!Number.isInteger(rawId) || rawId <= 0) {
      this.toast.error('Invalid admission ID.');
      this.router.navigate(['/dashboard/admission']);
      return;
    }

    this.id = rawId;
    this.load();
  }

  load(): void {
    if (!this.id) {
      return;
    }

    this.loading = true;

    this.service.getById(this.id).subscribe({
      next: response => {
        this.admission = response.data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Failed to load admission:', error);
        this.loading = false;
        this.toast.error('Failed to load admission.');
        this.router.navigate(['/dashboard/admission']);
      }
    });
  }

  submit(request: AdmissionRequest): void {
    if (!this.id) {
      return;
    }

    this.submitting = true;

    this.service.update(this.id, request).subscribe({
      next: response => {
        this.submitting = false;
        this.toast.success(response.message || 'Admission updated successfully.');
        this.router.navigate(['/dashboard/admission/view', this.id]);
      },
      error: error => {
        console.error('Failed to update admission:', error);
        this.submitting = false;
        this.toast.error('Failed to update admission.');
      }
    });
  }

  cancel(): void {
    if (this.id) {
      this.router.navigate(['/dashboard/admission']);
    } else {
      this.router.navigate(['/dashboard/admission']);
    }
  }
}
