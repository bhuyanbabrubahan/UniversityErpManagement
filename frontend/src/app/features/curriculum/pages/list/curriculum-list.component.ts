import { CommonModule } from "@angular/common";
import { Component, OnInit, inject, signal } from "@angular/core";
import { RouterLink } from "@angular/router";

import { CurriculumService } from "../../services/curriculum.service";
import { Curriculum } from "../../models/curriculum.model";
import { CurriculumPage } from "../../models/curriculum-page.model";

import { CurriculumSearchFilterComponent } from "../../components/curriculum-search-filter.component";
import { CurriculumTableComponent } from "../../components/curriculum-table.component";

import { ToastService } from "../../../../shared/services/toast.service";

@Component({
  selector: "app-curriculum-list",
  standalone: true,

  imports: [
    CommonModule,
    RouterLink,
    CurriculumSearchFilterComponent,
    CurriculumTableComponent,
  ],

  templateUrl: "./curriculum-list.component.html",
  styleUrl: "./curriculum-list.component.scss",
})
export class CurriculumListComponent implements OnInit {
  private readonly service = inject(CurriculumService);

  private readonly toast = inject(ToastService);

  readonly rows = signal<Curriculum[]>([]);

  readonly loading = signal(false);

  readonly page = signal(0);

  readonly size = signal(10);

  readonly totalElements = signal(0);

  readonly totalPages = signal(0);

  readonly currentSearch = signal<
    | {
        code?: string;
        status?: string;
        keyword?: string;
      }
    | undefined
  >(undefined);

  ngOnInit(): void {
    this.load();
  }

  load(search?: { code?: string; status?: string; keyword?: string }): void {
    if (search !== undefined) {
      this.currentSearch.set(search);
      this.page.set(0);
    }

    this.loading.set(true);

    const currentPage = this.page();
    const currentSize = this.size();
    const searchValue = this.currentSearch();

    const request$ = searchValue
      ? this.service.search(searchValue, currentPage, currentSize)
      : this.service.list(currentPage, currentSize);

    request$.subscribe({
      next: (response: CurriculumPage) => {
        this.rows.set(response?.content ?? []);

        this.totalElements.set(response?.totalElements ?? 0);

        this.totalPages.set(response?.totalPages ?? 0);

        this.loading.set(false);
      },

      error: (error) => {
        console.error("Failed to load curriculum:", error);

        this.rows.set([]);

        this.totalElements.set(0);

        this.totalPages.set(0);

        this.loading.set(false);

        this.toast.error(
          error?.error?.message || "Unable to load curriculum records.",
        );
      },
    });
  }

  changePage(page: number): void {
    if (page < 0 || page >= this.totalPages() || page === this.page()) {
      return;
    }

    this.page.set(page);

    this.load();
  }

  previousPage(): void {
    if (this.page() > 0) {
      this.page.set(this.page() - 1);
      this.load();
    }
  }

  nextPage(): void {
    if (this.page() + 1 < this.totalPages()) {
      this.page.set(this.page() + 1);
      this.load();
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

    this.load();
  }

  remove(id: number): void {
    if (!Number.isInteger(id) || id <= 0) {
      this.toast.error("Invalid curriculum ID.");
      return;
    }

    this.loading.set(true);

    this.service.delete(id).subscribe({
      next: () => {
        this.toast.success("Curriculum deleted successfully.");

        /*
         * Agar current page par sirf ek record tha
         * aur page first page nahi hai,
         * previous page par move karo.
         */
        if (this.rows().length === 1 && this.page() > 0) {
          this.page.set(this.page() - 1);
        }

        /*
         * Current search/filter + current page ke saath
         * list refresh hogi.
         */
        this.load();
      },

      error: (error) => {
        console.error("Failed to delete curriculum:", error);

        this.loading.set(false);

        this.toast.error(
          error?.error?.message || "Failed to delete curriculum.",
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
