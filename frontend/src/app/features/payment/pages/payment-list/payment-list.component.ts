import {
  Component,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { Router } from '@angular/router';

import { finalize } from 'rxjs';

import {
  PageResponse,
  StudentFeePaymentResponse,
  StudentFeePaymentSearchRequest
} from '../../models/payment.model';

import { PaymentSearchFilterComponent }
  from '../../components/payment-search-filter/payment-search-filter.component';

import { PaymentTableComponent }
  from '../../components/payment-table/payment-table.component';

import { DeletePaymentDialogComponent }
  from '../../components/delete-payment-dialog/delete-payment-dialog.component';

import { PaymentService }
  from '../../services/payment.service';

import { ToastService }
  from '@app/shared/services/toast.service';


@Component({
  selector: 'app-payment-list',

  standalone: true,

  imports: [
    CommonModule,
    PaymentSearchFilterComponent,
    PaymentTableComponent,
    DeletePaymentDialogComponent
  ],

  templateUrl: './payment-list.component.html',

  styleUrl: './payment-list.component.scss'
})
export class PaymentListComponent implements OnInit {


  // =========================================================
  // DATA
  // =========================================================

  payments: StudentFeePaymentResponse[] = [];


  // =========================================================
  // UI STATE
  // =========================================================

  loading = false;

  deleting = false;


  // =========================================================
  // PAGINATION
  // =========================================================

  currentPage = 0;

  pageSize = 10;

  totalElements = 0;

  totalPages = 0;


  // =========================================================
  // SORT
  // =========================================================

  sortBy = 'id';

  direction: 'asc' | 'desc' = 'desc';


  // =========================================================
  // SEARCH
  // =========================================================

  searchRequest: StudentFeePaymentSearchRequest = {};


  // =========================================================
  // DELETE DIALOG
  // =========================================================

  selectedPayment:
    StudentFeePaymentResponse | null = null;

  showDeleteDialog = false;


  // =========================================================
  // CONSTRUCTOR
  // =========================================================

  constructor(
    private readonly paymentService: PaymentService,
    private readonly router: Router,
    private readonly toastService: ToastService
  ) { }


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadPayments();

  }


  // =========================================================
  // LOAD ALL PAYMENTS
  // =========================================================

  loadPayments(): void {

    this.loading = true;

    this.paymentService
      .getAllPayments(
        this.currentPage,
        this.pageSize,
        this.sortBy,
        this.direction
      )
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response.data
          ) {

            this.applyPageResponse(
              response.data
            );

          } else {

            this.clearPaymentData();

            this.toastService.error(
              response?.message ||
              'Unable to load fee payments.'
            );

          }

        },

        error: error => {

          this.clearPaymentData();

          this.toastService.error(
            error?.error?.message ||
            error?.message ||
            'Unable to load fee payments.'
          );

        }

      });

  }


  // =========================================================
  // SEARCH PAYMENTS
  // =========================================================

  searchPayments(
    request: StudentFeePaymentSearchRequest
  ): void {

    this.searchRequest = {
      ...request
    };

    this.currentPage = 0;

    this.searchCurrentPage();

  }


  // =========================================================
  // SEARCH CURRENT PAGE
  // =========================================================

  private searchCurrentPage(): void {

    this.loading = true;

    this.paymentService
      .searchPayments(
        this.searchRequest,
        this.currentPage,
        this.pageSize,
        this.sortBy,
        this.direction
      )
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response.data
          ) {

            this.applyPageResponse(
              response.data
            );

          } else {

            this.clearPaymentData();

            this.toastService.error(
              response?.message ||
              'Unable to search fee payments.'
            );

          }

        },

        error: error => {

          this.clearPaymentData();

          this.toastService.error(
            error?.error?.message ||
            error?.message ||
            'Unable to search fee payments.'
          );

        }

      });

  }


  // =========================================================
  // CLEAR SEARCH
  // =========================================================

  clearSearch(): void {

    this.searchRequest = {};

    this.currentPage = 0;

    this.loadPayments();

  }


  // =========================================================
  // PAGINATION
  // =========================================================

  onPageChange(
    page: number
  ): void {

    if (
      page < 0 ||
      (
        this.totalPages > 0 &&
        page >= this.totalPages
      ) ||
      page === this.currentPage
    ) {

      return;

    }

    this.currentPage = page;

    if (this.hasActiveFilters()) {

      this.searchCurrentPage();

    } else {

      this.loadPayments();

    }

  }


  // =========================================================
  // PAGE SIZE
  // =========================================================

  onPageSizeChange(
    size: number
  ): void {

    if (
      !size ||
      size <= 0
    ) {

      return;

    }

    this.pageSize = size;

    this.currentPage = 0;

    if (this.hasActiveFilters()) {

      this.searchCurrentPage();

    } else {

      this.loadPayments();

    }

  }


  // =========================================================
  // SORT
  // =========================================================

  onSortChange(
    sortBy: string,
    direction: 'asc' | 'desc'
  ): void {

    this.sortBy = sortBy;

    this.direction = direction;

    this.currentPage = 0;

    if (this.hasActiveFilters()) {

      this.searchCurrentPage();

    } else {

      this.loadPayments();

    }

  }


  // =========================================================
  // NAVIGATION
  // =========================================================

  addPayment(): void {

    this.router.navigate([
      '/dashboard/payment/add'
    ]);

  }


  viewPayment(
    paymentId: number
  ): void {

    if (!paymentId) {
      return;
    }

    this.router.navigate([
      '/dashboard/payment/view',
      paymentId
    ]);

  }


  editPayment(
    paymentId: number
  ): void {

    if (!paymentId) {
      return;
    }

    this.router.navigate([
      '/dashboard/payment/edit',
      paymentId
    ]);

  }


  // =========================================================
  // DELETE DIALOG
  // =========================================================

  openDeleteDialog(
    paymentId: number
  ): void {

    if (!paymentId) {
      return;
    }

    const payment =
      this.payments.find(
        item => item.id === paymentId
      );

    this.selectedPayment =
      payment ??
      ({
        id: paymentId
      } as StudentFeePaymentResponse);

    this.showDeleteDialog = true;

  }


  // =========================================================
  // CLOSE DELETE DIALOG
  // =========================================================

  closeDeleteDialog(): void {

    if (this.deleting) {
      return;
    }

    this.selectedPayment = null;

    this.showDeleteDialog = false;

  }


  // =========================================================
  // CONFIRM DELETE
  // =========================================================

  confirmDelete(): void {

    if (
      !this.selectedPayment ||
      this.deleting
    ) {

      return;

    }

    const paymentId =
      this.selectedPayment.id;

    this.deleting = true;

    this.paymentService
      .deletePayment(paymentId)
      .pipe(
        finalize(() => {

          this.deleting = false;

        })
      )
      .subscribe({

        // =================================================
        // SUCCESS
        // =================================================

        next: response => {

          this.showDeleteDialog = false;

          this.selectedPayment = null;

          this.toastService.success(
            response?.message ||
            'Fee payment deleted successfully.'
          );


          // If current page has only one record
          // and it is not the first page,
          // move to previous page.

          if (
            this.payments.length === 1 &&
            this.currentPage > 0
          ) {

            this.currentPage--;

          }


          // Reload current list/search.

          this.refreshAfterDelete();

        },


        // =================================================
        // ERROR
        // =================================================

        error: error => {

          this.showDeleteDialog = false;

          this.selectedPayment = null;

          this.toastService.error(
            error?.error?.message ||
            error?.message ||
            'Unable to delete fee payment.'
          );

        }

      });

  }


  // =========================================================
  // REFRESH AFTER DELETE
  // =========================================================

  private refreshAfterDelete(): void {

    if (this.hasActiveFilters()) {

      this.searchCurrentPage();

    } else {

      this.loadPayments();

    }

  }


  // =========================================================
  // APPLY PAGE RESPONSE
  // =========================================================

  private applyPageResponse(
    data: PageResponse<StudentFeePaymentResponse>
  ): void {

    this.payments =
      data?.content ?? [];

    this.currentPage =
      data?.pageNumber ?? 0;

    this.pageSize =
      data?.pageSize ?? this.pageSize;

    this.totalElements =
      data?.totalElements ?? 0;

    this.totalPages =
      data?.totalPages ?? 0;

  }


  // =========================================================
  // CLEAR DATA
  // =========================================================

  private clearPaymentData(): void {

    this.payments = [];

    this.totalElements = 0;

    this.totalPages = 0;

  }


  // =========================================================
  // ACTIVE FILTER CHECK
  // =========================================================

  hasActiveFilters(): boolean {

    return Object.values(
      this.searchRequest
    ).some(
      value =>
        value !== null &&
        value !== undefined &&
        value !== ''
    );

  }


  // =========================================================
  // TRACK BY
  // =========================================================

  trackByPaymentId(
    _index: number,
    payment: StudentFeePaymentResponse
  ): number {

    return payment.id;

  }


  // =========================================================
  // RECORD RANGE
  // =========================================================

  getStartRecord(): number {

    if (this.totalElements === 0) {
      return 0;
    }

    return (
      this.currentPage *
      this.pageSize
    ) + 1;

  }


  // =========================================================
  // RECORD RANGE END
  // =========================================================

  getEndRecord(): number {

    return Math.min(
      (
        this.currentPage + 1
      ) * this.pageSize,
      this.totalElements
    );

  }

}