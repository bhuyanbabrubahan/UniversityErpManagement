import { CommonModule } from "@angular/common";

import { Component, OnInit, inject, signal } from "@angular/core";

import { ActivatedRoute, Router, RouterLink } from "@angular/router";

import { CurriculumService } from "../../services/curriculum.service";

import { Curriculum } from "../../models/curriculum.model";

import { ToastService } from "../../../../shared/services/toast.service";

@Component({
  selector: "app-curriculum-view",
  standalone: true,

  imports: [CommonModule, RouterLink],

  templateUrl: "./curriculum-view.component.html",

  styleUrl: "./curriculum-view.component.scss",
})
export class CurriculumViewComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly service = inject(CurriculumService);

  private readonly toast = inject(ToastService);

  readonly item = signal<Curriculum | null>(null);

  readonly loading = signal(false);

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get("id");

    if (!idParam) {
      this.toast.error("Curriculum ID is missing.");

      this.goBack();

      return;
    }

    const id = Number(idParam);

    if (!Number.isInteger(id) || id <= 0) {
      this.toast.error("Invalid curriculum ID.");

      this.goBack();

      return;
    }

    this.load(id);
  }

  private load(id: number): void {
    this.loading.set(true);

    this.service.get(id).subscribe({
      next: (response) => {
        this.item.set(response);

        this.loading.set(false);
      },

      error: (error) => {
        console.error("Failed to load curriculum:", error);

        this.loading.set(false);

        this.toast.error(error?.error?.message || "Unable to load curriculum.");

        this.goBack();
      },
    });
  }

  goBack(): void {
    this.router.navigate(["../"], {
      relativeTo: this.route,
    });
  }
}
