import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { finalize } from "rxjs";

import { TimetableService } from "../../services/timetable.service";
import { Timetable } from "../../models/timetable.model";
import { TimetableSearch } from "../../models/timetable-search.model";
import { TimetableSearchFilterComponent } from "../../components/timetable-search-filter.component";
import { TimetableTableComponent } from "../../components/timetable-table.component";
import { ToastService } from "../../../../shared/services/toast.service";
import { PAGE_SIZE_OPTIONS } from "../../shared/timetable.constants";

@Component({
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TimetableSearchFilterComponent,
    TimetableTableComponent,
  ],
  templateUrl: "./timetable-list.component.html",
  styleUrl: "./timetable-list.component.scss",
})
export class TimetableListComponent {
  private readonly service = inject(TimetableService);
  private readonly toast = inject(ToastService);

  readonly rows = signal<Timetable[]>([]);
  readonly loading = signal(false);

  readonly page = signal(0);
  readonly size = signal(10);

  readonly totalElements = signal(0);
  readonly totalPages = signal(0);

  readonly currentSearch = signal<TimetableSearch | undefined>(undefined);

  readonly pageSizeOptions = PAGE_SIZE_OPTIONS;

  ngOnInit(): void {
    this.load();
  }

  load(search?: TimetableSearch): void {
    /*
     * Search event:
     * - Save new search criteria
     * - Reset pagination to first page
     *
     * Reset event:
     * - Clear current search completely
     * - Reset pagination to first page
     */
    if (search !== undefined) {
      this.currentSearch.set(search);
      this.page.set(0);
    } else {
      this.currentSearch.set(undefined);
      this.page.set(0);
    }

    this.loading.set(true);

    const page = this.page();
    const size = this.size();
    const currentSearch = this.currentSearch();

    const request$ = currentSearch
      ? this.service.search(currentSearch, page, size)
      : this.service.list(page, size);

    request$.pipe(finalize(() => this.loading.set(false))).subscribe({
      next: (response) => {
        this.rows.set(response?.content ?? []);

        this.totalElements.set(response?.totalElements ?? 0);

        this.totalPages.set(response?.totalPages ?? 0);
      },

      error: (error) => {
        console.error("Failed to load timetable:", error);

        this.rows.set([]);
        this.totalElements.set(0);
        this.totalPages.set(0);

        this.toast.error(
          error?.error?.message || "Unable to load timetable records.",
        );
      },
    });
  }

  changePage(page: number): void {
    if (page < 0 || page >= this.totalPages() || page === this.page()) {
      return;
    }

    this.page.set(page);
    this.loadCurrentPage();
  }

  previousPage(): void {
    if (this.page() > 0) {
      this.page.update((value) => value - 1);
      this.loadCurrentPage();
    }
  }

  nextPage(): void {
    if (this.page() + 1 < this.totalPages()) {
      this.page.update((value) => value + 1);
      this.loadCurrentPage();
    }
  }

  changeSize(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const value = Number(target.value);

    if (!Number.isInteger(value) || value <= 0) {
      return;
    }

    this.size.set(value);
    this.page.set(0);

    this.loadCurrentPage();
  }

  remove(id: number): void {
    if (!Number.isInteger(id) || id <= 0) {
      this.toast.error("Invalid timetable ID.");
      return;
    }

    this.loading.set(true);

    this.service
      .delete(id)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.toast.success("Timetable deleted successfully.");

          /*
           * If last record of current page was deleted,
           * move to previous page.
           */
          if (this.rows().length === 1 && this.page() > 0) {
            this.page.update((value) => value - 1);
          }

          this.loadCurrentPage();
        },

        error: (error) => {
          console.error("Failed to delete timetable:", error);

          this.toast.error(
            error?.error?.message || "Unable to delete timetable.",
          );
        },
      });
  }

  private loadCurrentPage(): void {
    this.loading.set(true);

    const page = this.page();
    const size = this.size();
    const currentSearch = this.currentSearch();

    const request$ = currentSearch
      ? this.service.search(currentSearch, page, size)
      : this.service.list(page, size);

    request$.pipe(finalize(() => this.loading.set(false))).subscribe({
      next: (response) => {
        this.rows.set(response?.content ?? []);

        this.totalElements.set(response?.totalElements ?? 0);

        this.totalPages.set(response?.totalPages ?? 0);
      },

      error: (error) => {
        console.error("Failed to load timetable:", error);

        this.rows.set([]);
        this.totalElements.set(0);
        this.totalPages.set(0);

        this.toast.error(
          error?.error?.message || "Unable to load timetable records.",
        );
      },
    });
  }

  get startRecord(): number {
    if (this.totalElements() === 0) {
      return 0;
    }

    return this.page() * this.size() + 1;
  }

  get endRecord(): number {
    return Math.min((this.page() + 1) * this.size(), this.totalElements());
  }

  get pageNumbers(): number[] {
    const total = this.totalPages();

    if (total <= 0) {
      return [];
    }

    const maxVisible = 5;

    if (total <= maxVisible) {
      return Array.from({ length: total }, (_, index) => index);
    }

    let start = Math.max(0, this.page() - 2);

    let end = start + maxVisible;

    if (end > total) {
      end = total;
      start = end - maxVisible;
    }

    return Array.from({ length: end - start }, (_, index) => start + index);
  }
}
