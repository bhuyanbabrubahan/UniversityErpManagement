import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';

import { RecordStatus } from '../../../../shared/enums/record-status.enum';
import { AdmissionStatus } from '../../../../shared/enums/admission-staus.enum';
import { AdmissionResponse } from '../../models/admission.model';
import { formatAdmissionDate } from '../../shared/admission.utils';

@Component({
  selector: 'app-admission-table',
  standalone: true,
  imports: [CommonModule, MatPaginatorModule],
  templateUrl: './admission-table.component.html',
  styleUrl: './admission-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdmissionTableComponent {
  @Input() admissions: AdmissionResponse[] = [];
  @Input() loading = false;
  @Input() deletingId: number | null = null;
  @Input() totalElements = 0;
  @Input() page = 0;
  @Input() size = 10;

  @Output() viewed = new EventEmitter<AdmissionResponse>();
  @Output() edited = new EventEmitter<AdmissionResponse>();
  @Output() deleted = new EventEmitter<AdmissionResponse>();
  @Output() pageChanged = new EventEmitter<PageEvent>();

  readonly admissionStatuses = AdmissionStatus;
  readonly recordStatuses = RecordStatus;

  formatDate(value: string | null): string {
    return formatAdmissionDate(value);
  }

  pageChange(event: PageEvent): void {
    this.pageChanged.emit(event);
  }
}
