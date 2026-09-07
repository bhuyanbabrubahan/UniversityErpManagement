import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  StudentFeePaymentResponse
} from '../../models/payment.model';

import {
  getPaymentModeLabel,
  getPaymentStatusClass,
  getPaymentStatusLabel
} from '../../shared/payment.utils';


@Component({
  selector: 'app-payment-table',

  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './payment-table.component.html',

  styleUrl: './payment-table.component.scss'
})
export class PaymentTableComponent {


  // =====================================================
  // INPUTS
  // =====================================================

  @Input()
  payments: StudentFeePaymentResponse[] = [];


  @Input()
  loading = false;


  @Input()
  errorMessage = '';


  @Input()
  page = 0;


  @Input()
  size = 10;


  @Input()
  totalElements = 0;


  @Input()
  totalPages = 0;


  @Input()
  last = true;


  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  view = new EventEmitter<number>();


  @Output()
  edit = new EventEmitter<number>();


  @Output()
  delete = new EventEmitter<number>();


  @Output()
  pageChange =
    new EventEmitter<number>();


  @Output()
  pageSizeChange =
    new EventEmitter<number>();


  @Output()
  sortChange =
    new EventEmitter<{
      sortBy: string;
      direction: 'asc' | 'desc';
    }>();


  // =====================================================
  // SORT
  // =====================================================

  sortBy = 'id';

  direction: 'asc' | 'desc' = 'desc';


  // =====================================================
  // PREVIOUS PAGE
  // =====================================================

  previousPage(): void {

    if (
      this.page <= 0 ||
      this.loading
    ) {

      return;

    }


    this.pageChange.emit(
      this.page - 1
    );

  }


  // =====================================================
  // NEXT PAGE
  // =====================================================

  nextPage(): void {

    if (
      this.last ||
      this.loading ||
      this.page >= this.totalPages - 1
    ) {

      return;

    }


    this.pageChange.emit(
      this.page + 1
    );

  }


  // =====================================================
  // PAGE SIZE
  // =====================================================

  changePageSize(
    event: Event
  ): void {

    const value = Number(
      (
        event.target as
        HTMLSelectElement
      ).value
    );


    if (
      !Number.isInteger(value) ||
      value <= 0
    ) {

      return;

    }


    this.pageSizeChange.emit(
      value
    );

  }


  // =====================================================
  // SORT
  // =====================================================

  sort(
    field: string
  ): void {

    if (this.loading) {

      return;

    }


    if (
      this.sortBy === field
    ) {

      this.direction =
        this.direction === 'asc'
          ? 'desc'
          : 'asc';

    } else {

      this.sortBy = field;

      this.direction = 'asc';

    }


    this.sortChange.emit({

      sortBy: this.sortBy,

      direction: this.direction

    });

  }


  // =====================================================
  // STATUS LABEL
  // =====================================================

  getStatusLabel(
    status:
      StudentFeePaymentResponse[
        'paymentStatus'
      ]
  ): string {

    return getPaymentStatusLabel(
      status
    );

  }


  // =====================================================
  // STATUS CLASS
  // =====================================================

  getStatusClass(
    status:
      StudentFeePaymentResponse[
        'paymentStatus'
      ]
  ): string {

    return getPaymentStatusClass(
      status
    );

  }


  // =====================================================
  // MODE LABEL
  // =====================================================

  getModeLabel(
    mode:
      StudentFeePaymentResponse[
        'paymentMode'
      ]
  ): string {

    return getPaymentModeLabel(
      mode
    );

  }


  // =====================================================
  // TRACK BY
  // =====================================================

  trackById(
    _index: number,
    payment: StudentFeePaymentResponse
  ): number {

    return payment.id;

  }


  // =====================================================
  // VIEW
  // =====================================================

  onView(
    id: number
  ): void {

    if (!id) {

      return;

    }


    this.view.emit(id);

  }


  // =====================================================
  // EDIT
  // =====================================================

  onEdit(
    id: number
  ): void {

    if (!id) {

      return;

    }


    this.edit.emit(id);

  }


  // =====================================================
  // DELETE
  // =====================================================

  onDelete(
    id: number
  ): void {

    if (!id) {

      return;

    }


    this.delete.emit(id);

  }

}