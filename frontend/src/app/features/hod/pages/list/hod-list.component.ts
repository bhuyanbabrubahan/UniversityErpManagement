import { Component, inject, OnInit, signal } from "@angular/core";

import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";

import { HodService } from "../../services/hod.service";
import { Hod } from "../../models/hod.model";
import { HodSearch } from "../../models/hod-search.model";
import { HodPage } from "../../models/hod-page.model";

import { HodSearchFilterComponent } from "../../components/hod-search-filter.component";
import { HodTableComponent } from "../../components/hod-table.component";

import { ToastService } from "@app/shared/services/toast.service";
import {
  DeleteHodDialogComponent,
  DeleteHodDialogData,
} from "../../components/delete-hod-dialog.component";

@Component({
  selector: "app-hod-list",
  standalone: true,

  imports: [
    CommonModule,
    RouterLink,
    HodSearchFilterComponent,
    HodTableComponent,
  ],

  templateUrl: "./hod-list.component.html",
  styleUrl: "./hod-list.component.scss",
})
export class HodListComponent implements OnInit {
  private readonly service = inject(HodService);

  private readonly toast = inject(ToastService);

  private readonly dialog = inject(MatDialog);

  // =====================================================
  // TABLE DATA
  // =====================================================

  readonly rows = signal<Hod[]>([]);

  readonly loading = signal(false);

  // =====================================================
  // PAGINATION
  // =====================================================

  readonly page = signal(0);

  readonly size = signal(10);

  readonly totalElements = signal(0);

  readonly totalPages = signal(0);

  // =====================================================
  // CURRENT SEARCH
  // =====================================================

  readonly currentSearch = signal<HodSearch | undefined>(undefined);

  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {
    this.load();
  }

  // =====================================================
  // LOAD
  // =====================================================

  load(search?: HodSearch): void {
    // ---------------------------------------------------
    // New search
    // ---------------------------------------------------

    if (search !== undefined) {
      this.currentSearch.set(search);

      this.page.set(0);
    }

    // ---------------------------------------------------
    // Loading
    // ---------------------------------------------------

    this.loading.set(true);

    const currentPage = this.page();

    const currentSize = this.size();

    const searchValue = this.currentSearch();

    // ---------------------------------------------------
    // Request
    // ---------------------------------------------------

    const request$ = searchValue
      ? this.service.search(searchValue, currentPage, currentSize)
      : this.service.list(currentPage, currentSize);

    // ---------------------------------------------------
    // Subscribe
    // ---------------------------------------------------

    request$.subscribe({
      next: (response) => {
        const data: HodPage | null = response?.data ?? null;

        // -------------------------------------------------
        // Rows
        // -------------------------------------------------

        this.rows.set(data?.content ?? []);

        // -------------------------------------------------
        // Pagination
        // -------------------------------------------------

        this.totalElements.set(data?.totalElements ?? 0);

        this.totalPages.set(data?.totalPages ?? 0);

        // -------------------------------------------------
        // Loading complete
        // -------------------------------------------------

        this.loading.set(false);
      },

      error: (error) => {
        console.error("Failed to load HOD records:", error);

        this.rows.set([]);

        this.totalElements.set(0);

        this.totalPages.set(0);

        this.loading.set(false);

        this.toast.error(
          error?.error?.message || "Unable to load HOD records.",
        );
      },
    });
  }

  // =====================================================
  // PAGE
  // =====================================================

  changePage(page: number): void {
    if (page < 0 || page >= this.totalPages() || page === this.page()) {
      return;
    }

    this.page.set(page);

    this.load();
  }

  // =====================================================
  // PREVIOUS PAGE
  // =====================================================

  previousPage(): void {
    if (this.page() > 0) {
      this.page.set(this.page() - 1);

      this.load();
    }
  }

  // =====================================================
  // NEXT PAGE
  // =====================================================

  nextPage(): void {
    if (this.page() + 1 < this.totalPages()) {
      this.page.set(this.page() + 1);

      this.load();
    }
  }

  // =====================================================
  // PAGE SIZE
  // =====================================================

  changeSize(event: Event): void {
    const target = event.target as HTMLSelectElement;

    const value = Number(target.value);

    if (!Number.isInteger(value) || value <= 0) {
      return;
    }

    this.size.set(value);

    this.page.set(0);

    this.load();
  }

  // =====================================================
  // DELETE
  // =====================================================

  remove(id: number): void {
    // ---------------------------------------------------
    // Validate ID
    // ---------------------------------------------------

    if (!Number.isInteger(id) || id <= 0) {
      this.toast.error("Invalid HOD ID.");

      return;
    }

    // ---------------------------------------------------
    // Find current row
    // ---------------------------------------------------

    const row = this.rows().find((item) => item.id === id);

    if (!row) {
      this.toast.error("HOD record not found.");

      return;
    }

    // ---------------------------------------------------
    // Dialog data
    // ---------------------------------------------------

    const data: DeleteHodDialogData = {
      code: row.code,
      fullName: row.fullName,
      department: row.department,
    };

    // ---------------------------------------------------
    // Open confirmation dialog
    // ---------------------------------------------------

    const dialogRef = this.dialog.open(DeleteHodDialogComponent, {
      width: "500px",
      maxWidth: "95vw",
      data,
      disableClose: true,
    });

    // ---------------------------------------------------
    // Dialog result
    // ---------------------------------------------------

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) {
        return;
      }

      this.deleteConfirmed(id);
    });
  }

  // =====================================================
  // DELETE CONFIRMED
  // =====================================================

  private deleteConfirmed(id: number): void {
    this.loading.set(true);

    this.service.delete(id).subscribe({
      next: () => {
        this.toast.success("HOD deleted successfully.");

        // ------------------------------------------------
        // If last record of current page was deleted,
        // move to previous page.
        // ------------------------------------------------

        if (this.rows().length === 1 && this.page() > 0) {
          this.page.set(this.page() - 1);
        }

        // ------------------------------------------------
        // Reload current search/page
        // ------------------------------------------------

        this.load();
      },

      error: (error) => {
        console.error("Failed to delete HOD:", error);

        this.loading.set(false);

        this.toast.error(error?.error?.message || "Failed to delete HOD.");
      },
    });
  }

  // =====================================================
  // DISPLAY RANGE
  // =====================================================

  get startRecord(): number {
    if (this.totalElements() === 0) {
      return 0;
    }

    return this.page() * this.size() + 1;
  }

  get endRecord(): number {
    return Math.min((this.page() + 1) * this.size(), this.totalElements());
  }

  // =====================================================
  // PAGE NUMBERS
  // =====================================================

  get pageNumbers(): number[] {
    const total = this.totalPages();

    if (total <= 0) {
      return [];
    }

    const maxVisible = 5;

    // ---------------------------------------------------
    // Total pages <= 5
    // ---------------------------------------------------

    if (total <= maxVisible) {
      return Array.from(
        {
          length: total,
        },
        (_, index) => index,
      );
    }

    // ---------------------------------------------------
    // Dynamic range
    // ---------------------------------------------------

    let start = Math.max(0, this.page() - 2);

    let end = start + maxVisible;

    if (end > total) {
      end = total;

      start = end - maxVisible;
    }

    return Array.from(
      {
        length: end - start,
      },
      (_, index) => start + index,
    );
  }
}
