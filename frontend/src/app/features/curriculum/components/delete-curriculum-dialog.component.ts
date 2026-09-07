import { Component, inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { MATERIAL_MODULES } from "@app/shared/material/material";

export interface DeleteCurriculumDialogData {
  code?: string;
  subject?: string;
}

@Component({
  selector: "app-delete-curriculum-dialog",
  standalone: true,
  imports: [...MATERIAL_MODULES],
  templateUrl: "./delete-curriculum-dialog.component.html",
  styleUrl: "./delete-curriculum-dialog.component.scss",
})
export class DeleteCurriculumDialogComponent {
  private readonly dialogRef = inject(
    MatDialogRef<DeleteCurriculumDialogComponent>,
  );

  readonly data = inject<DeleteCurriculumDialogData>(MAT_DIALOG_DATA);

  cancel(): void {
    this.dialogRef.close(false);
  }

  confirm(): void {
    this.dialogRef.close(true);
  }
}
