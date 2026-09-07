import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { MATERIAL_MODULES } from '../../../../shared/material/material';


@Component({
  selector: 'app-delete-student-dialog',
  standalone: true,
  imports: [
    ...MATERIAL_MODULES
  ],
  templateUrl: './delete-student-dialog.component.html',
  styleUrl: './delete-student-dialog.component.scss'
})
export class DeleteStudentDialogComponent {


  private readonly dialogRef =
    inject(
      MatDialogRef<DeleteStudentDialogComponent>
    );


  readonly data = inject(MAT_DIALOG_DATA);



  cancel(): void {

    this.dialogRef.close(false);

  }



  confirm(): void {

    this.dialogRef.close(true);

  }


}