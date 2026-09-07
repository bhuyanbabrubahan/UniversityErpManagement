import { Component, Inject } from "@angular/core";

import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from "@angular/material/dialog";

@Component({
  selector: "app-delete-timetable-dialog",
  standalone: true,

  imports: [MatDialogModule],

  templateUrl: "./delete-timetable-dialog.component.html",

  styleUrl: "./delete-timetable-dialog.component.scss",
})
export class DeleteTimetableDialogComponent {
  constructor(
    private readonly dialogRef: MatDialogRef<DeleteTimetableDialogComponent>,

    @Inject(MAT_DIALOG_DATA)
    readonly data: DeleteTimetableDialogData,
  ) {}

  cancel(): void {
    this.dialogRef.close(false);
  }

  confirm(): void {
    this.dialogRef.close(true);
  }
}

export interface DeleteTimetableDialogData {
  code: string;
  subject: string;
}
