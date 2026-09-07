import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { ToastService } from '../../../../shared/services/toast.service';
import { AdmissionStatus } from '../../../../shared/enums/admission-staus.enum';
import { RecordStatus } from '../../../../shared/enums/record-status.enum';
import { AdmissionService } from '../../services/admission.service';
import { AdmissionResponse } from '../../models/admission.model';
import { formatAdmissionDate } from '../../shared/admission.utils';

@Component({
  selector: 'app-view-admission',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-admission.component.html',
  styleUrl: './view-admission.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ViewAdmissionComponent implements OnInit {
  private readonly service = inject(AdmissionService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);
  private readonly cdr = inject(ChangeDetectorRef);

  admission: AdmissionResponse | null = null;
  loading = false;

  readonly admissionStatuses = AdmissionStatus;
  readonly recordStatuses = RecordStatus;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!Number.isInteger(id) || id <= 0) {
      this.toast.error('Invalid admission ID.');
      this.router.navigate(['/dashboard/admissions']);
      return;
    }

    this.loading = true;

    this.service.getById(id).subscribe({
      next: response => {
        this.admission = response.data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Failed to load admission:', error);
        this.loading = false;
        this.toast.error('Failed to load admission.');
        this.router.navigate(['/dashboard/admissions']);
      }
    });
  }

  edit(): void {
    if (this.admission) {
      this.router.navigate(['/dashboard/admission/edit', this.admission.id]);
    }
  }

  back(): void {
    this.router.navigate(['/dashboard/admission']);
  }

  formatDate(value: string | null): string {
    return formatAdmissionDate(value);
  }
}
