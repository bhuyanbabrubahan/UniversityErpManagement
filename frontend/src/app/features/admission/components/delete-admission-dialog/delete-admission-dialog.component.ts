import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { AdmissionResponse } from '../../models/admission.model';

@Component({
  selector: 'app-delete-admission-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './delete-admission-dialog.component.html',
  styleUrl: './delete-admission-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DeleteAdmissionDialogComponent {
  @Input() admission: AdmissionResponse | null = null;
  @Input() busy = false;

  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  confirm(): void {
    if (!this.busy) {
      this.confirmed.emit();
    }
  }

  cancel(): void {
    if (!this.busy) {
      this.cancelled.emit();
    }
  }
}
