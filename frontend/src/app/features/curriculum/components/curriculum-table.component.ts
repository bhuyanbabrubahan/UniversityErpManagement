import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output, inject } from "@angular/core";
import { RouterLink } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";

import { Curriculum } from "../models/curriculum.model";
import {
  DeleteCurriculumDialogComponent,
  DeleteCurriculumDialogData,
} from "./delete-curriculum-dialog.component";

@Component({
  selector: "app-curriculum-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./curriculum-table.component.html",
  styleUrl: "./curriculum-table.component.scss",
})
export class CurriculumTableComponent {
  private readonly dialog = inject(MatDialog);

  @Input() rows: Curriculum[] = [];

  @Input() loading = false;

  @Output() remove = new EventEmitter<number>();

  delete(row: Curriculum): void {
    if (!Number.isInteger(row.id) || row.id <= 0) {
      console.error("Invalid curriculum ID:", row.id);
      return;
    }

    const data: DeleteCurriculumDialogData = {
      code: row.code,
      subject: row.subject,
    };

    const dialogRef = this.dialog.open(DeleteCurriculumDialogComponent, {
      width: "480px",
      maxWidth: "calc(100vw - 32px)",
      data,
      disableClose: true,
      autoFocus: false,
    });

    dialogRef.afterClosed().subscribe((confirmed) => {
      if (confirmed === true) {
        this.remove.emit(row.id);
      }
    });
  }

  trackById(index: number, row: Curriculum): number {
    return row.id;
  }
}
