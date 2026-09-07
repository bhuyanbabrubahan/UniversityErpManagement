import {
  Component
} from '@angular/core';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { inject } from '@angular/core';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

@Component({
  selector: 'app-delete-faculty-dialog',
  standalone: true,
  imports: [
    ...MATERIAL_MODULES
  ],
  templateUrl: './delete-faculty-dialog.component.html',
  styleUrl: './delete-faculty-dialog.component.scss'
})
export class DeleteFacultyDialogComponent {

  private readonly dialogRef =
    inject(MatDialogRef<DeleteFacultyDialogComponent>);

  // =====================================================
  // CONFIRM
  // =====================================================

  confirm(): void {

    this.dialogRef.close(true);
  }

  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {

    this.dialogRef.close(false);
  }
}