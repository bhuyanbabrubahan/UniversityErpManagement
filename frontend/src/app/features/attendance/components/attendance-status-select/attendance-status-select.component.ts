import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';

import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';
import { statusLabel } from '../../shared/attendance.utils';

@Component({
  selector: 'app-attendance-status-select',
  standalone: true,
  templateUrl: './attendance-status-select.component.html',
  styleUrl: './attendance-status-select.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceStatusSelectComponent {

  // ============================================================
  // CURRENT VALUE
  // ============================================================

  private _value: AttendanceStatus | string | null | undefined;

  @Input({ required: true })
  set value(value: AttendanceStatus | string | null | undefined) {

    this._value = value;

    this.selectedValue =
      this.normalizeStatus(value);
  }

  get value(): AttendanceStatus | string | null | undefined {
    return this._value;
  }


  // ============================================================
  // DISABLED STATE
  // ============================================================

  @Input()
  disabled = false;


  // ============================================================
  // VALUE CHANGE
  // ============================================================

  @Output()
  readonly valueChange =
    new EventEmitter<AttendanceStatus>();


  // ============================================================
  // AVAILABLE STATUSES
  // ============================================================

  readonly statuses: AttendanceStatus[] =
    Object.values(AttendanceStatus);


  readonly statusLabel =
    statusLabel;


  // ============================================================
  // NORMALIZED SELECTED VALUE
  // ============================================================

  selectedValue:
    AttendanceStatus | null = null;


  // ============================================================
  // STATUS CHANGE
  // ============================================================

  onStatusChange(event: Event): void {

    if (this.disabled) {
      return;
    }

    const select =
      event.target as HTMLSelectElement;

    const status =
      this.normalizeStatus(select.value);

    if (!status) {
      return;
    }

    this.selectedValue = status;

    this._value = status;

    this.valueChange.emit(status);
  }


  // ============================================================
  // STATUS NORMALIZATION
  // ============================================================

  private normalizeStatus(
    value:
      AttendanceStatus |
      string |
      null |
      undefined
  ): AttendanceStatus | null {

    const normalized =
      String(value ?? '')
        .trim()
        .toUpperCase()
        .replace(/[\s-]+/g, '_');

    switch (normalized) {

      case 'PRESENT':
        return AttendanceStatus.PRESENT;

      case 'ABSENT':
        return AttendanceStatus.ABSENT;

      case 'LEAVE':
        return AttendanceStatus.LEAVE;

      case 'HALF_DAY':
        return AttendanceStatus.HALF_DAY;

      default:
        return null;
    }
  }
}