import { Component, inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { MATERIAL_MODULES } from "@app/shared/material/material";

export interface DeleteHodDialogData {
  code?: string;
  fullName?: string;
  department?: string;
}

@Component({
  selector: "app-delete-hod-dialog",
  standalone: true,
  imports: [...MATERIAL_MODULES],
  templateUrl: "./delete-hod-dialog.component.html",
  styleUrl: "./delete-hod-dialog.component.scss",
})
export class DeleteHodDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<DeleteHodDialogComponent>);

  readonly data = inject<DeleteHodDialogData>(MAT_DIALOG_DATA);

  cancel(): void {
    this.dialogRef.close(false);
  }

  confirm(): void {
    this.dialogRef.close(true);
  }
}
