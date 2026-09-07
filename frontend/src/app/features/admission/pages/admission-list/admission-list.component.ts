import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';

import { ToastService } from '../../../../shared/services/toast.service';
import { AdmissionService } from '../../services/admission.service';

import {
  AdmissionResponse,
  AdmissionSearchRequest
} from '../../models/admission.model';

import { ADMISSION_DEFAULT_PAGE_SIZE } from '../../shared/admission.constants';

import { AdmissionSearchFilterComponent } from '../../components/admission-search-filter/admission-search-filter.component';
import { AdmissionTableComponent } from '../../components/admission-table/admission-table.component';
import { DeleteAdmissionDialogComponent } from '../../components/delete-admission-dialog/delete-admission-dialog.component';


@Component({
  selector: 'app-admission-list',
  standalone: true,

  imports: [
    CommonModule,
    AdmissionSearchFilterComponent,
    AdmissionTableComponent,
    DeleteAdmissionDialogComponent
  ],

  templateUrl: './admission-list.component.html',
  styleUrl: './admission-list.component.scss',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdmissionListComponent implements OnInit {

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly service =
    inject(AdmissionService);

  private readonly router =
    inject(Router);

  private readonly toast =
    inject(ToastService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =====================================================
  // TABLE STATE
  // =====================================================

  admissions: AdmissionResponse[] = [];

  loading = false;

  deletingId: number | null = null;


  // =====================================================
  // PAGINATION
  // =====================================================

  page = 0;

  size =
    ADMISSION_DEFAULT_PAGE_SIZE;

  totalElements = 0;


  // =====================================================
  // SEARCH
  // =====================================================

  private searchRequest:
    AdmissionSearchRequest | null = null;


  // =====================================================
  // DELETE DIALOG
  // =====================================================

  showDeleteDialog = false;

  selectedAdmission:
    AdmissionResponse | null = null;


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {
    this.load();
  }


  // =====================================================
  // LOAD ADMISSIONS
  // =====================================================

  load(): void {

    // -----------------------------------------------
    // Start loading
    // -----------------------------------------------

    this.loading = true;

    this.cdr.markForCheck();


    // -----------------------------------------------
    // Determine API request
    // -----------------------------------------------

    const request =
      this.searchRequest;


    const observable = request

      ? this.service.search(
          request,
          this.page,
          this.size,
          'id',
          'desc'
        )

      : this.service.getAll(
          this.page,
          this.size,
          'id',
          'desc'
        );


    // -----------------------------------------------
    // Subscribe
    // -----------------------------------------------

    observable.subscribe({

      // =============================================
      // SUCCESS
      // =============================================

      next: response => {

        console.log(
          'Admissions API response:',
          response
        );


        const data =
          response?.data;


        // -------------------------------------------
        // Update table data
        // -------------------------------------------

        this.admissions =
          data?.content ?? [];


        // -------------------------------------------
        // Update pagination
        // -------------------------------------------

        this.totalElements =
          data?.totalElements ?? 0;


        // -------------------------------------------
        // Stop loading
        // -------------------------------------------

        this.loading = false;


        // -------------------------------------------
        // IMPORTANT FOR OnPush
        // -------------------------------------------

        this.cdr.markForCheck();

      },


      // =============================================
      // ERROR
      // =============================================

      error: error => {

        console.error(
          'Failed to load admissions:',
          error
        );


        // -------------------------------------------
        // Reset state
        // -------------------------------------------

        this.admissions = [];

        this.totalElements = 0;

        this.loading = false;


        // -------------------------------------------
        // Toast
        // -------------------------------------------

        this.toast.error(
          'Failed to load admissions.'
        );


        // -------------------------------------------
        // IMPORTANT FOR OnPush
        // -------------------------------------------

        this.cdr.markForCheck();

      }

    });

  }


  // =====================================================
  // SEARCH
  // =====================================================

  onSearch(
    request: AdmissionSearchRequest
  ): void {

    this.page = 0;

    this.searchRequest = request;

    this.load();

  }


  // =====================================================
  // CLEAR SEARCH
  // =====================================================

  onClear(): void {

    this.page = 0;

    this.searchRequest = null;

    this.load();

  }


  // =====================================================
  // PAGINATION
  // =====================================================

  onPageChange(
    event: PageEvent
  ): void {

    this.page =
      event.pageIndex;

    this.size =
      event.pageSize;

    this.load();

  }


  // =====================================================
  // ADD ADMISSION
  // =====================================================

  add(): void {

    this.router.navigate([
      '/dashboard/admission/add'
    ]);

  }


  // =====================================================
  // VIEW ADMISSION
  // =====================================================

  view(
    admission: AdmissionResponse
  ): void {

    this.router.navigate([
      '/dashboard/admission/view',
      admission.id
    ]);

  }


  // =====================================================
  // EDIT ADMISSION
  // =====================================================

  edit(
    admission: AdmissionResponse
  ): void {

    this.router.navigate([
      '/dashboard/admission/edit',
      admission.id
    ]);

  }


  // =====================================================
  // REQUEST DELETE
  // =====================================================

  requestDelete(
    admission: AdmissionResponse
  ): void {

    // -----------------------------------------------
    // Prevent multiple delete requests
    // -----------------------------------------------

    if (
      this.deletingId !== null
    ) {
      return;
    }


    this.selectedAdmission =
      admission;

    this.showDeleteDialog =
      true;


    this.cdr.markForCheck();

  }


  // =====================================================
  // CANCEL DELETE
  // =====================================================

  cancelDelete(): void {

    // -----------------------------------------------
    // Don't close while deleting
    // -----------------------------------------------

    if (
      this.deletingId !== null
    ) {
      return;
    }


    this.showDeleteDialog =
      false;

    this.selectedAdmission =
      null;


    this.cdr.markForCheck();

  }


  // =====================================================
  // CONFIRM DELETE
  // =====================================================

  confirmDelete(): void {

    const admission =
      this.selectedAdmission;


    // -----------------------------------------------
    // Validation
    // -----------------------------------------------

    if (
      !admission ||
      this.deletingId !== null
    ) {
      return;
    }


    // -----------------------------------------------
    // Start deleting
    // -----------------------------------------------

    this.deletingId =
      admission.id;


    this.cdr.markForCheck();


    // -----------------------------------------------
    // API
    // -----------------------------------------------

    this.service
      .cancel(admission.id)
      .subscribe({

        // ===========================================
        // SUCCESS
        // ===========================================

        next: () => {

          this.deletingId =
            null;

          this.showDeleteDialog =
            false;

          this.selectedAdmission =
            null;


          this.toast.success(
            'Admission cancelled successfully.'
          );


          // -----------------------------------------
          // Reload table
          // -----------------------------------------

          this.load();


          this.cdr.markForCheck();

        },


        // ===========================================
        // ERROR
        // ===========================================

        error: error => {

          console.error(
            'Failed to cancel admission:',
            error
          );


          this.deletingId =
            null;


          this.toast.error(
            'Failed to cancel admission.'
          );


          this.cdr.markForCheck();

        }

      });

  }

}
